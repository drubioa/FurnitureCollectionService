package es.collectserv.collrequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;

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
			for(int i = 0;i < dates.size();i++){
				days.add(new DailyServicesImp(dates.get(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean userGotPreviosRequest(String phone){
		boolean existRequest = false;
		for(int i = 0;i < days.size();i++){
			existRequest = days.get(i).userGotPreviousRequest(phone);
			if(existRequest){
				break;
			}
		}
		try {
			SqlSession session = new SimpleMyBatisSesFactory()
				.getOpenSqlSesion();
			if(session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone).size() > 0){
				existRequest = true;
			}
			session.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return existRequest;
	}
	
	public synchronized List<ProvisionalAppointment> 
	getAppointmentToConfirm(String phone_number,int itemsRequest) 
			throws Exception{
		Date last_day = nextDay(new Date()); // Por defecto el día siguiente
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		// Exclusión mútua
		while(inUse){
			wait();
		}
		inUse = true;
		appintment_list.addAll(
				findAppointmentInServicesDays(itemsRequest,phone_number,last_day));
		if(itemsRequest > 0){
			appintment_list.addAll(
					createNewServiceDaysForAppointment(itemsRequest,
							phone_number,last_day));
		}
		// Fin de la exclusión mútua
		inUse = false;
		notifyAll();
		return appintment_list;
	}
	
	/**
	 * Se crean nuevos días de servicio para el usuario con el número de tléfono indicado
	 * y se devuelve un listado de solicitudes pendientes de confirmar, a partir del 
	 * último día en el que se solítico servicio de recogida.
	 * @param itemsRequest
	 * @param phone_number
	 * @param last_day
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> createNewServiceDaysForAppointment(
			int itemsRequest, String phone_number, Date last_day) throws Exception {
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		while(itemsRequest > 0){
			DailyServices day = new DailyServicesImp(last_day);

			int furnitureRealizables = 
					day.obtainRealizablePeticions() - itemsRequest;
			if(furnitureRealizables > 0){
				appintment_list.add(day.getAppointment(phone_number,
						furnitureRealizables));
				itemsRequest -= furnitureRealizables;
			}
			days.add(day);
		}
		return appintment_list;
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
	 * Devuelve una lista de solicitudes pendientes de confirmar para el usuario
	 * con el tléfono indicado, y que responde a los items solicitados.
	 * @param itemsRequest
	 * @param phone_number
	 * @param last_service_day
	 * @return
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> findAppointmentInServicesDays(int itemsRequest,
			String phone_number,Date last_service_day) throws Exception{
		List<ProvisionalAppointment> appintment_list = 
				new ArrayList<ProvisionalAppointment>();
		for(int i = 0;i < days.size() && itemsRequest >= 0;i++){
			if(days.get(i).obtainRealizablePeticions() > 0){
				last_service_day = days.get(i).getDay();
				int furnitureRealizables = 
						days.get(i).obtainRealizablePeticions() - itemsRequest;
				if(furnitureRealizables > 0){
					appintment_list.add(days.get(i).getAppointment(phone_number,
							furnitureRealizables));
					itemsRequest -= furnitureRealizables;
				}
			}
		}
		return appintment_list;
	}
}
