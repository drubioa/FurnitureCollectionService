package es.collectserv.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import es.collectserv.clases.Point;
import es.collectserv.clases.User;
import es.collectserv.clases.Zone;

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
	public void testInserAndDeletetUser(){
		String name = "Diego";
		String phone_number = "699390216";
		SqlSession session = sqlSesionFac.openSession();
		try{
		User user1 = new User(name,phone_number);
		session.insert("UserMapper.insertUser", user1);
		User user2 = (User) session.selectOne("UserMapper.selectUser", phone_number);
		assertTrue(user2.getName().equals(name));
		assertTrue(user2.getPhone_number().equals(phone_number));
		session.delete("UserMapper.deleteUser", phone_number);
		session.commit();
		}catch(Exception e){
			fail(e.toString());
		}finally{
			session.close();
		}
	}

}
