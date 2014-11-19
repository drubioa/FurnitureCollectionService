package es.collectserv.conector;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.json.JSONConverter;
import es.collectserv.json.JSONConverterImp;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

public class DailyAppointmentServiceConectorImp 
	implements DailyAppointmentServiceConector{
	private HttpHost mTarget;
	private DefaultHttpClient mHttpclient;
	private JSONConverter mConverter;
	
	public DailyAppointmentServiceConectorImp(String host,int port,String scheme){
		mHttpclient = new DefaultHttpClient();
		mTarget = new HttpHost(host, port, scheme);	
		mConverter = new JSONConverterImp();
	}
		
	public List<ProvisionalAppointment> getProvisionalAppointments(
	String phone,int num_furnitures,int collection_point_id) 
	throws Exception{
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
		return mConverter.JSONtoProvisionalAppointment(respJSON);
	}
	
	public HttpResponse confirmAppointment(CollectionRequest appointment) 
	throws Exception{
		assertNotNull(appointment);
		HttpPost post  = new
				HttpPost("/FurnitureCollectionService/resources/appointment");
		post.setHeader("content-type", MediaType.APPLICATION_JSON);
		JSONObject dato = mConverter.CollectionRequestToJSON(appointment);
		assertNotNull(dato);
		StringEntity entity = new StringEntity(dato.toString());
		post.setEntity(entity);
		HttpResponse resp = mHttpclient.execute(mTarget, post);
		assertNotNull(resp);
		if (resp.getStatusLine().getStatusCode() != 201) {					
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}
}
