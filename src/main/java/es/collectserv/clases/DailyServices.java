package es.collectserv.clases;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;

/**
 * Esta clase representa el servicio de recogida diario. Dentro del servicio diario
 * se establecen un máximo muebles que peuden ser recogidos al día, y un límite
 * de enseres solicitados por usuarios. 
 * @author Diego Rubio Abujas
 * @version 1.0
 */
public class DailyServices {
	private Date day;
	private static final int MAX_FUNRITNURES_PER_DAY = 24;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private List<ProvisionalAppointment> requestToConfirmation;
	private int furniteres_per_day;
	
	DailyServices(Date day) throws Exception{
		if(day.after(new Date())){
			throw new Exception("invalid day, it must "
					+ "be later than the current day");
		}
		requestToConfirmation = new ArrayList<ProvisionalAppointment>();
		this.day = day;
		SqlSession session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		furniteres_per_day = 
				session.selectOne("CollectionResquestMapper"
					+".selectFurnituresByDay");
		if(furniteres_per_day > MAX_FUNRITNURES_PER_DAY){
			throw new Exception("Invalid number of furnirutre request for this day");
		}
	}	
	
	/**
	 * Obtiene el número de enseres que el servicio puere recoger para un día especifico.
	 * @return int número de enseres que se pueden solicitar dicho día en función
	 * de las peticiones previamente realizadas
	 */
	public int obtainRealizablePeticions(){
		if((MAX_FUNRITNURES_PER_DAY - furniteres_per_day) != 0){
			if((MAX_FUNRITNURES_PER_DAY - furniteres_per_day) > 
			MAX_FURNIUTRES_PER_DAY_USER){
				return MAX_FURNIUTRES_PER_DAY_USER;
			}
			else{
				return (MAX_FUNRITNURES_PER_DAY - furniteres_per_day);
			}
	
		}
		else{
			return 0;
		}
	} 
	
	/**
	 * Comprueba si el usuario con dicho número de teléfono tiene una solicitud previa
	 * @param phone
	 * @return
	 */
	public boolean userGotPreviousRequest(String phone){
		boolean exists = false;
		for(int i = 0;i < requestToConfirmation.size();i++){
			if(requestToConfirmation.get(i).getTelephone().equals(phone)){
				exists = true;
				break;
			}
		}
		return exists;
	}
}
