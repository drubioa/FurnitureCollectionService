package es.collectserv.collrequest;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.List;

import javax.naming.ServiceUnavailableException;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

/**
 * Clase Singleton para el gestor de solicitudes de recogida de muebles y enseres.
 * @author Diego Rubio Abujas (dbiosag@gmail.com)
 *
 */
public interface RequestManagementSingleton {
	
	/**
	 * Comprueba si el usuario tiene una solicitud previa pendiente con una fecha posterior
	 * a la fecha del sistema.
	 * @param phone
	 * @return true si tiene una solicitud previa pendiente de confirmar o confirmada.
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public boolean userGotPreviosRequest(String phone) 
			throws InterruptedException, IOException;
	
	/**
	 * Dado un punto de recogida, un telefono sin solicitudes previas en curso y un numero de muebles
	 * se solicita una o varias solicitudes de recogida para confirmarlas posteriormente. 
	 * @param phone_number
	 * @param itemsRequest
	 * @param collectionPointId
	 * @return un listado de solicitudes pendientes de confirmar.
	 * @throws Exception
	 */
	public  List<ProvisionalAppointment> 
	getAppointmentToConfirm(String phone_number,int itemsRequest,int collectionPointId) 
			throws Exception;
	
	/**
	 * Se confirma una solicitud de recogida.
	 * @param request
	 * @throws ServiceUnavailableException
	 * @throws IOException 
	 * @throws NotBoundException 
	 * @throws InterruptedException 
	 */
	public void confirmProvisionalAppointment(
			CollectionRequest request) throws ServiceUnavailableException, IOException, NotBoundException, InterruptedException;
	
	/**
	 * Se cancela una solicitud de recogida pendientes de llevarse a cabe, es decir
	 * con una fecha posterior a la actual.
	 * @param phone
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void cancelPeendingRequest(String phone) 
			throws InterruptedException, IOException;
}
