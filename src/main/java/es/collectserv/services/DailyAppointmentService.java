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

import es.collectserv.collrequest.CollectionRequest;
import es.collectserv.collrequest.ProvisionalAppointment;
import es.collectserv.collrequest.RequestManagementimp;

@Path("/appointment")
public class DailyAppointmentService {
	private static RequestManagementimp manager = new RequestManagementimp();
	
	public DailyAppointmentService(){
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProvisionalAppointment> getProvisionalAppintments(
			@QueryParam("phone_number") String phone_number,
			@QueryParam("num_funritures") int itemsRequest,
			@QueryParam("collection_point_id") int collection_point_id){
		if(manager.userGotPreviosRequest(phone_number)){
			throw new WebApplicationException(Response.Status.
					BAD_REQUEST);
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
	public Response confirmCollectionRequest(
			List<CollectionRequest> appoitmentsToConfirm){
		for(int i = 0;i < appoitmentsToConfirm.size();i++){
			manager.confirmProvisionalAppointmen(appoitmentsToConfirm.get(i));
		}
		return null;
	}
}
