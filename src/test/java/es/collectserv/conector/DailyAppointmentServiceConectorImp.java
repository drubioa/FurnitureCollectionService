package es.collectserv.conector;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.json.JSONConverter;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

public class DailyAppointmentServiceConectorImp 
	implements DailyAppointmentServiceConector{
	private HttpHost mTarget;
	private DefaultHttpClient mHttpclient;
	
	public DailyAppointmentServiceConectorImp(String host,int port,String scheme){
		mHttpclient = new DefaultHttpClient();
		mTarget = new HttpHost(host, port, scheme);	
	}
	
	public List<ProvisionalAppointment> getProvisionalAppointments(
	String phone,int num_furnitures,int collection_point_id) 
		throws URISyntaxException, HttpException, IOException, JSONException, 
			ParseException{
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/appointment"
						+"?phone_number="+phone
						+"&num_funritures="+num_furnitures
						+"&collection_point_id="+collection_point_id);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				mHttpclient.execute(mTarget, getRequest);
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		JSONObject respJSON = new JSONObject(respStr);
		if( httpResponse.getEntity() != null ) {
			httpResponse.getEntity().consumeContent();
	    }
		return JSONConverter.JSONtoProvisionalAppointment(respJSON);
	}
	
	public HttpResponse confirmAppointment(CollectionRequest appointment) 
			throws URISyntaxException, HttpException, IOException, JSONException{
		assertNotNull(appointment);
		HttpPost post  = new
				HttpPost("/FurnitureCollectionService/resources/appointment");
		post.setHeader("content-type", MediaType.APPLICATION_JSON);
		if(!(appointment.getFch_collection()).isAfter(appointment.getFch_request())){
			throw new RuntimeException("Bad dates of appointment");
		}
		JSONObject dato;
		try {
			dato = JSONConverter.CollectionRequestToJSON(appointment);
			assertNotNull(dato);
		} catch (JSONException e) {
			e.printStackTrace();
			throw e;
		}
		StringEntity entity = new StringEntity(dato.toString());
		post.setEntity(entity);
		HttpResponse resp = mHttpclient.execute(mTarget, post);
		assertNotNull(resp);
		if (resp.getStatusLine().getStatusCode() != 200) {					
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}
	
	public List<CollectionRequest> getPendingCollectionRequest(String phone) 
			throws URISyntaxException, JSONException, ParseException, 
			org.apache.http.ParseException, IOException, HttpException{
		if(phone == null){
			throw new NullPointerException("Phone argument is null in getPendingCollectionRequest method");
		}
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/collectionrequests"
						+"?phone_number="+phone);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				mHttpclient.execute(mTarget, getRequest);
		if(httpResponse == null){
			throw new NullPointerException("httpResponse is null");
		}
		if(httpResponse.getStatusLine().getStatusCode() == 204){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		JSONObject respJSON = new JSONObject(respStr);
		if( httpResponse.getEntity() != null ) {
			httpResponse.getEntity().consumeContent();
	    }
		return JSONConverter.JSONtoCollectionRequest(respJSON);
		
	}
	
	public HttpResponse deletePendingAppointments(String phone_number) 
			throws URISyntaxException, HttpException, IOException{

		HttpDelete deleteRestues = new
				HttpDelete("/FurnitureCollectionService/resources/appointment"
						+"?phone_number="+phone_number);
		deleteRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp = mHttpclient.execute(mTarget, deleteRestues);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode()+"\n"+resp.getParams());
		}
		if(resp.getEntity() != null) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}
}
