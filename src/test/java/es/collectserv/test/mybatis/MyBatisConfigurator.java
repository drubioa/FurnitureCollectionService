package es.collectserv.test.mybatis;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


@RunWith(JUnit4.class)
public class MyBatisConfigurator {
	private String configFile;
	private InputStream is;
	protected static SqlSessionFactory sqlSesionFac;
	
	public MyBatisConfigurator(){
		configFile = "mybatis-config.xml";
		try {
			is = Resources.getResourceAsStream(configFile);
			sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
}
