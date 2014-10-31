package es.collectserv.test.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.model.ProvisionalAppointment;

@RunWith(JUnit4.class)
public class TestDailyAppointmentService {
	static final int SLEEP_TIME = 5000; 
	HttpHost target;
	DefaultHttpClient httpclient;
	
	public TestDailyAppointmentService(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost("localhost", 8080, "http");			
	}

	@After 
	public void tearDown(){
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a provisional appointment.
	 */
	@Test
	public void testGetProvisionalAppointments(){
		try{
			validAppointment(getProvisionalAppointments("604300014",1,1).get(0));
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	/**
	 * Get a provisional appointment for 2 days.
	 */
	@Test
	public void testGet2ProvisionalAppointments(){
		try{
			List<ProvisionalAppointment> appointments =
					getProvisionalAppointments("604300014",5,1);
			assertTrue(appointments.size() > 1);
			for(ProvisionalAppointment appointment : appointments){
				validAppointment(appointment);
			}
			
		}
		catch(Exception e){
			fail(e.toString());
		}
	}	
	
	/**
	 * 
	 * @param phone
	 * @param num_furnitures
	 * @param collection_point_id
	 * @return ProvisionalAppointment for collection furnitures.
	 * @throws Exception
	 */
	private List<ProvisionalAppointment> getProvisionalAppointments(
			String phone,int num_furnitures,int collection_point_id) 
					throws Exception{
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/appointment"
						+"?phone_number="+phone
						+"&num_funritures="+num_furnitures
						+"&collection_point_id="+collection_point_id);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				httpclient.execute(target, getRequest);
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		JSONObject respJSON = new JSONObject(respStr);
		return JSONtoCollectionPoint(respJSON);
	}
	
	
	/**
	 * Validate if the appointment is correct, and all these fields are in correct format.
	 * @param apointmnet
	 */
	private void validAppointment(ProvisionalAppointment apointmnet){
		assertNotNull(apointmnet.getTelephone());
		assertTrue(apointmnet.getTelephone().charAt(0) == '6');
		assertTrue(apointmnet.getNumFurnitures() > 0);
		assertNotNull(apointmnet.getFch_collection());
		assertNotNull(apointmnet.getFch_request());
		assertNotNull(apointmnet.getCollectionPointId());
	}
	
   /**
    * Convierte un JSON a un objeto de tipo ProvisionalAppointment.
	* @param object 
	* @return ProvisionalAppointment
	* @throws Exception si se produce error en el parser, jsonexception o el numero 
	* de muebles no es valido.
	*/
	private List<ProvisionalAppointment> JSONtoCollectionPoint(JSONObject arg) 
			throws Exception{
		List<ProvisionalAppointment> appointments 
			= new ArrayList<ProvisionalAppointment>();
		JSONArray objects  = arg.getJSONArray("provisionalAppointment");
		for(int i = 0; i < objects.length();i++){
			JSONObject object = objects.getJSONObject(i);
	        ProvisionalAppointment appointment = new ProvisionalAppointment();
	        appointment.setCollectionPointId(object.getInt("collectionPointId"));
	        appointment.setFch_collection
	        			(convStringToDate(object.getString("fch_collection")));
	        appointment.setFch_request(convStringToDate(object.getString("fch_request")));
	        appointment.setNumFurnitures(object.getInt("numFurnitures"));
	        appointment.setTelephone(object.getString("telephone"));
	        appointments.add(appointment);
		}
		return appointments;
	}
	
	private Date convStringToDate(String inputDate) throws ParseException{
		// Format: 2001-07-04T12:08:56.235-07:00
		Date date 
		  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"	).parse(inputDate);
	    return date;
	}
	
}
