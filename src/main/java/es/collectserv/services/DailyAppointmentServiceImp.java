package es.collectserv.services;

import java.util.ArrayList;
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

import es.collectserv.collrequest.RequestManagementimp;
import es.collectserv.converter.ProvisionalAppointmentConverter;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

@Path("/appointment")
public class DailyAppointmentServiceImp implements DailyAppointmentService{
	private static RequestManagementimp manager = new RequestManagementimp();
	
	public DailyAppointmentServiceImp(){
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ProvisionalAppointmentConverter> getProvisionalAppintments(
			@QueryParam("phone_number") String phone_number,
			@QueryParam("num_funritures") int itemsRequest,
			@QueryParam("collection_point_id") int collection_point_id){
		try {
			if(manager.userGotPreviosRequest(phone_number)){
				System.out.println("ERROR: user "+phone_number+" got previous request.");
				throw new WebApplicationException(Response.Status.
						BAD_REQUEST);
			}
		} catch (Exception e1) {
			System.out.println("ERROR: "+e1.getMessage());
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
		try {
			List<ProvisionalAppointment> appointment =
					manager.getAppointmentToConfirm(phone_number, 
					itemsRequest,collection_point_id); 
			if(appointment == null){
					System.out.println("appointment is null.");
			}
			ArrayList<ProvisionalAppointmentConverter> appointmentsConverter 
				= new ArrayList<ProvisionalAppointmentConverter>();
			for(int i = 0;i < appointment.size();i++){
				appointmentsConverter
				.add(new ProvisionalAppointmentConverter(appointment.get(i)));
			}
			return appointmentsConverter; 
		} catch (Exception e) {
			System.out.println("ERROR: "+e.getMessage());
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
			System.out.println("ERROR: "+ex.getMessage());
			throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
}
