package es.collectserv.sqlconector;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.factories.SimpleMyBatisSesFactory;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.User;

public class SqlConectorImp implements SqlConector{
	private SqlSession session;
	
	public SqlConectorImp(){
	
	}
	
	public void addNewUser(User user) throws IOException{
		session =
				new SimpleMyBatisSesFactory().getOpenSqlSesion();
		session.insert("UserMapper.insertUser", user);
		session.commit();
		session.close();		
	}
	
	public Boolean CheckIfUserGotPendingRequest(String phone_number) throws IOException{
		try {
			session = 
					new SimpleMyBatisSesFactory().getOpenSqlSesion();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		boolean existPreviousRequest = 
				session.selectOne("UserMapper.selectIfUserGotPreviousRequest", 
				phone_number);
		session.close();
		return existPreviousRequest;
	}
	
	public void deleteUser(String phone_number) 
			throws IllegalArgumentException, IOException{
		session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		User usuario = session.selectOne("UserMapper.selectUser", 
					phone_number);
		// Check if the user exists.
		if(usuario == null){
			session.close();
			System.out.println("Cannot Delete because user not found.");
			throw new WebApplicationException( 
					Response.Status.NOT_FOUND);
		}
		/* Check if user got previous requests. If the user got previous
		request cannot be remove. */
		if(CheckIfUserGotPendingRequest(phone_number)){
			session.close();
			throw new WebApplicationException( 
					Response.Status.BAD_REQUEST);
		}
		// Delete User
		session.close();
		session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		session.delete("UserMapper.deleteUser",phone_number);
		session.commit();
		session.close();		
	}

	public int selectFurnituresByDay(Date day) throws IOException {
		session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		int mFurniteres_per_day = 
				session.selectOne("CollectionRequestMapper"
					+".selectFurnituresByDay",day);
		session.close();
		return mFurniteres_per_day;
	}

	public void registerRequestInDB(CollectionRequest request) throws IOException{
	    session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
	 	session.insert("CollectionRequestMapper.insertCollectionRequest", request);
	 	session.insert("CollectionRequestMapper.insertFurnituresInRequest",
	 			request);
	 	session.commit();
	 	session.close();
	}

	public void removesPendingRequestFromBD(CollectionRequest request) throws IOException{
		session = new SimpleMyBatisSesFactory().getOpenSqlSesion();
		session.delete("CollectionRequestMapper.deleteFurnituresFromCollReq",request);
		session.delete("CollectionRequestMapper.deleteCollectionRequest",request);
		session.commit();
		session.close();
	}

	public List<Date> selectAllCollectionDays() throws IOException {
		session = new SimpleMyBatisSesFactory()
		.getOpenSqlSesion();
		List<Date> dates = session.selectList(
			"CollectionRequestMapper.selectAllCollectionDays");
		session.close();
		return dates;
	}

	public List<CollectionRequest> getPendingCollectionRequest(String phone) 
			throws IOException{
		session = new SimpleMyBatisSesFactory()
		.getOpenSqlSesion();
		List<CollectionRequest> requests = session.selectList("CollectionRequestMapper"+
				".selectPendingRequestByPhone",phone);
		session.close();
		return requests;	
	}
}
