package es.collectserv.services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sun.mail.iap.Response;

import es.collectserv.clases.ProvisionalAppointment;
import es.collectserv.clases.RequestManagement;

@Path("/appointment")
public class DailyAppointmentService {
	private static RequestManagement manager = new RequestManagement();
	
	DailyAppointmentService(){
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ProvisionalAppointment> getProvisionalAppintments(
			@QueryParam("phone_number") String phone_number,
			@QueryParam("num_funritures") int cantidad){
		return null;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response confirmCollectionRequest(List<ProvisionalAppointment> appoitmentsToConfirm){
		return null;
	}
}
