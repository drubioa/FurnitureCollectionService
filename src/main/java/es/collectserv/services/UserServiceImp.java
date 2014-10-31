package es.collectserv.services;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
			return Response.status(500).entity(e.toString()).build();
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
	
	@GET
	@Path("{phone_number}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserByPhoneNumber(@PathParam("phone_number") 
		String phone_number){
		User usuario;
		try{
			SqlSession session = 
					new SimpleMyBatisSesFactory().getOpenSqlSesion();
			usuario = session.selectOne("UserMapper.selectUser", 
					phone_number);
			session.close();
		}catch(Exception e){
			throw new WebApplicationException( 
					Response.Status.INTERNAL_SERVER_ERROR);
		}
		if(usuario == null){
			throw new WebApplicationException(Response.Status.NOT_ACCEPTABLE);
		}
		return usuario;
	}
	
}
