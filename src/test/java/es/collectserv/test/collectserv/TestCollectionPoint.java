package es.collectserv.test.collectserv;

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

import es.collectserv.model.CollectionPoint;


@RunWith(JUnit4.class)
public class TestCollectionPoint {
	HttpHost target;
	DefaultHttpClient httpclient;
	
	public TestCollectionPoint(){
		httpclient = new DefaultHttpClient();
		target = new HttpHost("localhost", 8080, "http");			
	}
	
	/**
	 * Test if a valid request to find nearest collection 
	 * point in urban area returns successful result.
	 */
	@Test
	public void testGETPoitByValidUrbanLocation(){
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
	
	/**
	 * Try to get point by invalid location. This test must fail
	 */
	@Test
	public void testGETPoitByInvalidUrbanLocation(){
		try {
			// lng lat 0,0 is a invalid position.
			obtainsCollectionPoint(0,0,false);
			fail("System return CollectionPoint, and it must throw Exception");
		} catch (Exception e) {
			if(e.getLocalizedMessage()
					.contains("Failed : HTTP error code : 400")){
				assertTrue(true);
			}
			else{
				e.printStackTrace();
				fail(e.toString());
			}
		}
	}
	
	/**
	 * @param lat lattitude
	 * @param lng longite
	 * @param isRuralArea if is RuralArea or UrbanArea
	 * @return Nearest collection point for point
	 * @throws Exception cannot find nearest collection point.
	 */
	private CollectionPoint obtainsCollectionPoint(double lat,
			double lng,boolean isRuralArea) throws Exception{
		HttpGet getRequest = 
				new HttpGet("/FurnitureCollectionService/resources/point"
						+"?lat="+lat
						+"&lng="+lng
						+"&isRuralArea="+isRuralArea);
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
