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
					session.selectOne("CollectionPointMapper.selectPoint", 1);
			System.out.println(point.getDirection());
			assertTrue(point.getDirection().equals("Calle Sol"));
		}catch(Exception e){
			fail(e.toString());
		}finally{
			session.close();
		}
	}
	
	
	@Test
	public void testInsertUser(){
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
