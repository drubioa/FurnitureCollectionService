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
	
	public TestDailyAppointmentService(){
	
	}

	@Before
	public void setUp(){
		mAppointmentService = new DailyAppointmentServiceConectorImp
				("localhost", 8080, "http");				
	}
	
	/**
	 * Get a provisional appointment.
	 */
	@Test
	public void testGet1ProvisionalAppointments(){
		final String phoneNumber = "600400314";
		final int num_furnitures = 1;
		final int collectionPointID = 1; 
		mExpectedValueOfNumberAppointments = 1;
		createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
		deleteAllRequestsOf(phoneNumber);
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
		createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
		deleteAllRequestsOf(phoneNumber);
	}
	
	@Test
	public void testGet3ProvisionalAppointments(){
		final String phoneNumber = "60132016";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1;
		final int collectionPointID = 1; 
		mExpectedValueOfNumberAppointments = 3;
		createExampleRequest(phoneNumber,num_furnitures,collectionPointID);
		deleteAllRequestsOf(phoneNumber);
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
	
	/**
	 * Get a provisional appointment, later confirm it.
	 *  Finally cancel this appointment.
	 * @throws Exception 
	 */
	@Test
	public void testGetAndConfirm1Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "622010126";
		final int num_furnitures = 2;
		final int collectionPointId = 1;
		getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
				collectionPointId);
	}
	
	/**
	 * Get a provisional appointment, later confirm it.
	 *  Finally cancel this appointment.
	 * @throws Exception 
	 */
	@Test
	public void testGetAndConfirm2Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "622010121";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER + 1;
		final int collectionPointId = 1;
		getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
				collectionPointId);
	}
	
	@Test
	public void testGetAndConfirm3Appointment() throws Exception{
		final String userName = "Paco";
		final String userPhoneNumber = "622010121";
		final int num_furnitures = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1;
		final int collectionPointId = 1;
		getAndConfirm1Appointment(userName,userPhoneNumber,num_furnitures,
				collectionPointId);
	}
	
	public static void getAndConfirm1Appointment(String userName, String userPhoneNumber, 
			int num_furnitures, int collectionPointId) throws Exception{
		final UserServiceConector conector = 
				new UserServiceConectorImp("localhost",8080,"http");
		final User user = new User(userName,userPhoneNumber);
		try{
			HttpResponse response = conector.addUser(user);
			assertTrue(response.getStatusLine().getStatusCode() == 201);
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
			mAppointmentService.deletePendingAppointments(userPhoneNumber);
		}
		catch(Exception e){
			fail(e.toString());
			throw e;
		}		
		finally{
			HttpResponse response;
			try {
				response = conector.deleteUser(user);
				assertTrue(response.getStatusLine().getStatusCode() == 200);
			} catch (Exception e) {
				fail(e.toString());
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Crea una lista de ejemplo de muebles distintos.
	 * @param num_furnitures
	 * @return 
	 */
	public static List<Furniture> createExampleFurnitureList(int num_furnitures){			
		List<Furniture> furnitures = new ArrayList<Furniture>();
		for(int i = 1;i <= num_furnitures;i++){
			furnitures.add(new Furniture(i,1));
		}
		return furnitures;
	}

}
