package es.collectserv.conector;

import org.apache.http.HttpResponse;

import es.collectserv.model.User;

/**
 * @version 1.0 
 * @author Diego Rubio Abujas
 *
 */
public interface UserServiceConector {
	
	/**
	 * Conectar al servidor y solicita que se cree un nuevo usuario.
	 * @param user
	 * @return respuesta del servidor
	 * @throws Exception 
	 */
	public HttpResponse addUser(User user) throws Exception;
	
	/**
	 * 
	 * @param user
	 * @return respuesta del servidor
	 * @throws Exception
	 */
	public HttpResponse deleteUser(User user) throws Exception;
	
	
	/**
	 * 
	 * @param phoneNumber
	 * @return True if user got pending collection request. false in other case.
	 * @throws Exception
	 */
	public Boolean checkIfUserGotPendingReq(String phoneNumber) 
			throws Exception;
}
