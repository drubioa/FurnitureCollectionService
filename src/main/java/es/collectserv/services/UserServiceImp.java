package es.collectserv.services;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import es.collectserv.model.User;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;

@Path("users")
public class UserServiceImp implements UserService{
	private static SqlConector session ;
	
	public UserServiceImp(){
		session = new SqlConectorImp();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(User user) {
			try {
				if(session.CheckIfUserExist(user.getPhone_number())){
					throw new WebApplicationException( 
							Response.Status.BAD_REQUEST);	
				}
				session.addNewUser(user);
			} catch (IOException e) {
				throw new WebApplicationException(Response.status(Status.CONFLICT)
		                .entity("No pudo crearse el nueve usuario.").build());
			}
			return Response.created(URI.create("/users/"+
					user.getPhone_number())).build();
	}
	
	
	@GET
	@Path("{phone_number}")
	public Response userGotPrendingRequest(@PathParam("phone_number") 
		String phone_number){
		try {
			if(!session.CheckIfUserGotPendingRequest(phone_number)){
				throw new WebApplicationException( 
						Response.Status.BAD_REQUEST);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{phone_number}")
	public Response deleteUser(
			@PathParam("phone_number")  String phone_number) {
		try{	
			if(!session.CheckIfUserExist(phone_number)){
				throw new WebApplicationException( 
						Response.Status.BAD_REQUEST);	
			}
			session.deleteUser(phone_number);
		}catch(Exception e){
			e.printStackTrace();
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok().build();
	}
	
}
