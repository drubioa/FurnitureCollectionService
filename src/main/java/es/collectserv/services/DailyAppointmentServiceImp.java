package es.collectserv.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.collectserv.collrequest.RequestManagementSingletonImp;
import es.collectserv.converter.ProvisionalAppointmentConverter;
import es.collectserv.exceptions.IlegalRequestException;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;

@Path("/appointment")
public class DailyAppointmentServiceImp implements DailyAppointmentService{
	public static final RequestManagementSingletonImp manager = 
			RequestManagementSingletonImp.getInstance();
	
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
				throw new IlegalRequestException("phone number got"
						+ "previous pending request.");
			}
		} catch (IlegalRequestException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			throw new WebApplicationException(Response.Status.
					BAD_REQUEST);
		} catch (InterruptedException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.
					INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException(Response.Status.
					INTERNAL_SERVER_ERROR);
		}	
			List<ProvisionalAppointment> appointment;
		try {
				appointment = manager.getAppointmentToConfirm(phone_number, 
				itemsRequest,collection_point_id);
			if(appointment == null || appointment.size() == 0){
				throw new NullPointerException("appointment cannot be found.");
			}
			// Se convierte en un objeto que pueda ser correctamente parseable a JSON
			ArrayList<ProvisionalAppointmentConverter> appointmentsConverter 
				= new ArrayList<ProvisionalAppointmentConverter>();
			for(ProvisionalAppointment a : appointment){
				appointmentsConverter.add(new ProvisionalAppointmentConverter(a));
			}
			return appointmentsConverter; 
		}catch (Exception e) {
				e.printStackTrace();
				System.out.println(e.getMessage());
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
			manager.confirmProvisionalAppointment(collectionRequest);
		}
		catch(Exception ex){
			ex.printStackTrace();
			System.out.println(ex.getMessage());
			throw new WebApplicationException(Response.Status
					.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DELETE
	public Response deletePendingRequest(
			@PathParam("phone_number")  String phone_number) {
				return null;
	}
}
