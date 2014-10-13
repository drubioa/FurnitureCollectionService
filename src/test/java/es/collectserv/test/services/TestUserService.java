package es.collectserv.test.services;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.clases.User;


@RunWith(JUnit4.class)
public class TestUserService {
	HttpHost target;
	DefaultHttpClient httpclient;
	
	public TestUserService(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost("66.85.153.171", 8080, "http");	
	}
	
	/**
	 * Test POST new user and checks if http service responds 201's message.
	 */
	@Test
	public void testPOSTUser(){
		try{
			final User user = new User("UsuarioPrueba","600000001"); 
			int statusCode = addNewUser(user).getStatusLine().getStatusCode(); 
			assertTrue(statusCode == 201);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	
	
	/**
	 * Test POST new user and checks if http service responds 201's message. 
	 * Later, GET this User by phone number and checks if this corresponds 
	 * with that.
	 */
	@Test
	public void testPOSTAndGETUser(){
		try{
			final String phone_number = "6149731246";
			final User user = new User("Peter",phone_number);
			HttpResponse response = addNewUser(user);
			if (response.getStatusLine().getStatusCode() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				 + response.getStatusLine().getStatusCode());
			}
			assertTrue(response.getStatusLine().getStatusCode() == 201);
			//Get Object
			User userObtainsByGET = getUserByPhone(phone_number);
			assertNotNull(userObtainsByGET);
			assertTrue(phone_number.equals(userObtainsByGET.getPhone_number()));
		}
		catch(Exception e){
			fail(e.toString());
		}	
	}
	
	
	/**
	 * Test GET invalid phone number, and check if the server corresponds 
	 * with NOT FOUND status code.
	 */
	@Test
	public void testGETnotExistPhone(){
		try{
			final String nobodiesPhoneNumber = "9021233456";
			getUserByPhone(nobodiesPhoneNumber); 
		} 
		catch( RuntimeException e){
			assertTrue(e.getMessage().equals("Failed : HTTP error code : 406"));
		}
		catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	private HttpResponse addNewUser(User user) throws Exception{
		HttpPost postRestues = new
				HttpPost("/FurnitureCollectionService/resources/users");
		postRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		JSONObject dato = userToJSON(user);
		StringEntity entity = new StringEntity(dato.toString());
		entity.setContentType(MediaType.APPLICATION_JSON);
		postRestues.setEntity(entity);
		HttpResponse resp = httpclient.execute(target, postRestues);
		if (resp.getStatusLine().getStatusCode() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		return resp;
	}
	
	/**
	 * 
	 * @param phoneNumber
	 * @return User obtains by GET request
	 * @throws Exception
	 */
	private User getUserByPhone(String phoneNumber) throws Exception{
		HttpGet del = new
				HttpGet("/FurnitureCollectionService/resources/users"
		+"/"+phoneNumber);
		del.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp = httpclient.execute(target,del);
		if(resp.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + resp.getStatusLine().getStatusCode());
		}
		String respStr = EntityUtils.toString(resp.getEntity());
		JSONObject respJSON = new JSONObject(respStr);
		User user = new User();
		user.setName(respJSON.getString("name"));
		user.setPhone_number(respJSON.getString("phone_number"));
		return user;
	}
	
	private JSONObject userToJSON(User user) throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user.getName());
		dato.put("phone_number", user.getPhone_number());
		return dato;
	}
}
