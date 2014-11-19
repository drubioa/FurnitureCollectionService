package es.collectserv.conector;

import javax.ws.rs.core.MediaType;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.json.JSONConverterImp;
import es.collectserv.model.User;

public class UserServiceConectorImp implements UserServiceConector{
	private static HttpHost mTarget;
	private static DefaultHttpClient mHttpclient;
	private JSONConverterImp mConverter;
	
	public UserServiceConectorImp(String host,int port,String scheme){
		mHttpclient = new DefaultHttpClient();
		mTarget = new HttpHost(host, port, scheme);	
		mConverter = new JSONConverterImp();
	}

	public HttpResponse addUser(User user) throws Exception{
		HttpPost post  = new
				HttpPost("/FurnitureCollectionService/resources/users");
		post.setHeader("content-type", MediaType.APPLICATION_JSON);
		JSONObject dato = mConverter.userToJSON(user);
		StringEntity entity = new StringEntity(dato.toString());
		post.setEntity(entity);
		HttpResponse resp = mHttpclient.execute(mTarget, post);
		resp.getEntity().consumeContent();
		if (resp.getStatusLine().getStatusCode() != 201) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		return resp;
	}
	
	
	public HttpResponse deleteUser(User user) throws Exception{
		HttpDelete deleteRestues = new
				HttpDelete("/FurnitureCollectionService/resources/users"
						+"/"+user.getPhone_number());
		deleteRestues.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp = mHttpclient.execute(mTarget, deleteRestues);
		if (resp.getStatusLine().getStatusCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			 + resp.getStatusLine().getStatusCode());
		}
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
	    }
		return resp;
	}

	public Boolean checkIfUserGotPendingReq(String phoneNumber) 
			throws Exception{
		HttpGet del = new
				HttpGet("/FurnitureCollectionService/resources/users"
		+"/"+phoneNumber);
		del.setHeader("content-type", MediaType.APPLICATION_JSON);
		HttpResponse resp;
			resp = mHttpclient.execute(mTarget,del);
		if( resp.getEntity() != null ) {
			resp.getEntity().consumeContent();
		}
		if(resp.getStatusLine().getStatusCode() == 200){
			return true;
		}
		else if(resp.getStatusLine().getStatusCode() == 404){
			return false;
		}
		else{
			throw new Exception("Server error");
		}
	}

}
