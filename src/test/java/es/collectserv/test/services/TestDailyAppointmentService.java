package es.collectserv.test.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.conector.DailyAppointmentServiceConector;
import es.collectserv.conector.DailyAppointmentServiceConectorImp;
import es.collectserv.conector.UserServiceConector;
import es.collectserv.conector.UserServiceConectorImp;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;
import es.collectserv.test.collectserv.AppointmentValidator;

@RunWith(JUnit4.class)
public class TestDailyAppointmentService {
	private static DailyAppointmentServiceConector mAppointmentService;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private int mExpectedValueOfNumberAppointments;
	private static final String HOST = "localhost";
	
	public TestDailyAppointmentService(){
	
	}

	@Before
	public void setUp(){
		mAppointmentService = new DailyAppointmentServiceConectorImp
				(HOST, 8080, "http");				
	}
	
	@After
	public void tearDown(){
		// Wait 2 second after each test.
		try {
		    Thread.sleep(2000);   //2000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Get a provisional appointment.
	 */
	@Test
	public void testGet1ProvisionalAppointments(){
		final String phoneNumber = "601100318";
		final int num_furnitures = 1;
		final int collectionPointID = 1; 
		mExpectedValueOfNumberAppointments = 1;
		try{
			createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
			getAllCollectionRequest(phoneNumber);
		}catch(Exception e){
			if(!e.toString().contains("HTTP error code : 404")){
				fail(e.toString());
			}
		}
		finally{
			deleteAllRequestsOf(phoneNumber);
		}
	}
			
	/**
	 * Get a provisional appointment for 2 days.
	 */
	@Test
	public void testGet2ProvisionalAppointments(){
		final String phoneNumber = "600320016";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER + 1;
		final int collectionPointID = 1; 
		mExpectedValueOfNumberAppointments = 2;
		try{
			createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
			getAllCollectionRequest(phoneNumber);
		}catch(Exception e){
			if(!e.toString().contains("HTTP error code : 404")){
				fail(e.toString());
			}
		}
		finally{
			deleteAllRequestsOf(phoneNumber);
		}
	}
	
	@Test
	public void testGet3ProvisionalAppointments(){
		final String phoneNumber = "60132016";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1;
		final int collectionPointID = 1; 
		mExpectedValueOfNumberAppointments = 3;
		try{
			createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
			getAllCollectionRequest(phoneNumber);
		}catch(Exception e){
			if(!e.toString().contains("HTTP error code : 404")){
				fail(e.toString());
			}
		}
		finally{
			deleteAllRequestsOf(phoneNumber);
		}
	}

	/**
	 * Get a provisional appointment, later confirm it.
	 *  Finally cancel this appointment.
	 * @throws Exception 
	 */
	@Test
	public void testGetAndConfirm1Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "600000001";
		final int num_furnitures = 1;
		final int collectionPointId = 1;
		try{
			createUser(userName,userPhoneNumber);
			getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
				collectionPointId);
			assertTrue(getAllCollectionRequest(userPhoneNumber).size() == 1);
		} catch (Exception e) {
			fail(e.toString());
		}
		finally{
			deleteAllRequestsOf(userPhoneNumber);
			deleteUser(new User(userName,userPhoneNumber));
		}
	}

