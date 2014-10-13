package es.collectserv.test.services;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.clases.CollectionPoint;


@RunWith(JUnit4.class)
public class TestCollectionPoint {
	HttpHost target;
	DefaultHttpClient httpclient;
	
	public TestCollectionPoint(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost("localhost", 8080, "http");			
	}
	
	@Test
	public void testGETPoitByValidLoc(){
		try {
			CollectionPoint point =
					obtainsCollectionPoint(36.536234,-6.193096,false);
			assertNotNull(point);
			assertTrue(point.getLongitude() == -6.193095 
					&& point.getLatitude() == 36.536233);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	private CollectionPoint obtainsCollectionPoint(double lat,
			double lng,boolean b) throws Exception{
		HttpGet getRequest = 
				new HttpGet("/FurnitureCollectionService/resources/point"
						+"?lat="+lat
						+"&lng="+lng
						+"&isRuralArea="+b);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				httpclient.execute(target, getRequest);
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());
		}
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		JSONObject respJSON = new JSONObject(respStr);
		CollectionPoint point = JSONtoCollectionPoint(respJSON);
		return point;
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
