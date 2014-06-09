package es.collectserv.test;

import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.clases.CollectionPoint;

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
							+"?phone_number="+"699390215"
							+"&num_funritures="+5);
			getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
			HttpResponse httpResponse = 
					httpclient.execute(target, getRequest);
			String respStr = EntityUtils.toString(httpResponse.getEntity());
			System.out.println(respStr);
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				throw new RuntimeException("Failed : HTTP error code : "
						 + httpResponse.getStatusLine().getStatusCode());	
			}
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	private CollectionPoint JSONtoCollectionPoint(JSONObject object) 
			throws JSONException{
		CollectionPoint point = new CollectionPoint();
		point.setLatitude(object.getDouble("latitude"));
		point.setLongitude(object.getDouble("longitude"));
		point.setZone(object.getInt("zone"));
		return point;
		
	}
	
}
