package es.uca.collectserv.test;

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
	public void testOpenSession() {
		try{
			SqlSession session = sqlSesionFac.openSession();
			session.close();
			assertTrue(true);}
		catch(Exception e){
			fail(e.toString());
		}
	}
	

}
