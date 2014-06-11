package es.collectserv.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.Furniture;
import es.collectserv.clases.Point;
import es.collectserv.clases.User;
import es.collectserv.clases.Zone;
import es.collectserv.collrequest.CollectionRequest;
import es.collectserv.factories.SimpleMyBatisSesFactory;

@RunWith(JUnit4.class)
public class TestMyBatis {
	private String configFile;
	private InputStream is;
	private SqlSessionFactory sqlSesionFac;
	
	public TestMyBatis(){
		configFile = "mybatis-config.xml";
		try {
			is = Resources.getResourceAsStream(configFile);
			sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testOpenAndCloseSession() {
		try{
			SqlSession session = sqlSesionFac.openSession();
			session.close();
			assertTrue(true);}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	@Test 
	public void testSelectFurnituresByDay() throws IOException{
		SqlSession session = 
				new SimpleMyBatisSesFactory().getOpenSqlSesion();
		try {
			Date date = new Date();
			int furniteres_per_day = session.selectOne("CollectionRequestMapper"
						+".selectFurnituresByDay",date);
			assertTrue(furniteres_per_day == 0);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	@Test
	public void testObtainsCollectionsPointByZone(){
		try{
			SqlSession session = sqlSesionFac.openSession();
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
			session.close();
			assertNotNull(zone);
			assertNotNull(zone.getPoints());
			assertTrue(zone.getPoints().size() > 0);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	@Test
	public void testGetPointByZone(){
		try{
			SqlSession session = sqlSesionFac.openSession();
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
			session.close();
			assertNotNull(zone);
			assertNotNull(zone.getPoints());
			assertTrue(zone.getPoints().size() > 0);
			Point currentPoint = new Point(36.5363800,-6.1930940);
			assertTrue(zone.nearestCollectionPoint(currentPoint) != null);
		}
		catch(Exception e){
			fail(e.toString());
		}	
	}
	
	
	@Test
	public void testAddFurnitureToCollectionRequest(){
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			// Test to add furnitures
			List<Furniture> list = new ArrayList<Furniture>();
			Furniture example = new Furniture();
			example.setCantidad(1);
			example.setId(1);
			list.add(example);
			solicitud.setFurnitures(list);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}
	
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
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
	
	@Test
	public void testSelectAllEmptyCollectionDays(){
		SqlSession session = sqlSesionFac.openSession();
		try{
			List<Date> days = session.selectList("CollectionRequestMapper"+
					".selectAllCollectionDays");
			assertNotNull(days);
			assertTrue(days.size() == 0);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}
	}
	
	@Test
	public void testSelectAllCollectionDays(){
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
			Zone zone = session.selectOne("ZoneMapper.selectZone", 3);
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
			List<Date> days = session.selectList("CollectionRequestMapper"+
					".selectAllCollectionDays");
			assertNotNull(days);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}				
	}
	
	@Test
	public void testInserAndDeleteUser(){
		String name = "Diego";
		String phone_number = "699390216";
		SqlSession session = sqlSesionFac.openSession();
		try{
			User user1 = new User(name,phone_number);
			session.insert("UserMapper.insertUser", user1);
			User user2 = (User) session.selectOne("UserMapper.selectUser", 
					phone_number);
			assertTrue(user2.getName().equals(name));
			assertTrue(user2.getPhone_number().equals(phone_number));
			session.delete("UserMapper.deleteUser", phone_number);
			session.rollback();
		}catch(Exception e){
			fail(e.toString());
		}finally{
			session.close();
		}
	}

}
