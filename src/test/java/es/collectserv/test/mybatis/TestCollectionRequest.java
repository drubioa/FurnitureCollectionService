package es.collectserv.test.mybatis;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.collectserv.model.Area;
import es.collectserv.model.CollectionPoint;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.Point;
import es.collectserv.model.User;

/**
 * 
 * @author Diego Rubio Abujas
 *
 */
public class TestCollectionRequest extends MyBatisConfigurator{
	private  Area urban;
	private  CollectionRequest solicitud;
	private  String name = "Diego";
	private  String phone_number = "699390219";
	private  CollectionPoint point;
	private  SqlSession session = sqlSesionFac.openSession();
	
	public TestCollectionRequest() throws Exception{
		session = sqlSesionFac.openSession();
		List<CollectionPoint> puntos = 
				session.selectList(
						"CollectionPointMapper.selectUrbanAreaPoints");
		urban = new Area(puntos);
		Point currentPoint = new Point(36.5363800,-6.1930940);
		point = urban.nearestCollectionPoint(currentPoint);
		session.close();
	}
	
	/**
	 * Se crea una solicitud de ejemplo.
	 */
	@Before 
	public void setUp(){
		session = sqlSesionFac.openSession();
		session.insert("UserMapper.insertUser", new User(name,phone_number));
		solicitud = createExampleRequest(name,phone_number,point);
	}
	
	/**
	 * Se realiza rollback y se cierra la conexion con la base de datos.
	 */
	@After
	public void tearDown(){
		session.rollback();
		session.close();
	}
	
	@Test
	public void testInsertAndSelectRequestByUser(){
		try{
			int numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			// Inicialmente no se ha introducido la solicitud por lo que el usuario
			//no tiene ninguna solicitud pendiente.
			assertTrue(numRequests == 0);
			assertNull(session.selectOne("CollectionRequestMapper"+
					".selectRequestByUser",solicitud.getTelephone()));
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
			numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			
			assertTrue(numRequests > 0);
			// Una vez el usuario la ha introducido se localiza la solicitud.
			assertNotNull(session.selectOne("CollectionRequestMapper"+
					".selectRequestByUser",solicitud.getTelephone()));
		}
		catch(Exception e){
			fail(e.toString());
		}		
	}
	
	/**
	 * Se introduce una solicitud de recogida pendiente de confirmar y se comprueba
	 * que se ha introducido correctamente.
	 */
	@Test
	public void testInsertCollectionRequest(){
		try{		
			// At the moment request id is 0 because it doesn't introduced in the system.
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
			// When the request was introduced in the system, this id change
			assertTrue(solicitud.getId() > 0);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	

	
	/**
	 * Se crea una nueva solicitud de recogida y se inserta en el sistema. 
	 */
	@Test
	public void testinsertCollectionRequestAndSelectCollectionRequest(){
		try{
			assertTrue(solicitud.getId() == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			assertNotNull(
					session.selectOne("CollectionRequestMapper"+
			".selectCollectionRequestById",solicitud.getId()));
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	@Test
	public void testSelectAllPendingCollectionRequests(){
		try{
			assertTrue(solicitud.getId() == 0);
			int numberOfPendingRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequest").size();
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			assertNotNull(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequest"));
			assertTrue(session.selectList("CollectionRequestMapper"+
					".selectPendingRequest").size() ==  numberOfPendingRequests + 2);
		}
		catch(Exception e){
			fail(e.toString());
		}	
	}
	
	@Test
	public void testSelectEmptyPendingResquestByPhone(){
		try{
			assertTrue(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequestByPhone","900900900").size() == 0);
		}
		catch(Exception e){
			fail(e.toString());
		}		
	}
	
	@Test
	public void testSelectPendingRequestByPhone(){
		try{
			assertTrue(solicitud.getId() == 0);
			int numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			assertTrue(numRequests == 0);
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db by phone
			numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			assertTrue(numRequests == 1);
		}
		catch(Exception e){
			fail(e.toString());
		}	
	}
	
	/**
	 * Se inserta y elimina una solicitud de recogida. 
	 */
	@Test
	public void testInsertAndDeleteCollectionRequest(){
		try{
			int numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			assertTrue(numRequests == 0);
			assertTrue(solicitud.getId() == 0);
			int pendingRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequest").size();;
			session.insert("CollectionRequestMapper.insertCollectionRequest",
					solicitud);
			session.insert("CollectionRequestMapper.insertFurnituresInRequest",
					solicitud);
			assertTrue(solicitud.getId() > 0);
			// Select inserted request in db
			numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			assertTrue(numRequests >= 1);
			assertNotNull(
					session.selectList("CollectionRequestMapper"+
			".selectPendingRequest"));
			session.delete("CollectionRequestMapper.deleteFurnituresFromCollReq",
					solicitud);
			session.delete("CollectionRequestMapper.deleteCollectionRequest",
					solicitud);
			assertTrue(session.selectList("CollectionRequestMapper"+
					".selectPendingRequest").size() == pendingRequests);
			numRequests = session.selectList("CollectionRequestMapper"+
					".selectPendingRequestByPhone",phone_number).size();
			assertTrue(numRequests == 0);
		}
		catch(Exception e){
			fail(e.toString());
		}			
	}

	private CollectionRequest createExampleRequest
		(String name,String phone_number, CollectionPoint point){
		CollectionRequest solicitud = new CollectionRequest();
		solicitud.setTelephone(phone_number);
		solicitud.setCollectionPointId(point.getPointId());
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		LocalDate day = new LocalDate();
		solicitud.setFch_collection(day.plusDays(1));
		solicitud.setFch_request(day);
		List<Furniture> furnitures = new ArrayList<Furniture>();
		furnitures.add(new Furniture(1,2));
		furnitures.add(new Furniture(2,1));	
		solicitud.setFurnitures(furnitures);
		return solicitud;
	}
}
