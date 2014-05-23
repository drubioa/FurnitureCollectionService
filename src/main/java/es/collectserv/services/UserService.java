package es.collectserv.services;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import es.collectserv.clases.User;

@Path("/user")
public class UserService {
	private InputStream is;
	private SqlSessionFactory sqlSesionFac;
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(User user) {
		try{
			is = Resources.getResourceAsStream("mybatis-config.xml");
			sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
			SqlSession session = sqlSesionFac.openSession();
			session.insert("UserMapper.insertUser", user);
			session.commit();
			session.close();
			String result = "New user register : " + user.getPhone_number();
			return Response.status(201).entity(result).build();
		}
		catch(Exception e){
			return Response.status(500).entity(e.toString()).build();
		}
	}
	
	@GET
	@Path("{phone_numer}")
	@Consumes(MediaType.APPLICATION_JSON)
    public User findUserByPhone() {
        return new TestBean("a", 1, 1L);
    }
}
