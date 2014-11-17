package es.collectserv.test.mybatis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import es.collectserv.model.Area;
import es.collectserv.model.CollectionPoint;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;

@RunWith(JUnit4.class)
public class TestArea {
	private String configFile;
	private InputStream is;
	private SqlSessionFactory sqlSesionFac;
	
	public TestArea(){
		configFile = "mybatis-config.xml";
		try {
			is = Resources.getResourceAsStream(configFile);
			sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
	 * Try to load all collection points of urban area.
	 */
	@Test
	public void testSelectUrbanAreaPoints(){
		try{
			SqlSession session = sqlSesionFac.openSession();
			List<CollectionPoint> puntos = 
					session.selectList("CollectionPointMapper"
							+ ".selectUrbanAreaPoints");
			session.close();
			assertNotNull(puntos);
			assertTrue(puntos.size() > 0);
			Area area = new Area(puntos);
			assertNotNull(area);
			assertNotNull(area.getPoints());
			assertTrue(area.getPoints().size() > 0);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	/**
	 * Try to load all collection points of rural area.
	 */
	@Test
	public void testSelectRuralPoints(){
		try{
			SqlSession session = sqlSesionFac.openSession();
			List<CollectionPoint> puntos = 
					session.selectList("CollectionPointMapper"
							+ ".selectRuralPoints");
			session.close();
			assertNotNull(puntos);
			assertTrue(puntos.size() > 0);
			Area area = new Area(puntos);
			assertNotNull(area);
			assertNotNull(area.getPoints());
			assertTrue(area.getPoints().size() > 0);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
}
