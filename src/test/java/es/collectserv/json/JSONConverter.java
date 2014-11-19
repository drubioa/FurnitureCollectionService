package es.collectserv.json;

import java.util.List;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.model.CollectionPoint;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;

/**
 * Clase con metodos para adaptar objetos a formato JSON destinado a las diversas
 * conexiones con el sercio que gestiona las solicitudes de recogida de enseres.
 * @author Diego Rubio Abujas (dbiosag@gmail.com)
 *
 */
public interface JSONConverter {
	
	/**
	 * 
	 * @param user
	 * @return JSON correspondiente al usuario
	 * @throws JSONException
	 */
	public JSONObject userToJSON(User user) throws JSONException;
	
	/**
	 * 
	 * @param respJSON
	 * @return
	 * @throws JSONException
	 */
	public CollectionPoint JSONtoCollectionPoint(JSONObject respJSON) 
			throws JSONException;
	
	/**
	 * 
	 * @param appointment
	 * @return
	 * @throws JSONException
	 */
	public JSONObject CollectionRequestToJSON(CollectionRequest appointment) 
			throws JSONException;
	
	
	/**
	    * Convierte un JSON a un objeto de tipo ProvisionalAppointment.
		* @param object 
		* @return ProvisionalAppointment
		* @throws Exception si se produce error en el parser, jsonexception o el numero 
		* de muebles no es valido.
		*/
	public List<ProvisionalAppointment> JSONtoProvisionalAppointment(JSONObject arg) 
			throws Exception;
}
