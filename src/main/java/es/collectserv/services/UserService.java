package es.collectserv.services;

import javax.ws.rs.core.Response;

import es.collectserv.model.User;

/**
 * 
 * @author Diego Rubio Abujas
 *
 */
public interface UserService {
	
	/**
	 * Add new user number to db
	 * @param user
	 * @return return URI, if the user exists or a error occur return a error 500.
	 */
	public Response createNewUser(User user);
	
	/**
	 * Check if the user got pending collection request in the system.
	 * @return OK or NOT_FOUND
	 */
	public Response userGotPrendingRequest(String phone_number);
	
	/**
	 * Remove a current user to db.
	 * @return 
	 */
	public Response deleteUser(String phone_number);
}
