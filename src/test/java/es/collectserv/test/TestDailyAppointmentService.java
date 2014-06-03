package es.collectserv.test;

import static org.junit.Assert.fail;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

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
							+"&num_funritures="+1);
			getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
			HttpResponse httpResponse = 
					httpclient.execute(target, getRequest);
			if(httpResponse.getStatusLine().getStatusCode() != 200){
				throw new RuntimeException("Failed : HTTP error code : "
						 + httpResponse.getStatusLine().getStatusCode());	
			}
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
}
