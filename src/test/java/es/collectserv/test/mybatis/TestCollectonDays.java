package es.collectserv.test.mybatis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import es.collectserv.clases.Area;
import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.Point;
import es.collectserv.clases.User;
import es.collectserv.collrequest.CollectionRequest;

public class TestCollectonDays extends MyBatisConfigurator{

	@Test
	public void testSelectAllEmptyCollectionDays(){
		SqlSession session = sqlSesionFac.openSession();
		try{
			List<Date> days = session.selectList("CollectionRequestMapper"+
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
	
	@Test
	public void testSelectAllCollectionDays(){
		String name = "Diego";
		String phone_number = "615690926";
		User user = new User(name,phone_number);
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		solicitud.setFch_collection(gc.getTime());
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
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			List<Date> days = session.selectList("CollectionRequestMapper"+
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
