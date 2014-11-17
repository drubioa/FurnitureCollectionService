package es.collectserv.collrequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.DateMidnight;

import es.collectserv.factories.SimpleMyBatisSesFactory;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

public class RequestManagementimp implements RequestManagement{
	private static boolean inUse;
	private static List<DailyServices> days;
	
	public RequestManagementimp(){
		inUse = false;
		days = new ArrayList<DailyServices>();
		try {
			SqlSession session = new SimpleMyBatisSesFactory()
				.getOpenSqlSesion();
			List<Date> dates = session.selectList(
					"CollectionRequestMapper.selectAllCollectionDays");
			for(int i = 0;i < dates.size();i++){ //Add all current collection request
				days.add(new DailyServicesImp(dates.get(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized boolean userGotPreviosRequest(String phone) 
			throws InterruptedException{
		boolean existRequest = false;
		while(inUse){
			wait();
		}
		inUse = true;
		for(int i = 0;i < days.size();i++){
			existRequest = days.get(i).userGotPreviousRequest(phone);
			if(existRequest){
				break;
			}
		}
		try {
			SqlSession session = new SimpleMyBatisSesFactory()
				.getOpenSqlSesion();
			if(existPreviousRequest(phone,session)){
				existRequest = true;
			}
			session.close();
		}catch (IOException e) {
			inUse = false;
			notifyAll();
			e.printStackTrace();
		}
		inUse = false;
		notifyAll();
		return existRequest;
	}

	private boolean existPreviousRequest(String phone,SqlSession session){
		return session.selectList("CollectionRequestMapper"+
				".selectPendingRequestByPhone",phone).size() > 0;
	}
	
	public synchronized List<ProvisionalAppointment> 
	getAppointmentToConfirm(String phone_number,int itemsRequest,int collectionPointId) 
			throws Exception{
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		// Exclusión mútua
		while(inUse){
			wait();
		}
		inUse = true;
		// Se utilizan los actuales dias de servicio
		appintment_list.addAll(
				createAppointmentInServicesDays(itemsRequest,phone_number,collectionPointId));
		for(int i = 0;i < appintment_list.size();i++){
			itemsRequest -= appintment_list.get(i).getNumFurnitures();
		}
		if(itemsRequest > 0){
		// Se crean nuevos dias de servicio.
			appintment_list.addAll(
					createNewServiceDaysForAppointment(itemsRequest,
							phone_number,collectionPointId));
		}
		// Fin de la exclusión mútua
		inUse = false;
		notifyAll();
		return appintment_list;
	}
	
	/**
	 * Devuelve la primera fecha disponible para crear un nuevo dia de servicio de recogida
	 * @param appintment_list
	 * @return una fecha en la que se podrá crear una nueva fecha de recogida.
	 */
	private Date obtainsFirstCollectionDay(
			List<ProvisionalAppointment> appintment_list) {
		Date date;
		if(appintment_list.size() == 0){
			date = new Date();
		}
		else{
			date = appintment_list.get(appintment_list.size() - 1).getFch_collection();
		}
		return nextDay(date);
	}

	/**
	 * Se crean nuevos días de servicio para el usuario con el número de tléfono indicado
	 * y se devuelve un listado de solicitudes pendientes de confirmar, a partir del 
	 * último día en el que se solítico servicio de recogida.
	 * @param itemsRequest
	 * @param phone_number
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> createNewServiceDaysForAppointment(
			int itemsRequest, String phone_number,int collectionPointId) 
					throws Exception {
		List<ProvisionalAppointment> appointment_list = 
				new ArrayList<ProvisionalAppointment>();
		Date last_day = obtainsFirstCollectionDay(appointment_list);
		while(itemsRequest > 0){
			DailyServices day = new DailyServicesImp(last_day);
			int furnitureRealizables = day.obtainRealizablePeticions();
			if(furnitureRealizables > itemsRequest){
				appointment_list.add(day.getAppointment(phone_number,
						itemsRequest,collectionPointId));
				itemsRequest = 0;
			}
			else{
				appointment_list.add(day.getAppointment(phone_number,
						furnitureRealizables,collectionPointId));
				itemsRequest -= furnitureRealizables;
			}
			last_day = nextDay(last_day);
			days.add(day);
		}
		return appointment_list;
	}

	/**
	 * 
	 * @param day
	 * @return the next day
	 */
	private Date nextDay(Date day){
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		return gc.getTime();
	}
	
	/**
	 * Genera una lista de solicitudes pendientes de confirmar para el usuario
	 * con el tléfono indicado, y que responde a los items solicitados.
	 * @param itemsRequest
	 * @param phone_number
	 * @param last_service_day
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> createAppointmentInServicesDays(int itemsRequest,
			String phone_number, int collectionPointId) throws Exception{
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		for(int i = 0;i < days.size() && itemsRequest > 0;i++){
			if(days.get(i).obtainRealizablePeticions() > 0){
				int furnitureRealizables = 
						days.get(i).obtainRealizablePeticions() - itemsRequest;
				if(furnitureRealizables > 0){
					appintment_list.add(days.get(i).getAppointment(phone_number,
							furnitureRealizables,collectionPointId));
					itemsRequest -= furnitureRealizables;
				}
			}
		}
		return appintment_list;
	}

	public synchronized void confirmProvisionalAppointment(CollectionRequest request) {
		DailyServices dailyService = findDailyService(request.getFch_collection());
		if(dailyService == null){
			throw new RuntimeException("Appointment to Confirm cannot be localized \n"+
					"collection day: "+request.getFch_collection());
		}
		try {
			// Se registra la solicitud y se elimina la cita pen diente de confirmar
			SqlSession session =
					new SimpleMyBatisSesFactory().getOpenSqlSesion();
			session.insert("CollectionRequestMapper.insertCollectionRequest", request);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					request);
			session.commit();
			session.close();
			if(inUse){
				wait();
			}
			inUse = true;
			dailyService.confirmProvisionalAppointment(request.getTelephone());
			inUse = false;
			notifyAll();
		} catch (Exception e) {
			inUse = false;
			notifyAll();
			e.printStackTrace();
		}
	}

	/**
	 * Localiza el dia de servicio correspondiente a una fecha de recogida de enseres
	 * @param fch_collection
	 * @return
	 */
	private DailyServices findDailyService(Date fch_collection) {
		DailyServices day = null;
		DateMidnight b = new DateMidnight(fch_collection);
		for(int i = 0;i < days.size() && day == null;i++){
			DateMidnight a = new DateMidnight(days.get(i).getNextValidServiceDay());
			if(a.isEqual(b)){
				day = days.get(i);
			}
		}
		return day;
	}
}
