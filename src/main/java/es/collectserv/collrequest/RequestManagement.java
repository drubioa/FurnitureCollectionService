package es.collectserv.collrequest;

import java.util.List;

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
	*/
	public boolean userGotPreviosRequest(String phone);
	
	/**
	 * Se reserva uno o varios dias para la petición del usuario. Esta petición
	 *  debe confirmarse posteriormente. Para evitar inconsistencias dicha función
	 *  se realizá con e.m a los recursos de servicio diario.
	 * @param phone_number número de teléfono correspondiente a un usuario
	 * @param itemsRequest número de enseres que desea depositar.
	 * @return List<ProvisionalAppointment>  listado de citas provisionales pendientes
	 * de confirmar.
	 * @throws Exception 
	*/
	public List<ProvisionalAppointment> 
		getAppointmentToConfirm(String phone_number,int itemsRequest) 
		throws Exception;
}
