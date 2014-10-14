package es.collectserv.test.mybatis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import es.collectserv.clases.Area;
import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.Point;
import es.collectserv.clases.User;
import es.collectserv.collrequest.CollectionRequest;

public class TestCollectionRequest extends MyBatisConfigurator{


	
	@Test
	public void testInsertCollectionRequest(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		solicitud.setFch_collection(new Date());
		solicitud.setFch_request(new Date());
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940); 
			Area zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}
	}
	
	@Test
	public void testInsertAndSelectCollectionRequest(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		solicitud.setFch_collection(new Date());
		solicitud.setFch_request(new Date());
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940); 
			Area zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			assertNotNull(
					session.selectOne("CollectionRequestMapper"+
			".selectCollectionRequestById",solicitud.getId()));
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}
	
	
	
	@Test
	public void testSelectAllCollectionRequests(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		solicitud.setFch_collection(gc.getTime());
		solicitud.setFch_request(new Date());
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940); 
			Area zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			assertNotNull(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequest"));
			assertTrue(session.selectList("CollectionRequestMapper"+
					".selectPendingRequest").size() == 2);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}
	
	@Test
	public void testSelectEmptyPendingResquestByPhone(){
		SqlSession session = sqlSesionFac.openSession();
		try{
			assertTrue(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequestByPhone","900900900").size() == 0);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}
	
	@Test
	public void testSelectPendingRequestByPhone(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		solicitud.setFch_collection(gc.getTime());
		solicitud.setFch_request(new Date());
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940); 
			Area zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db by phone
			assertNotNull(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequestByPhone",phone_number));
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}
	
}
