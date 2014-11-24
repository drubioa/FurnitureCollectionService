package es.collectserv.test.collectserv;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.DailyServices;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.test.concurrent.PhoneNumberGenerator;

@RunWith(JUnit4.class)
public class TestDailyServices {
	private static DailyServices mDailyService;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private PhoneNumberGenerator mPhoneGenerator;
	
	public TestDailyServices() throws Exception{
		java.util.Date dt = new java.util.Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOne = dtOrg.plusDays(1);
		mDailyService = new DailyServices(dtPlusOne.toDate());
		mPhoneGenerator = new PhoneNumberGenerator();
	}
	
	@Before
	public void setUp(){
		mPhoneGenerator.resetValue();
	}

	@Test
	public void testObtainRealizablePeticions(){
		String phone = mPhoneGenerator.generate_phoneNumber();
		// Se comprueban peticiones diponibles
		try {
			assertTrue(mDailyService.obtainRealizablePeticions(phone) 
					== MAX_FURNIUTRES_PER_DAY_USER);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		// Se realiza peticion provisional
		try {
			mDailyService.getProvisionalAppointment(phone, MAX_FURNIUTRES_PER_DAY_USER, 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// Se comprueba que no pueden realizarse mas peticiones
		try {
			assertTrue(mDailyService.obtainRealizablePeticions(phone) == 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		// Se confirma la peticion provisional
		try {
			mDailyService.confirmProvisionalAppointment(phone);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}	
	
	@Test
	public void testObtainsAndRemovesProvisionalAppointment(){
		// Se realiza peticion provisional
		ProvisionalAppointment appointment = null;
		final String phone = mPhoneGenerator.generate_phoneNumber();
		try {
			appointment = 
					mDailyService.getProvisionalAppointment(phone, MAX_FURNIUTRES_PER_DAY_USER, 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// Se comprueba que no pueden realizarse mas peticiones
		try {
			assertTrue(mDailyService.obtainRealizablePeticions(phone) == 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		// Se remueve la peticion provisional
		assertNotNull(appointment);
		try {
			mDailyService.removeUnconfirmedAppointment(appointment);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
		// Se comprueba que pueden volver a realizar peticiones.
		try {
			assertTrue(mDailyService.obtainRealizablePeticions(phone) == MAX_FURNIUTRES_PER_DAY_USER);
		} catch (InterruptedException e) {
			e.printStackTrace();
			fail();
		}
	}
}
