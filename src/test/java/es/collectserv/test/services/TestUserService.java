package es.collectserv.test.services;

import static org.junit.Assert.*;

import org.apache.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.conector.UserServiceConector;
import es.collectserv.conector.UserServiceConectorImp;
import es.collectserv.model.User;


@RunWith(JUnit4.class)
public class TestUserService {
	private UserServiceConector conector;
	private final String HOST = "108.170.29.138";
	
	@Before
	public void setUp(){
		conector = new UserServiceConectorImp(HOST,8080,"http");
	}
	
	/**
	 * Test POST new user and checks if http service responds 201's message. 
	 * Later, GET this User by phone number and checks if this corresponds 
	 * with that. Finally DELETE user and check if the server responds 200's message.
	 */
	@Test
	public void testPOSTAndDeleteUser(){
		final String name = "Diego";
		final String phone = "631933433";
		final User user = new User(name,phone);
		try{
			// Add User
			HttpResponse response = conector.addUser(user);
			assertTrue(response.getStatusLine().getStatusCode() == 201);
		}
		catch(Exception e){
			fail(e.toString());
		}
		finally{
			try {
				// Delete User
				HttpResponse response = conector.deleteUser(user);
				assertTrue(response.getStatusLine().getStatusCode() == 200);
			} catch (Exception e) {
				fail(e.toString());
			}
		}
	}
	
	@Test
	public void testPOST2equalUsers(){
		final User user = new User("Paco","605301031");
		try{
			// Add User
			HttpResponse response = conector.addUser(user);
			assertTrue(response.getStatusLine().getStatusCode() == 201);
			// Add User Again
			response = conector.addUser(user);	
		}
		catch(Exception e){
			assertTrue(e.getMessage().equals("Failed : HTTP error code : 400"));
		}
		
		HttpResponse response;
		try {
			response = conector.deleteUser(user);
			assertTrue(response.getStatusLine().getStatusCode() == 200);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}
	
	/**
	 * Test GET invalid phone number, and check if the server corresponds 
	 * with NOT FOUND status code.
	 */
	@Test
	public void testGETPhoneNoHaveReq(){
		try{
			final String nobodiesPhoneNumber = "900010101";
			assertFalse(conector.checkIfUserGotPendingReq(nobodiesPhoneNumber)); 
		}
		catch (Exception e) {
			fail(e.toString());
		}
	}
}
