package es.collectserv.conector;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.json.JSONConverter;
import es.collectserv.model.CollectionPoint;

public class CollectionPointServiceConectorImp 
	implements CollectionPointServiceConector{
	private HttpHost mTarget;
	private DefaultHttpClient mHttpclient;
	
	public CollectionPointServiceConectorImp(){
		mHttpclient = new DefaultHttpClient();
		mTarget = new HttpHost("66.85.153.171", 8080, "http");	
	}
	
	/**
	 * 
	 * @param lng
	 * @param lat
	 * @param isRuralArea
	 * @return nearest collection point to the request.
	 * @throws URISyntaxException
	 * @throws JSONException
	 * @throws HttpException
	 * @throws IOException
	 */	
	public CollectionPoint getNearestCollectionPoint(final Double lng,
			final Double lat,final boolean isRuralArea) 
					throws URISyntaxException, JSONException, 
					HttpException, IOException{
		HttpGet getRequest =  
				new HttpGet("/FurnitureCollectionService/resources/point"
						+"?lat="+lat
						+"&lng="+lng
						+"&isRuralArea="+isRuralArea);
		getRequest.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse httpResponse = 
				mHttpclient.execute(mTarget, getRequest);
		String respStr = EntityUtils.toString(httpResponse.getEntity());
		if(httpResponse.getStatusLine().getStatusCode() != 200){
			throw new RuntimeException("Failed : HTTP error code : "
					 + httpResponse.getStatusLine().getStatusCode());	
		}
		JSONObject respJSON = new JSONObject(respStr);
		return JSONConverter.JSONtoCollectionPoint(respJSON);
	}

	
}
