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

import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.User;


@RunWith(JUnit4.class)
public class MyBatisTests {
	private String configFile;
	private InputStream is;
	private SqlSessionFactory sqlSesionFac;
	
	public MyBatisTests(){
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
	public void testSelectCollectPoint(){
		SqlSession session = sqlSesionFac.openSession();
		try{
			CollectionPoint point = (CollectionPoint) 
					session.selectOne("es.collectserv.clases.CollectionPointMapper.selectPoint", 1);
			System.out.println(point.getDirection());
			assertTrue(point.getDirection().equals("Calle Sol"));
		}finally{
			session.close();
		}
	}
	
	
	@Test
	public void testInsertUser(){
		SqlSession session = sqlSesionFac.openSession();
		User user1 = new User("Diego","699390216");
		session.insert("es.collectserv.services.UserMapper.insertUser", user1);
		session.commit();
		User user2 = (User) session.selectOne("es.uca.collectserv.user.UserMapper.selectUser", 1);
		assertTrue(user2.getName() == user1.getName());
		assertTrue(user2.getPhone_number() == user1.getPhone_number());
		session.close();
	}

}
