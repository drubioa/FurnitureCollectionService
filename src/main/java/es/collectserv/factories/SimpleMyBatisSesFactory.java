package es.collectserv.factories;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SimpleMyBatisSesFactory{
	private static InputStream is;
	private static SqlSessionFactory sqlSesionFac;
	private final String RES = "mybatis-config.xml";
	
	public SimpleMyBatisSesFactory() throws IOException{
		is = Resources.getResourceAsStream(RES);
		sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
	}
	
	public  SqlSession getOpenSqlSesion(){
		return sqlSesionFac.openSession();
	}
}
