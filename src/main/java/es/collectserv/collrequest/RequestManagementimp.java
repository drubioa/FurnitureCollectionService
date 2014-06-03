package es.collectserv.collrequest;

import java.io.IOException;
import java.util.ArrayList;
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
		List<ProvisionalAppointment> appintment_list = new ArrayList<ProvisionalAppointment>();
		// Exclusión mútua en el tratamiento de solicitudes
		while(inUse){
			wait();
		}
		inUse = true;
		for(int i = 0;i < days.size() && itemsRequest >= 0;i++){
			if(days.get(i).obtainRealizablePeticions() > 0){
				int furnitureRealizables = 
						days.get(i).obtainRealizablePeticions() - itemsRequest;
				if(furnitureRealizables > 0){
					appintment_list.add(days.get(i).getAppointment(phone_number,
							furnitureRealizables));
					itemsRequest -= furnitureRealizables;
				}
			}
		}
		inUse = false;
		notifyAll();
		return appintment_list;
	}
	
}
