package es.collectserv.collrequest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;

/**
 * Esta clase representa el servicio de recogida diario. Dentro del servicio diario
 * se establecen un máximo muebles que peuden ser recogidos al día, y un límite
 * de enseres solicitados por usuarios. 
 * @author Diego Rubio Abujas
 * @version 1.0
 */
public class DailyServicesImp implements DailyServices{
	private final Date day;
	private static final int MAX_FUNRITNURES_PER_DAY = 24;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private List<ProvisionalAppointment> requestToConfirmation;
	private ExecutorService ex = Executors.newFixedThreadPool(MAX_FUNRITNURES_PER_DAY);
	private int furniteres_per_day;
	private boolean inUse;
	
	public DailyServicesImp(Date day) throws Exception{
		if(day.before(Calendar.getInstance().getTime())){
			throw new Exception("invalid day, it must "
					+ "be later than the current day ("+day.toString()+")");
		}
		inUse = false;
		requestToConfirmation = new ArrayList<ProvisionalAppointment>();
		this.day = day;
		SqlSession session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		furniteres_per_day = 
				session.selectOne("CollectionRequestMapper"
					+".selectFurnituresByDay",day);
		if(furniteres_per_day > MAX_FUNRITNURES_PER_DAY){
			throw new Exception("Invalid number of furnirutre request for this day");
		}
	}	
	

	public synchronized ProvisionalAppointment getAppointment(String phone,
			int num_furnitures,int pointId) throws Exception{
		if(inUse){
			wait();
		}
		inUse = true;
		if((calculateRealizablePetitions() < num_furnitures)
				|| findPreviousRequest(phone)
				|| ((num_furnitures + furniteres_per_day) > MAX_FUNRITNURES_PER_DAY)){
			inUse = false;
			notifyAll();
			throw new Exception("Resquest is not realizable");
		}
		ProvisionalAppointment requestToConfirmation = 
				new ProvisionalAppointment(num_furnitures,phone,pointId,day);
		this.furniteres_per_day += num_furnitures;
		requestToConfirmation.setRequestManagement(this);
		ex.execute(requestToConfirmation);
		this.requestToConfirmation.add(requestToConfirmation);
		inUse = false;
		notifyAll();
		return requestToConfirmation;
	}
	
	private int calculateRealizablePetitions(){
		int realizables = 0;
		inUse = true;
		if(MAX_FUNRITNURES_PER_DAY != furniteres_per_day){
			if((MAX_FUNRITNURES_PER_DAY - furniteres_per_day) > 
			MAX_FURNIUTRES_PER_DAY_USER){
				realizables = MAX_FURNIUTRES_PER_DAY_USER;
			}
			else{
				realizables =  (MAX_FUNRITNURES_PER_DAY - furniteres_per_day);
			}
		}
		return realizables;
	} 
	
	/**
	 * Devuelve el número de enseres que se pueden solicitar para dicho dia de servicio
	 * @throws InterruptedException 
	 */
	public synchronized int obtainRealizablePeticions() throws InterruptedException{
		if(inUse){
			wait();
		}
		inUse = true;
		int realizables = calculateRealizablePetitions();
		inUse = false;
		notifyAll();
		return realizables;
	} 

	/**
	 * Comprueba si el usuario con dicho número de teléfono tiene una solicitud previa
	 * @param phone
	 * @return
	 * @throws InterruptedException 
	 */
	public synchronized boolean userGotPreviousRequest(String phone) 
			throws InterruptedException{
		if(inUse){
			wait();
		}
		inUse = true;
		boolean exists = findPreviousRequest(phone);
		inUse = false;
		notifyAll();
		return exists;
	}

	/**
	 * Busca las solicitudes anteriores para un numero de teléfono, devuelve null 
	 * en caso de no poder localizarla.
	 * @param phone
	 * @return
	 */
	private boolean findPreviousRequest(String phone){
		boolean exists = false;
		for(int i = 0;i < requestToConfirmation.size();i++){
			if(requestToConfirmation.get(i).getTelephone().equals(phone)){
				exists = true;
				break;
			}
		}
		return exists;
	} 
	
	
	public Date getDay(){
		return this.day;
	}


	public void confirmProvisionalAppointment(String phone) throws Exception {
		if(inUse){
			wait();
		}
		inUse = true;
		ProvisionalAppointment appointment = findAppointment(phone);
		inUse = false;
		notifyAll();
		if(appointment == null){
			throw new Exception("Appointment not correspond to this service day for this phone number("+phone+").");
		}
		else{
			requestToConfirmation.remove(appointment);
		}
	}
	
	/**
	 * Localiza la solicitud pendiente de confirmar para el telefono indicado
	 * @param phone
	 * @return la solicitud pendiente de localizar o nulo.
	 */
	private synchronized ProvisionalAppointment findAppointment(String phone) {
		ProvisionalAppointment appointment = null;
		for(int i = 0;i < requestToConfirmation.size() && appointment == null;i++){
			if(requestToConfirmation.get(i).getTelephone() == phone){
				appointment = requestToConfirmation.get(i);
			}
		}
		return appointment;
	}


	public synchronized void removeUnconfirmedAppointment(ProvisionalAppointment appointment) 
			throws InterruptedException {
		int num = appointment.getNumFurnitures();
		if(inUse){
			wait();
		}
		inUse = true;
		// if remove appointment, update the realizable collection value
		if(requestToConfirmation.remove(appointment)){
			this.furniteres_per_day -= num;
		}
		inUse = false;
		notifyAll();
	}
}
