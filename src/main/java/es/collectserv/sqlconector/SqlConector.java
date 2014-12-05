package es.collectserv.sqlconector;

import java.io.IOException;
import java.util.List;

import org.joda.time.LocalDate;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.User;

public interface SqlConector {
	
	/**
	 * Insert a new user in server DB.
	 * @param user
	 * @throws IOException
	 */
	public void addNewUser(User user) throws IOException;
	
	/**
	 * Se comprueba si el usuario tiene alguna solicitud pendiente.
	 * @param phone_number
	 * @return
	 * @throws IOException
	 */
	public Boolean CheckIfUserGotPendingRequest(String phone_number) 
			throws IOException;
	
	/**
	 * Se elimina un usuario del sistema.
	 * @param phone_number
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public void deleteUser(String phone_number) 
			throws IllegalArgumentException, IOException;
	
	/**
	 * El numero de muebles que se han solicitado recoger en una fecha.
	 * @param last_day
	 * @return
	 * @throws IOException
	 */
	public int selectFurnituresByDay(LocalDate last_day) throws IOException;
	
	/**
	 * Se establece una sesion sql para introducir la solicitud de recogida.
	 * @param request 
	 * @throws IOException 
	 */
	public void registerRequestInDB(CollectionRequest request) throws IOException;
	
	/** Se eliminan de la base de datos las peticiones pendientes de realizar y
	 * se actualizan los dias de servicio.
	 * @param phone
	 * @throws IOException 
	 */
	public void removesPendingRequestFromBD(CollectionRequest request) throws IOException;

	/**
	 * Consultar la base de datos para devolver todas las solicitudes pendientes
	 * de realizar que tiene el usuario.
	 * @param phone
	 * @return un listado con todas las peticiones de recogida pendientes de un usuario.
	 * @throws IOException
	 */
	public List<CollectionRequest> getPendingCollectionRequest(String phone) throws IOException;
	
	/**
	 * 
	 * @return todas las fechas en las que hay previstas peticiones de recogida.
	 * @throws IOException
	 */
	public List<LocalDate> selectAllCollectionDays() throws IOException;
}
