package es.collectserv.test.mybatis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import es.collectserv.clases.Area;
import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.CollectionRequest;
import es.collectserv.clases.Furniture;
import es.collectserv.clases.Point;
import es.collectserv.clases.User;
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
			e.printStackTrace();
		}finally{
			session.close();
		}
		
	}
	
	
	
	
	@Test
	/**
	 * Add a furniture to collection request and later check if this 
	 * furniture was added to the request.
	 */
	public void testAddFurnitureToCollectionRequest(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		solicitud.setFch_collection(new Date());
		solicitud.setFch_request(new Date());
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940); 
			Area zone = session.selectOne("ZoneMapper.selectZone", 3);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			// Test to add furnitures
			List<Furniture> list = new ArrayList<Furniture>();
			Furniture example = new Furniture();
			example.setCantidad(1);
			example.setId(1);
			list.add(example);
			solicitud.setFurnitures(list);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}		
	}

}
