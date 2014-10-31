package es.collectserv.test.mybatis;

import static org.junit.Assert.assertTrue;
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
	
}
