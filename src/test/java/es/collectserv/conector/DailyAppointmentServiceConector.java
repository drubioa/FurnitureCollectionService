package es.collectserv.conector;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.codehaus.jettison.json.JSONException;

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
	 * Obtiene todas las solicitudes de recogida pendientes de realizar.
	 * @param phone
	 * @return
	 * @throws HttpException 
	 * @throws IOException 
	 * @throws org.apache.http.ParseException 
	 * @throws ParseException 
	 * @throws JSONException 
	 * @throws URISyntaxException 
	 */
	public List<CollectionRequest> getPendingCollectionRequest(String phone) 
			throws URISyntaxException, JSONException, ParseException, org.apache.http.ParseException, 
			IOException, HttpException;
	
	/**
	 * 
	 * @param appointment
	 * @return HttpResponse with OK if sucess result.
	 * @throws Exception
	 */
	public HttpResponse confirmAppointment(CollectionRequest appointment) 
	throws Exception;
	
	/**
	 * 
	 * @param phone_number
	 * @return
	 * @throws URISyntaxException 
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public HttpResponse deletePendingAppointments(String phone_number) throws URISyntaxException, HttpException, IOException;
}
