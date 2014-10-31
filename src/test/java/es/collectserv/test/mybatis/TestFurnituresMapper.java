package es.collectserv.test.mybatis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import es.collectserv.factories.SimpleMyBatisSesFactory;

public class TestFurnituresMapper extends MyBatisConfigurator{
	
	@Test 
	/**
	 * Testing if possible obtain the number of furnitures to collect by 
	 * the current date.
	 * @throws IOException
	 */
	public void testSelectFurnituresByDay() throws IOException{
		SqlSession session = 
				new SimpleMyBatisSesFactory().getOpenSqlSesion();
		try {
			Date date = new Date();
			int furniteres_per_day = session.selectOne("CollectionRequestMapper"
						+".selectFurnituresByDay",date);
			assertTrue(furniteres_per_day == 0);
		} catch (Exception e) {
			fail(e.toString());
		}finally{
			session.close();
		}	
	}
}
