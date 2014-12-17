package es.collectserv.collrequest;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joda.time.LocalDate;

import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;

/**
 * Esta clase representa el servicio de recogida diario. Dentro del servicio diario
 * se establecen un máximo muebles que peuden ser recogidos al día, y un límite
 * de enseres solicitados por usuarios. 
 * @author Diego Rubio Abujas
 * @version 1.0
 */
public class DailyServices implements Comparable<DailyServices> {
	private static LocalDate LAST_DATE;
	private final LocalDate mDate;
	private static final int MAX_FUNRITNURES_PER_DAY = 24;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private List<ProvisionalAppointment> mRequestToConfirmation;
	private ExecutorService poolOfThreads = 
			Executors.newFixedThreadPool(MAX_FUNRITNURES_PER_DAY);
	private int mFurniteres_per_day;
	private AtomicBoolean inUse; // By control of concurrency
	private static SqlConector sesion;
	
	public DailyServices(LocalDate last_day) throws IllegalArgumentException, IOException{
		final LocalDate today = new LocalDate();
		if(last_day.isBefore(today)){
			throw new IllegalArgumentException("invalid day, it must "
					+ "be later than the current day ("+last_day.toString()+")");
		}
		if(LAST_DATE == null){
			Calendar now = Calendar.getInstance();
			LAST_DATE = new LocalDate(now);
		}
		else if(!last_day.isAfter(LAST_DATE)){
			throw new IllegalArgumentException("invalid day "+last_day+", it must "
					+ "be later than the last service day ("+LAST_DATE+")");
		}
		else{
			LAST_DATE = last_day;
		}
		sesion = new SqlConectorImp();
		inUse = new AtomicBoolean(false);
		mRequestToConfirmation = new ArrayList<ProvisionalAppointment>();
		this.mDate = last_day;
		mFurniteres_per_day = sesion.selectFurnituresByDay(last_day);
	}	
	

	/**
	 * Se obtiene una solicitud pendiente de confirmar para este dia de servicio de recogida.
	 * @param phone
	 * @param num_furnitures
	 * @param pointId
	 * @return ProvisionalAppointment
	 * @throws InterruptedException 
	 * @throws NotBoundException 
	 * @throws Exception el usuario no debe tener solicitudes pendientes de confirmar.
	 */
	public synchronized ProvisionalAppointment getProvisionalAppointment(String phone,
			int num_furnitures,int pointId) throws IllegalArgumentException, InterruptedException, NotBoundException{
		if(findAppointment(phone) != null){
			throw new IllegalArgumentException("User got pending request in this date.");
		}
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		if((calculateRealizablePetitions() < num_furnitures)
				|| userGotProvisionalAppointment(phone)
				|| ((num_furnitures + mFurniteres_per_day) > MAX_FUNRITNURES_PER_DAY)){
			inUse.set(false);
			notifyAll();
			throw new NotBoundException("Resquest is not realizable.");
		}
		ProvisionalAppointment requestToConfirmation = 
				new ProvisionalAppointment(num_furnitures,phone,pointId,mDate);
		mFurniteres_per_day += num_furnitures;
		mRequestToConfirmation.add(requestToConfirmation);
		requestToConfirmation.setRequestManagement(this);
		poolOfThreads.execute(requestToConfirmation);
		inUse.set(false); 
		notifyAll();
		return requestToConfirmation;
	}
	
	/**
	 * 
	 * @return amount of request realizables of current day
	 */
	private int calculateRealizablePetitions(){
		int realizables = 0; // By default 
		if(MAX_FUNRITNURES_PER_DAY == mFurniteres_per_day){
			realizables = 0;
		}
		else if((MAX_FUNRITNURES_PER_DAY - mFurniteres_per_day) >= 
			MAX_FURNIUTRES_PER_DAY_USER){
				realizables = MAX_FURNIUTRES_PER_DAY_USER;
		}
		else{
			realizables = MAX_FUNRITNURES_PER_DAY - mFurniteres_per_day;
		}
		return realizables;
	} 
	
