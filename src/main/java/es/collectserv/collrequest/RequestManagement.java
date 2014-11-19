package es.collectserv.collrequest;

import java.util.List;

import javax.naming.ServiceUnavailableException;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

/**
 * Dicha clase representa un gestor de solicitudes que se encargará de proporcionar
 * las fechas de recogida de enseres, y registrar dichas en solicitudes una vez hayan
 * sido confirmadas por el usuario en la base de datos. También se encargará de 
 * comprobar si el usuario tiene anteriores peticiones en curso.
 * 
 * @author Diego Rubio Abujas
 * @version 1.0
 */
public interface RequestManagement {
	
	/**
	 * Comprueba si el usuario con dicho teléfono tiene alguna solicitud en curso
	 * o alguna solicitud pendiente de confirmar
	 * @param phone numero de teléfono del usuario
	 * @return true si el usuario tiene una solicitud previa a la fecha actual, false en 
	 * otro caso.
	 * @throws InterruptedException 
	*/
	public boolean userGotPreviosRequest(String phone) throws InterruptedException;
	
	/**
	 * Se reserva uno o varios dias para la petición del usuario. Esta petición
	 *  debe confirmarse posteriormente. Para evitar inconsistencias dicha función
	 *  se realizá con e.m a los recursos de servicio diario.
	 * @param phone_number número de teléfono correspondiente a un usuario
	 * @param itemsRequest número de enseres que desea depositar.
	 * @param collectionPointId identificador del punto de recogida
	 * @return List<ProvisionalAppointment>  listado de citas provisionales pendientes
	 * de confirmar.
	 * @throws Exception 
	*/
	public List<ProvisionalAppointment> 
		getAppointmentToConfirm(String phone_number,int itemsRequest,int collectionPointId) 
		throws Exception;
	
	/**
	 * Se confirma una cita pendiente de confirmar y se registra en la base de datos.
	 * Una vez registrada es eliminada del listado de citas pendientes de confirmar.
	 * @param collectionRequest
	 * @throws ServiceUnavailableException 
	 */
	public void confirmProvisionalAppointment(CollectionRequest collectionRequest) 
			throws ServiceUnavailableException;
	
}
