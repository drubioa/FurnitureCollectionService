package es.collectserv.test.mybatis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.LocalDate;
import org.junit.Test;

import es.collectserv.model.Area;
import es.collectserv.model.CollectionPoint;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Point;
import es.collectserv.model.User;

public class TestCollectionDays extends MyBatisConfigurator{

	@Test
	public void testSelectAllEmptyCollectionDays(){
		SqlSession session = sqlSesionFac.openSession();
		try{
			List<LocalDate> days = session.selectList("CollectionRequestMapper"+
					".selectAllCollectionDays");
			assertNotNull(days);
			assertTrue(days.size() == 0);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}
	}
	
	/**
	 * Se introduce una nueva solicitud de recogida y se realiza la consulta Select.
	 */
	@Test
	public void testSelectAllCollectionDays(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		LocalDate day = new LocalDate();
		solicitud.setFch_collection(day.plusDays(1));
		solicitud.setFch_request(day);
		SqlSession session = sqlSesionFac.openSession();
		try{
			// Inserts new example user
			session.insert("UserMapper.insertUser", user);
			// Find some collection point
			Point currentPoint = new Point(36.5363800,-6.1930940);
			List<CollectionPoint> puntos = 
					session.selectList("CollectionPointMapper.selectUrbanAreaPoints");
			Area zone = new Area(puntos);
			CollectionPoint point = 
					zone.nearestCollectionPoint(currentPoint); 
			solicitud.setCollectionPoint(point);
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			List<LocalDate> days = session.selectList("CollectionRequestMapper"+
					".selectAllCollectionDays");
			assertNotNull(days);
		}
		catch(Exception e){
			fail(e.toString());
		}finally{
			session.rollback();
			session.close();
		}				
	}
	
	
}
