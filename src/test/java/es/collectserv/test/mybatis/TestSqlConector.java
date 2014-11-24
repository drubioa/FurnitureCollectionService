package es.collectserv.test.mybatis;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.model.User;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;


@RunWith(JUnit4.class)
public class TestSqlConector {
	SqlConector session;
	
	public TestSqlConector(){
		session =  new SqlConectorImp();
	}
	
	@Test
	public void trestAddAndRemoveNewUser(){
		User user = new User("anonymous","682535978");
		try {
			session.addNewUser(user);
			session.deleteUser(user.getPhone_number());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
}
