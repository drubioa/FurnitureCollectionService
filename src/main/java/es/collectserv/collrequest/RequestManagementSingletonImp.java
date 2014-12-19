package es.collectserv.collrequest;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.ServiceUnavailableException;

import org.joda.time.LocalDate;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;

public class RequestManagementSingletonImp implements RequestManagementSingleton{
	private static RequestManagementSingletonImp INSTANCE = null;
	private final int MAX_FURNITURES_PER_USER = 12;
	private AtomicBoolean inUse;
	private CopyOnWriteArrayList<DailyServices> mDays;
	private static SqlConector session;
	
	private synchronized static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new RequestManagementSingletonImp();
        }
    }
	
	public void setListDaylyServices(List<DailyServices> days){
		mDays  =  new CopyOnWriteArrayList<DailyServices>(days);
	}
	
	public static RequestManagementSingletonImp getInstance() {
        if (INSTANCE == null){
        	createInstance();
        }
        return INSTANCE;
    }

	private RequestManagementSingletonImp(){
		inUse = new AtomicBoolean(false);
		mDays = new CopyOnWriteArrayList<DailyServices>();
		session = new SqlConectorImp();
		try {
			List<LocalDate> dates = session.selectAllCollectionDays();
			for(LocalDate d : dates){ //Add all current collection request
				mDays.add(new DailyServices(d));
			}
			Collections.sort(mDays);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean userGotPreviosRequest(String phone) 
			throws InterruptedException, IOException{
		boolean existRequest = false;
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		try {
			if(checkIfUserGotPrevProvReq(phone) || checkIfExistPendingRequest(phone)){
				existRequest = true;
			}
		} catch (IOException e) {
			inUse.set(false);
			notifyAll();
			throw e;
		}
		inUse.set(false);
		notifyAll();
		return existRequest;
	}
	
	/**
	 * Comprueba si el usuario tiene una solicitud pendiente de confirmar.
	 * @param phone
	 * @return
	 * @throws IOException
	 */
	private boolean checkIfExistPendingRequest(String phone) throws IOException {
		return session.getPendingCollectionRequest(phone).size() > 0;
	}

	/**
	 * Comprueba si el usuario tiene solicitudes provisionales previas.
	 * @param phoneNumber
	 * @return boolean con la evaluacion de si el usuario tiene alguna solicitud previa.
	 * @throws InterruptedException
	 */
	private boolean checkIfUserGotPrevProvReq(String phoneNumber) 
			throws InterruptedException{
		boolean existRequest = false;
		for(DailyServices d : mDays){
			existRequest = d.checkIfuserGotPreviousRequest(phoneNumber);
			if(existRequest){
				break;
			}
		}
		return existRequest;
	}

	public synchronized List<ProvisionalAppointment> 
	getAppointmentToConfirm(String phone_number,int itemsRequest,int collectionPointId) 
			throws Exception{
		if(checkIfExistPendingRequest(phone_number) ||
				checkIfUserGotPrevProvReq(phone_number)){
			throw new IllegalArgumentException("User with phone "+phone_number+
					" got pending request, and he can't realize new request yet.");
		}
		if(itemsRequest > MAX_FURNITURES_PER_USER){
			throw new IllegalArgumentException("Too furnitures to collect.Limit is "
					+MAX_FURNITURES_PER_USER);			
		}
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		// Exclusion mutua
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		// If exist invalid services days they must be removes.
		if(!AppointmentManteiner.isValidDailyServices(mDays)){
			mDays = new CopyOnWriteArrayList<DailyServices>(
					AppointmentManteiner.removeInvalidServiceDate(mDays));
			inUse.set(false);
			notifyAll();
			return getAppointmentToConfirm(phone_number,
					itemsRequest,collectionPointId);
		}
		appintment_list.addAll(
				createProvisionalAppointments(itemsRequest,phone_number,collectionPointId));
		// Fin de la exclusion mutua
		inUse.set(false);
		notifyAll();
		return appintment_list;
	}
	
	/**
	 * Genera una lista de solicitudes pendientes de confirmar para el usuario
	 * con el telefono indicado, y que responde a los items solicitados.
	 * @param itemsRequest
	 * @param phone_number
	 * @param last_service_day
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> createProvisionalAppointments(int itemsRequest,
			String phone_number, int collectionPointId) throws Exception{
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		for(DailyServices d : mDays){
			int realizablesFurPerReq = 
					d.obtainRealizablePeticions(phone_number);
			if(realizablesFurPerReq > 0){
				if(itemsRequest <= realizablesFurPerReq){
					appintment_list.add(d.getProvisionalAppointment(phone_number, 
							itemsRequest, collectionPointId));
					itemsRequest = 0;
					break;
				}else{
					appintment_list.add(d.getProvisionalAppointment(phone_number, 
							realizablesFurPerReq, collectionPointId));
					itemsRequest -= realizablesFurPerReq;
				}
			}
		}
		// Se creab nuevos dias de recogida
		if(itemsRequest > 0){
			appintment_list.addAll(createNewServiceDaysForAppointment(itemsRequest,
					phone_number,collectionPointId));
		}
		return appintment_list;
	}

	/**
	 * Se crean nuevos dias de servicio para el usuario con el numero de telefono indicado
	 * y se devuelve un listado de solicitudes pendientes de confirmar, a partir del 
	 * ultimo dia en el que se solicitco servicio de recogida.
	 * @param itemsRequest
	 * @param phone_number
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> createNewServiceDaysForAppointment(
			int itemsRequest, String phone_number, int collectionPointId) 
					throws Exception {
		List<ProvisionalAppointment> appointment_list = 
				new ArrayList<ProvisionalAppointment>();
		LocalDate last_day;
		if(mDays.isEmpty()){
			Calendar now = Calendar.getInstance();
			last_day = new LocalDate(now);
		}else{
			last_day = (mDays.get(mDays.size() - 1)).getDate();
		}
		while(itemsRequest > 0){
			last_day = last_day.plusDays(1);
			DailyServices day = new DailyServices(last_day);
			int furnitureRealizables = day.obtainRealizablePeticions(phone_number);
			if(furnitureRealizables >= itemsRequest){
				appointment_list.add(day.getProvisionalAppointment(phone_number,
						itemsRequest,collectionPointId));
				itemsRequest = 0;
			}
			else{
				appointment_list.add(day.getProvisionalAppointment(phone_number,
						furnitureRealizables,collectionPointId));
				itemsRequest -= furnitureRealizables;
			}
			mDays.add(day);
		}
		return appointment_list;
	}

	public synchronized void confirmProvisionalAppointment(
			CollectionRequest request) 
					throws ServiceUnavailableException, IOException, NotBoundException, 
					InterruptedException {
		if(request == null){
			throw new ServiceUnavailableException("request is null");
		}
		if(!request.checkCorrectRequest()){
			throw new ServiceUnavailableException("Request got incorrect format. \n"+
					request.toString());
		}
		if(mDays.size() == 0){
			throw new ServiceUnavailableException("DailyServices is empty");			
		}
		DailyServices dailyService = null;
		for(DailyServices d : mDays){
			if(d.getServiceDate().isEqual(request.getFch_collection())){
				dailyService = d;
				break;
			}
		}
		if(dailyService == null){
			throw new NotBoundException("dailyService not found for day "+
						request.getFch_collection());
		}
		try {
			if(inUse.get()){
				wait();
			}
			inUse.set(true);
			dailyService.confirmProvisionalAppointment(request.getTelephone());
			inUse.set(false);
			notifyAll();
			session.registerRequestInDB(request);
		} catch (IOException e) {
			inUse.set(false);
			notifyAll();
			throw e;
		} catch (InterruptedException e) {
			inUse.set(false);
			notifyAll();
			throw e;
		} catch (UnsupportedOperationException e) {
			inUse.set(false);
			notifyAll();
			throw e;
		}
	}

	/**
	 * Incrementa el numero de muebles que se pueden recoger debido a una solicitud
	 * de recogida no realizada y cancelada.
	 * @param numFurnitures
	 * @param fch_collection
	 * @throws InterruptedException 
	 */
	private void addDisponiblesCollectionFurnitures(int numFurnitures,
			LocalDate fch_collection) throws InterruptedException {
		for(DailyServices d : mDays){
			if(d.getDate() == fch_collection){
				d.addFurnituresPerDayFromCanceledRequest(numFurnitures);
			}
		}
	}

	public synchronized void cancelPeendingRequest(String phone) 
			throws InterruptedException, IOException {
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		try {
			if(checkIfExistPendingRequest(phone)){
				removePendingCollectionRequest(phone);
			}
			else{
				removesUnconfirmedAppointments(phone);
			}
		} catch (IOException e) {
			inUse.set(false);
			notifyAll();	
			throw e;
		}
		inUse.set(false);
		notifyAll();	
	}

	private void removePendingCollectionRequest(String phone) throws IOException, InterruptedException{
		for(CollectionRequest c : session.getPendingCollectionRequest(phone)){
			session.removesPendingRequestFromBD(c);
			addDisponiblesCollectionFurnitures(
					c.getNumFurnitures(),c.getFch_collection());
		}
	}
	
	/**
	 * Se eliminan todas las solicitudes pendientes de realizar del usuario.
	 * @param phone
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	private void removesUnconfirmedAppointments(String phone) 
			throws InterruptedException, IOException{
		for(DailyServices day : mDays){
			if(day.checkIfuserGotPreviousRequest(phone)){
				day.removeAppointment(phone);
			}
		}
	}

	/**
	 * Se declara objeto no clonable
	 */
	public Object clone() throws CloneNotSupportedException {
    	throw new CloneNotSupportedException(); 
}
}