	/**
	 * Devuelve el número de enseres que se pueden solicitar para dicho dia de servicio
	 * @throws InterruptedException 
	 */
	public synchronized int obtainRealizablePeticions(String phone_number) 
			throws InterruptedException{
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		int realizables;
		if(userGotProvisionalAppointment(phone_number)){
			realizables = 0;
		}
		else{
			realizables = calculateRealizablePetitions();
		}
		inUse.set(false);
		notifyAll();
		return realizables;
	} 

	/**
	 * Comprueba si el usuario con dicho número de teléfono tiene una solicitud previa
	 * @param phone
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized boolean checkIfuserGotPreviousRequest(String phone) 
			throws InterruptedException{
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		boolean exists = userGotProvisionalAppointment(phone);
		inUse.set(false);
		notifyAll();
		return exists;
	}

	/**
	 * Busca las solicitudes anteriores para un numero de teléfono, devuelve null 
	 * en caso de no poder localizarla.
	 * @param phone
	 * @return
	 */
	private boolean userGotProvisionalAppointment(String phone){
		boolean exists = false;
		for(ProvisionalAppointment a : mRequestToConfirmation){
			if(a.getTelephone().equals(phone)){
				exists = true;
				break;
			}
		}
		return exists;
	} 
	
	
	public LocalDate getServiceDate(){
		return this.mDate;
	}


	public synchronized void confirmProvisionalAppointment(String phone) 
			throws InterruptedException, UnsupportedOperationException {
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		ProvisionalAppointment appointment = findAppointment(phone);
		if(appointment == null){
			inUse.set(false);
			notifyAll();
			throw new UnsupportedOperationException("Appointment not correspond to this service day for "
					+ "this phone number("+phone+").");
		}
		else{
			mRequestToConfirmation.remove(appointment);
			inUse.set(false);
			notifyAll();
		}
	}
	
	/**
	 * Localiza la solicitud pendiente de confirmar para el telefono indicado
	 * @param phone
	 * @return la solicitud pendiente de localizar o nulo.
	 * @throws InterruptedException 
	 */
	private ProvisionalAppointment findAppointment(String phone){
		ProvisionalAppointment appointment = null;
		for(ProvisionalAppointment a : mRequestToConfirmation){
			if(a.getTelephone().contains(phone)){
				appointment = a;
				break;
			}
		}
		return appointment;
	}
	
	/** Obtiene el numero de mueble solicitados para recoger en dicho dia */
	public int getmFurniteres_per_day(){
		return mFurniteres_per_day;
	}

	public synchronized void removeAppointment(String phone) 
			throws InterruptedException{
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		if(userGotProvisionalAppointment(phone)){
			inUse.set(true);
			ProvisionalAppointment a = findAppointment(phone);
			mFurniteres_per_day -= a.getNumFurnitures();
			mRequestToConfirmation.remove(a);
		}
		inUse.set(false);
		notifyAll();
	}

	/**
	 * Se elimina una solicitud pendiente de confirmar.
	 * @param appointment
	 * @throws InterruptedException
	 */
	public synchronized void removeUnconfirmedAppointment(
			ProvisionalAppointment appointment) 
			throws InterruptedException {
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		// if remove appointment, update the realizable collection value
		if(mRequestToConfirmation.remove(appointment)){
			mFurniteres_per_day -= appointment.getNumFurnitures();
		}
		inUse.set(false);
		notifyAll();
	}

	/**
	 * Se agrega un numero de muebles que se pueden recoger en dicho dia de servicio 
	 * debido a una cancelacion de una solicitud confirmada.
	 * @param n
	 * @throws InterruptedException
	 */
	public synchronized void addFurnituresPerDayFromCanceledRequest(int n) 
			throws InterruptedException {
		// Check arguments
		while(inUse.get()){
			wait();
		}
		inUse.set(true);
		if(n + mFurniteres_per_day > MAX_FUNRITNURES_PER_DAY || 
				n > MAX_FURNIUTRES_PER_DAY_USER){
			throw new IllegalArgumentException("invalid number of "
					+ " for dayly service");
		}
		mFurniteres_per_day += n;
		inUse.set(false);
		notifyAll();
	}

	/**
	 * 
	 * @return fecha correspondientes al dia de servicio.
	 */
	public LocalDate getDate(){
		return mDate;
	}

	public int compareTo(DailyServices o) {
		if (getDate() == null || o.getDate() == null){
		      return 0;
		}
		return getDate().compareTo(o.getDate());
	}
}
