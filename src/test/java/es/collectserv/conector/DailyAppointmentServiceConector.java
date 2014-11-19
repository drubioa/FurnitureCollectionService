package es.collectserv.conector;

import java.util.List;

import org.apache.http.HttpResponse;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

/**
 * 
 * @author Diego Rubio Abujas (dbiosag@gmail.com)
 *
 */
public interface DailyAppointmentServiceConector {

	/**
	 * 
	 * @param phone
	 * @param num_furnitures
	 * @param collection_point_id
	 * @return ProvisionalAppointment for collection furnitures.
	 * @throws Exception
	 */
	public List<ProvisionalAppointment> getProvisionalAppointments(String phone,
			int num_furnitures,int collection_point_id) throws Exception;
	
	/**
	 * 
	 * @param appointment
	 * @return HttpResponse with OK if sucess result.
	 * @throws Exception
	 */
	public HttpResponse confirmAppointment(CollectionRequest appointment) 
	throws Exception;
}
