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

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;
import es.collectserv.model.User;

@Path("/users")
public class UserServiceImp implements UserService{
	
	public UserServiceImp(){
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(User user) {
		try{
			addNewUser(user);
			return Response.created(URI.create("/users/"+
					user.getPhone_number())).build();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
	}
	
	/**
	 * Insert a new user in server DB.
	 * @param user
	 * @throws IOException
	 */
	private void addNewUser(User user) throws IOException{
		SqlSession session =
				new SimpleMyBatisSesFactory().getOpenSqlSesion();
		session.insert("UserMapper.insertUser", user);
		session.commit();
		session.close();		
	}
	
	private Boolean CheckIfUserGotPrendingRequest(String phone_number){
		SqlSession session;
		try {
			session = 
					new SimpleMyBatisSesFactory().getOpenSqlSesion();
		} catch (IOException e) {
			e.printStackTrace();
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		boolean existPreviousRequest = 
				session.selectOne("UserMapper.selectIfUserGotPreviousRequest", 
				phone_number);
		session.close();
		return existPreviousRequest;	
	}
	
	@GET
	@Path("{phone_number}")
	public Response userGotPrendingRequest(@PathParam("phone_number") 
		String phone_number){
		if(!CheckIfUserGotPrendingRequest(phone_number)){
			throw new WebApplicationException(404);
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{phone_number}")
	public Response deleteUser(
			@PathParam("phone_number")  String phone_number) {
		User usuario;
		SqlSession session;
		try{
			session = 
					new SimpleMyBatisSesFactory().getOpenSqlSesion();
			usuario = session.selectOne("UserMapper.selectUser", 
					phone_number);
			// Check if the user exists.
			if(usuario == null){
				System.out.println("Cannot Delete because user not found.");
				throw new WebApplicationException( 
						Response.Status.NOT_FOUND);
			}
			/* Check if user got previous requests. If the user got previous
			request cannot be remove. */
			if(CheckIfUserGotPrendingRequest(phone_number)){
				throw new WebApplicationException( 
						Response.Status.BAD_REQUEST);
			}
			// Delete User
			session.delete("UserMapper.deleteUser",phone_number);
			session.commit();
			session.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		return Response.ok().build();
	}
	
}
