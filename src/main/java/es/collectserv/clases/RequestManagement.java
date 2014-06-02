package es.collectserv.clases;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;

/**
 * Dicha clase representa un gestor de solicitudes que se encargará de proporcionar
 * las fechas de recogida de enseres, y registrar dichas en solicitudes una vez hayan
 * sido confirmadas por el usuario en la base de datos. También se encargará de 
 * comprobar si el usuario tiene anteriores peticiones en curso.
 * 
 * @author Diego Rubio Abujas
 * @version 1.0
 */
public class RequestManagement {
	private static boolean inUse;
	private static List<DailyServices> days;
	
	public RequestManagement(){
		inUse = false;
		days = new ArrayList<DailyServices>();
		try {
			SqlSession session = new SimpleMyBatisSesFactory()
				.getOpenSqlSesion();
			List<Date> dates = session.selectList(
					"CollectionRequestMapper.selectAllCollectionDates");
			for(int i = 0;i < dates.size();i++){
				days.add(new DailyServices(dates.get(i)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Comprueba si el usuario con dicho teléfono tiene alguna solicitud en curso
	 * o alguna solicitud pendiente de confirmar
	 * @param phone
	 * @return
	 */
	public boolean usertGotPreviosRequest(String phone){
		boolean existRequest = false;
		for(int i = 0;i < days.size();i++){
			existRequest = days.get(i).userGotPreviousRequest(phone);
			if(existRequest){
				break;
			}
		}
		try {
			SqlSession session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
			if(session.selectList("CollectionResquetMapper.selectPendingRequestByPhone",phone).size() > 0){
				existRequest = true;
			}
			session.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return existRequest;
	}
	
	/**
	 * Se reserva uno o varios dias para la petición del usuario. Esta petición
	 *  debe confirmarse posteriormente. Para evitar inconsistencias dicha función
	 *  se realizá con e.m a los recursos de servicio diario.
	 * @param phone_number número de teléfono correspondiente a un usuario
	 * @param itemsRequest número de enseres que desea depositar.
	 * @return List<ProvisionalAppointment>  listado de citas provisionales pendientes
	 * de confirmar.
	 * @throws InterruptedException
	 */
	public synchronized List<ProvisionalAppointment> 
	getAppointmentToConfirm(String phone_number,int itemsRequest) 
			throws InterruptedException{
		int cont = 0;
		while(inUse){
			wait();
		}
		inUse = true;
		for(int i = 0;i < days.size() && cont < itemsRequest;i++){
			if(days.get(i).obtainRealizablePeticions() > 0){
				if(days.get(i).obtainRealizablePeticions() < itemsRequest){
					
				}
				else{
					
				}
			}
		}
		inUse = false;
		notifyAll();
		return null;
	}
	
}