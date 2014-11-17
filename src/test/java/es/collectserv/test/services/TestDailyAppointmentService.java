package es.collectserv.test.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.conector.DailyAppointmentServiceConector;
import es.collectserv.conector.DailyAppointmentServiceConectorImp;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.ProvisionalAppointment;

@RunWith(JUnit4.class)
public class TestDailyAppointmentService {
	static final int SLEEP_TIME = 5000; 
	private DailyAppointmentServiceConector mAppointmentService;
	
	public TestDailyAppointmentService(){
		mAppointmentService = new DailyAppointmentServiceConectorImp
				("localhost", 8080, "http");			
	}

	@After 
	public void tearDown(){
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get a provisional appointment.
	 */
	@Test
	public void testGetProvisionalAppointments(){
		final String phoneNumber = "604300014";
		final int num_furnitures = 1;
		final int collectionPointID = 1; 
		try{
			List<ProvisionalAppointment> appointments = 
					mAppointmentService.getProvisionalAppointments(
							phoneNumber,num_furnitures,collectionPointID);
			assertNotNull(appointments);
			assertTrue(appointments.size() == 1);
			validAppointment(appointments);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}
	
	/**
	 * Get a provisional appointment for 2 days.
	 */
	@Test
	public void testGet2ProvisionalAppointments(){
		final String phoneNumber = "604300014";
		final int num_furnitures = 1;
		final int collectionPointID = 1; 
		try{
			List<ProvisionalAppointment> appointments =
					mAppointmentService.getProvisionalAppointments(
							phoneNumber,num_furnitures,collectionPointID);
			assertTrue(appointments.size() > 1);
			validAppointment(appointments);
		}
		catch(Exception e){
			fail(e.toString());
		}
	}	
	
	/**
	 * Get a provisional appointment, later confirm it.
	 *  Finally cancel this appointment.
	 */
	@Test
	public void testGetAndConfirmAppointment(){
		try{
			
			List<ProvisionalAppointment> appointments =
					mAppointmentService.getProvisionalAppointments(
							"604300014",5,2);
			assertTrue(appointments.size() > 1);
			validAppointment(appointments);
			ProvisionalAppointment appointment = appointments.get(0);
			// Add furnitures to collection request
			List<Furniture> furnitures = new ArrayList<Furniture>();
			furnitures.add(new Furniture(1,1));
			furnitures.add(new Furniture(2,1));
			CollectionRequest req =
					new CollectionRequest(appointment,furnitures);
			confirmProvisionalAppointment(req);
			
		}
		catch(Exception e){
			fail(e.toString());
		}		
	}
	

	private void confirmProvisionalAppointment(CollectionRequest appointment) 
			throws Exception{
		HttpResponse response = mAppointmentService.confirmAppointment(appointment);
		assertTrue(response.getStatusLine().getStatusCode() == 201);
	}

	
	
	private void validAppointment(List<ProvisionalAppointment> appointments){
		for(ProvisionalAppointment appointment : appointments){
			validAppointment(appointment);
		}
	}
	
	/**
	 * Validate if the appointment is correct, and all these fields are in correct format.
	 * @param appointment
	 */
	private void validAppointment(ProvisionalAppointment appointment){
		assertNotNull(appointment.getTelephone());
		assertTrue(appointment.getTelephone().charAt(0) == '6');
		assertTrue(appointment.getNumFurnitures() > 0);
		assertNotNull(appointment.getFch_collection());
		assertNotNull(appointment.getFch_request());
		assertNotNull(appointment.getCollectionPointId());
	}
}
