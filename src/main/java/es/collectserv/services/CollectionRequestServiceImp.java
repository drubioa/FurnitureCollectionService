package es.collectserv.services;

import java.io.IOException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.collectserv.converter.CollectionRequestConverter;
import es.collectserv.model.CollectionRequest;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;

@Path("collectionrequests")
public class CollectionRequestServiceImp implements CollectionRequestService{
	private static SqlConector conector = new SqlConectorImp();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<CollectionRequestConverter> getCollectionRequest(
			@QueryParam("phone_number") String phone) {
		ArrayList<CollectionRequest> requests = new ArrayList<CollectionRequest>();
		try {
			if(phone == null){
				throw new WebApplicationException(Response.Status.BAD_REQUEST);
			}
			if(!conector.CheckIfUserExist(phone)){
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
			requests = (ArrayList<CollectionRequest>) 
					conector.getAllCollectionRequest(phone);
			if(requests.size() == 0){
				throw new WebApplicationException(Response.Status.NO_CONTENT);
			}
			ArrayList<CollectionRequestConverter> reqConverter = 
					new ArrayList<CollectionRequestConverter>();
			for(CollectionRequest c : requests){
				reqConverter.add(new CollectionRequestConverter(c));
			}
			return reqConverter;
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}

}