	/**
	 * Get a provisional appointment, later confirm it.
	 *  Finally cancel this appointment.
	 * @throws Exception 
	 */
	@Test
	public void testGetAndConfirm2Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "622317121";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER + 1;
		final int collectionPointId = 1;
		try{
			createUser(userName,userPhoneNumber);
			getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
				collectionPointId);
			assertTrue(getAllCollectionRequest(userPhoneNumber).size() >= 2);
		} catch (Exception e) {
			fail(e.toString());
		}
		finally{
			deleteAllRequestsOf(userPhoneNumber);
			deleteUser(new User(userName,userPhoneNumber));
		}
	}

	@Test
	public void testGetAndConfirm3Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "666778800";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1;
		final int collectionPointId = 1;
		try {
			createUser(userName,userPhoneNumber);
			getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
					collectionPointId);
			assertTrue(getAllCollectionRequest(userPhoneNumber).size() >= 3);
		} catch (Exception e) {
			fail(e.toString());
		}
		finally{
			deleteAllRequestsOf(userPhoneNumber);
			deleteUser(new User(userName,userPhoneNumber));
		}
	}

	/**
	 * Se prueba a solicitur peticiones pendientes de un usuario que no existe
	 * en el sistema.
	 */
	@Test
	public void testGetConfirmedAppointmentOfUnexistUser(){
		final String userPhoneNumber = "800000000";
		try {
			getAllCollectionRequest(userPhoneNumber);
		} catch (Exception e) {
			if(!e.toString().contains("HTTP error code : 404")){
				fail(e.toString());
			}
		}
	}
	
	/**
	 * Se prueba a solicitud solicitudes confirmados de un usuario registrado
	 * que no tiene ninguna solicitud pendiente
	 */
	@Test
	public void testGetConfirmedAppUserNoHaveReq(){
		final String userName = "Anonymous";
		final String userPhoneNumber = "800000001";
		try {
			createUser(userName,userPhoneNumber);
			getAllCollectionRequest(userPhoneNumber);
		} catch (Exception e) {
			if(!e.toString().contains("HTTP error code : 204")){
				fail(e.toString());
			}
		}
		finally{
			try {
				deleteUser(new User(userName,userPhoneNumber));
			} catch (Exception e) {
				fail(e.toString());
			}
		}
	}
	
	
	private void createExampleRequest(String phoneNumber, int num_furnitures, int collectionPointID){
		try{
			List<ProvisionalAppointment> appointments = 
					mAppointmentService.getProvisionalAppointments(
							phoneNumber,num_furnitures,collectionPointID);
			assertNotNull(appointments);
			assertTrue(appointments.size() >= mExpectedValueOfNumberAppointments);
			AppointmentValidator.validAppointment(appointments);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	private void deleteAllRequestsOf(String phoneNumber){
		try {
			mAppointmentService.deletePendingAppointments(phoneNumber);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (HttpException e) {
			e.printStackTrace();
			fail(e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	private void createUser(String userName, String userPhoneNumber) throws Exception{
		final UserServiceConector conector = 
				new UserServiceConectorImp(HOST,8080,"http");
		final User user = new User(userName,userPhoneNumber);
		HttpResponse response = conector.addUser(user);
		assertTrue(response.getStatusLine().getStatusCode() == 201);
	}
	
	public static void getAndConfirm1Appointment(String userName, String userPhoneNumber, 
			int num_furnitures, int collectionPointId) throws Exception{
		try{
			// First get a provisional appointment
			List<ProvisionalAppointment> appointments =
					mAppointmentService.getProvisionalAppointments(
							userPhoneNumber,num_furnitures,collectionPointId);
			assertNotNull(appointments);
			assertTrue(appointments.size() == ((num_furnitures - 1) 
					/ MAX_FURNIUTRES_PER_DAY_USER)+1);
			AppointmentValidator.validAppointment(appointments);
			for(ProvisionalAppointment appointment: appointments){
				List<Furniture> furnitures = createExampleFurnitureList(
						appointment.getNumFurnitures());
				CollectionRequest req =
						new CollectionRequest(appointment,furnitures);
				assertTrue(req.checkCorrectRequest());
				mAppointmentService.confirmAppointment(req);
			}
		}
		catch(Exception e){
			fail(e.toString());
			throw e;
		}
	}
	
	public static List<CollectionRequest> getAllCollectionRequest(String phone) throws Exception{
		DailyAppointmentServiceConector conector;
		conector = new DailyAppointmentServiceConectorImp(HOST,8080,"http");
		List<CollectionRequest> list = conector.getPendingCollectionRequest(phone);
		if(list != null){
			for(CollectionRequest req : list){
				validCollectionRequest(req);
			}
		}
		return list;
	}
		
	private static void deleteUser(User user) throws Exception{
		UserServiceConector conector;
		conector = new UserServiceConectorImp(HOST,8080,"http");
		HttpResponse response = conector.deleteUser(user);
		assertTrue(response.getStatusLine().getStatusCode() == 200);
	}
	
	private static boolean validCollectionRequest(CollectionRequest req){
		if(req == null){
			return false;
		}
		int totalFurnitures = 0;
		for(Furniture f : req.getFurnitures()){
			totalFurnitures += f.getCantidad();
			assertNotNull(f.getName());
			assertTrue(!f.getName().isEmpty());
		}
		return req.checkCorrectRequest() && req.getNumFurnitures() == totalFurnitures;
	}
	
	/**
	 * Crea una lista de ejemplo de muebles distintos.
	 * @param num_furnitures
	 * @return 
	 */
	private static List<Furniture> createExampleFurnitureList(int num_furnitures){			
		List<Furniture> furnitures = new ArrayList<Furniture>();
		for(int i = 1;i <= num_furnitures;i++){
			furnitures.add(new Furniture(i,1));
		}
		return furnitures;
	}

}
