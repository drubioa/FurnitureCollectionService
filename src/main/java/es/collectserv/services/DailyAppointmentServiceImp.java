package es.collectserv.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.collectserv.clases.CollectionRequest;
import es.collectserv.clases.ProvisionalAppointment;
import es.collectserv.collrequest.RequestManagementimp;

@Path("/appointment")
public class DailyAppointmentServiceImp implements DailyAppointmentService{
	private static RequestManagementimp manager = new RequestManagementimp();
	
	public DailyAppointmentServiceImp(){
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProvisionalAppointment> getProvisionalAppintments(
			@QueryParam("phone_number") String phone_number,
			@QueryParam("num_funritures") int itemsRequest,
			@QueryParam("collection_point_id") int collection_point_id){
		try {
			if(manager.userGotPreviosRequest(phone_number)){
				throw new WebApplicationException(Response.Status.
						BAD_REQUEST);
			}
		} catch (Exception e1) {
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		try {
			List<ProvisionalAppointment> appointment =
					manager.getAppointmentToConfirm(phone_number, 
					itemsRequest,collection_point_id); 
			return appointment; 
		} catch (Exception e) {
			throw new WebApplicationException(Response.Status.
					INTERNAL_SERVER_ERROR);
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void confirmCollectionRequest(
			CollectionRequest collectionRequest){
		if(collectionRequest == null){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);			
		}
		try{
			manager.confirmProvisionalAppointmen(collectionRequest);
		}
		catch(Exception ex){
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
