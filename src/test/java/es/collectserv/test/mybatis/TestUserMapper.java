package es.collectserv.test.mybatis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import es.collectserv.model.User;

public class TestUserMapper extends MyBatisConfigurator{
	
	@Test
	/**
	 * Insert a new user and later deleted it. 
	 * Check if the user was removed successful.
	 */
	public void testInserAndDeleteUser(){
		String name = "Diego";
		String phone_number = "699390256";
		SqlSession session = sqlSesionFac.openSession();
		try{
			User user1 = new User(name,phone_number);
			session.insert("UserMapper.insertUser", user1);
			session.delete("UserMapper.deleteUser", phone_number);
			session.rollback();
		}catch(Exception e){
			fail(e.toString());
		}finally{
			session.close();
		}
	}

	@Test
	/**
	 * Try to add a same user twice.
	 */
	public void testInserTwoUserWithSamePhone(){
		String name = "Diego";
		String phone_number = "699323216";
		SqlSession session = sqlSesionFac.openSession();
		try{
			User user = new User(name,phone_number);
			// Insert the user twice.
			session.insert("UserMapper.insertUser", user);
			session.insert("UserMapper.insertUser", user);
			fail("Two users with same number wew introduced.");
		}catch(Exception e){
			assertTrue(true);
		}finally{
			session.close();
		}
	}
	
	/**
	 * Se verifica la consulta select para comprobar que un usuario no tiene solicitudes
	 * previas.
	 */
	@Test
	public void testUserNotGotPrevRequest(){
		String phone_number = "699323216";
		SqlSession session = sqlSesionFac.openSession();
		try{
			boolean existPreviousRequest = 
					session.selectOne("UserMapper.selectIfUserGotPreviousRequest", 
					phone_number);
			assertFalse(existPreviousRequest);
		}catch(Exception e){
			fail();
			e.printStackTrace();
		}finally{
			session.close();
		}
	}
	
}
