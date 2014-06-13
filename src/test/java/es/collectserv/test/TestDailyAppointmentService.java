package es.collectserv.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.ProvisionalAppointment;

@RunWith(JUnit4.class)
public class TestDailyAppointmentService {
	HttpHost target;
	DefaultHttpClient httpclient;
	
	public TestDailyAppointmentService(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost("localhost", 8080, "http");			
	}

	@Test
	public void testGetProvisionalAppintments(){
		try{
			HttpGet getRequest = 
					new HttpGet("/FurnitureCollectionService/resources/appointment"
							+"?phone_number="+"604300021"
							+"&num_funritures="+1
							+"&collection_point_id="+1);
			getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
			HttpResponse httpResponse = 
					httpclient.execute(target, getRequest);
			String respStr = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(respStr);
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				throw new RuntimeException("Failed : HTTP error code : "
						 + httpResponse.getStatusLine().getStatusCode());	
			}
			JSONObject respJSON = new JSONObject(respStr);
			ProvisionalAppointment appointment = JSONtoCollectionPoint(respJSON);
			validAppointment(appointment);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	
	
	private void validAppointment(ProvisionalAppointment apointmnet){
		assertNotNull(apointmnet.getTelephone());
		assertTrue(apointmnet.getTelephone().charAt(0) == '6');
		assertTrue(apointmnet.getNumFurnitures() > 0);
		assertNotNull(apointmnet.getFch_collection());
		assertNotNull(apointmnet.getFch_request());
		assertNotNull(apointmnet.getCollectionPointId());
	}
	
	private ProvisionalAppointment JSONtoCollectionPoint(JSONObject object) 
			throws Exception{
		ProvisionalAppointment appointment = new ProvisionalAppointment();
		appointment.setCollectionPointId(object.getInt("collectionPointId"));
		appointment.setFch_collection(convStringToDate(object.getString("fch_collection")));
		appointment.setFch_request(convStringToDate(object.getString("fch_request")));
		appointment.setNumFurnitures(object.getInt("numFurnitures"));
		appointment.setTelephone(object.getString("telephone"));
		return appointment;
		
	}
	
	private Date convStringToDate(String date) throws ParseException{
		SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateInstance();
		return formatter.parse(date);
	}
	
}
