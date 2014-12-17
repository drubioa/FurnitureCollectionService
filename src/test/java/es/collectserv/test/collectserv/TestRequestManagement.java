package es.collectserv.test.collectserv;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.RequestManagementSingletonImp;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;
import es.collectserv.test.concurrent.PhoneNumberGenerator;

@RunWith(JUnit4.class)
public class TestRequestManagement {
	private static TestingRequestManagUtilities utilities = 
			new TestingRequestManagUtilities();
	private static RequestManagementSingletonImp mManagement;
	private static final PhoneNumberGenerator mNumberGenerator 
		= new PhoneNumberGenerator();;
	
	public TestRequestManagement(){
		mManagement = RequestManagementSingletonImp.getInstance();
	}
	
	/**
	* Se compueba que un nuevo usuario no tiene anteriores solicitudes pendientes 
	* de confirmar
	* @throws IOException 
	*/
	@Test
	public void testUserNOTGotPreviosRequest() throws IOException{
		try {
			String phone = "611123456";
			assertFalse(mManagement.userGotPreviosRequest(phone));
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testGet1ProvisionalAppointment(){
		String phone = mNumberGenerator.generate_phoneNumber();
		try {
			List<ProvisionalAppointment> appointments = 
					mManagement.getAppointmentToConfirm(phone, 1, 1);
			assertNotNull(appointments);
			assertTrue(appointments.size() == 1);
			ProvisionalAppointment appointmet = appointments.get(0);
			assertTrue(appointmet.getTelephone() == phone);
			utilities.validAppointment(appointmet);
			assertTrue(mManagement.userGotPreviosRequest(phone));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}
	
	/**
	 * Se crea una solicitud provisional y posterioermente se confirma. 
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testGet1ProvisionalAppointmentAndConfirm() throws IOException{
		String phone =  mNumberGenerator.generate_phoneNumber();
		SqlConector connector = new SqlConectorImp();
		connector.addNewUser(new User("anonymous",phone));
		try {
			List<ProvisionalAppointment> appointments = 
					mManagement.getAppointmentToConfirm(phone, 4, 1);
			assertNotNull(appointments);
			assertTrue(appointments.size() == 1);
			ProvisionalAppointment appointmet = appointments.get(0);
			assertTrue(appointmet.getTelephone() == phone);
			utilities.validAppointment(appointmet);
			assertTrue(mManagement.userGotPreviosRequest(phone));
			CollectionRequest collectionResquest = 
					utilities.createExampleCollectionRequest(appointmet);
			assertTrue(mManagement.userGotPreviosRequest(phone));
			mManagement.confirmProvisionalAppointment(collectionResquest);
			assertTrue(mManagement.userGotPreviosRequest(phone));
			mManagement.cancelPeendingRequest(phone);
			assertFalse(mManagement.userGotPreviosRequest(phone));
		} catch (Exception e) {
			connector.deleteUser(phone);
			e.printStackTrace();
			fail();
		}
		connector.deleteUser(phone);
	}
	
	/**
	 * Se crea una solicitud provisional y posterioermente se confirma. 
	 * @throws IOException 
	 */
	@Test
	public void testGet4ProvisionalAppointmentAndConfirm() throws IOException{
		String phone =  mNumberGenerator.generate_phoneNumber();
		SqlConector connector = new SqlConectorImp();
		connector.addNewUser(new User("anonymous",phone));
		try {
			List<ProvisionalAppointment> appointments = 
					mManagement.getAppointmentToConfirm(phone, 12, 1);
			assertNotNull(appointments);
			assertTrue(appointments.size() == 3); // 12 / 4 = 3
			for(ProvisionalAppointment appointmet : appointments){
				assertTrue(appointmet.getTelephone() == phone);	
				utilities.validAppointment(appointmet);
				assertTrue(appointmet.getNumFurnitures() == 4);
				CollectionRequest collectionResquest = 
						utilities.createExampleCollectionRequest(appointmet);
				mManagement.confirmProvisionalAppointment(collectionResquest);
			}
			assertTrue(mManagement.userGotPreviosRequest(phone));
			mManagement.cancelPeendingRequest(phone);
			assertFalse(mManagement.userGotPreviosRequest(phone));
		} catch (Exception e) {
			connector.deleteUser(phone);
			e.printStackTrace();
			fail();
		}
		connector.deleteUser(phone);
	}
	
	/**
	 * Se prueba que el sistema no permite a un usuario con una solicitud
	 * de recogida confirmada, realizar una nueva solicitud.
	 * @throws IOException 
	 */
	@Test
	public void testGetAppointmentWhenUserGotPrevConfirmAppointment() throws IOException{
		String phone =  mNumberGenerator.generate_phoneNumber();
		SqlConector connector = new SqlConectorImp();
		connector.addNewUser(new User("anonymous",phone));
		try {
			List<ProvisionalAppointment> appointments = 
					mManagement.getAppointmentToConfirm(phone, 12, 1);
			assertNotNull(appointments);
			assertTrue(appointments.size() == 3); // 12 / 4 = 3
			for(ProvisionalAppointment appointmet : appointments){
				assertTrue(appointmet.getTelephone() == phone);	
				utilities.validAppointment(appointmet);
				assertTrue(appointmet.getNumFurnitures() == 4);
				CollectionRequest collectionResquest = 
						utilities.createExampleCollectionRequest(appointmet);
				mManagement.confirmProvisionalAppointment(collectionResquest);
			}
			assertTrue(mManagement.userGotPreviosRequest(phone));
			try{
				mManagement.getAppointmentToConfirm(phone, 12, 1);
			}catch(IllegalArgumentException e){
				assertTrue(true);
			}finally{
				mManagement.cancelPeendingRequest(phone);
				assertFalse(mManagement.userGotPreviosRequest(phone));
			}
		} catch (Exception e) {
			connector.deleteUser(phone);
			e.printStackTrace();
			fail();
		}
		connector.deleteUser(phone);
	}

}
