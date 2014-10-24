package es.collectserv.services;

import javax.ws.rs.core.Response;

import es.collectserv.clases.User;

/**
 * 
 * @author Diego Rubio Abujas
 *
 */
public interface UserService {
	
	/**
	 * Add new user numbe to db
	 * @param user
	 * @return return URI, if the user exists or a error occur return a error 500.
	 */
	public Response createNewUser(User user);
	
	/**
	 * Get user name and number by phone, or indicate if the user doesn't exists.
	 * @param phone_number
	 * @return
	 */
	public User getUserByPhoneNumber(String phone_number);
}
