package es.collectserv.test.services;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.collectserv.collrequest.DailyServices;
import es.collectserv.collrequest.DailyServicesImp;

public class TestRemoveUnconfirmedAppointment {
	private DailyServices service_day;
	static final int SLEEP_TIME = 5000;
	
	@Before
	public void setUp() throws Exception {	
		service_day = new DailyServicesImp(nextDay(new Date()));
	}
	
	private Date nextDay(Date day){
		Calendar gc = Calendar.getInstance(); 
		gc.add(Calendar.DATE, 1);
		return gc.getTime();
	}

	@After
	public void tearDown() throws Exception {
		service_day = null;
	}

	@Test
	public void testAddNewProvAppointAndWaitSleepTime() throws InterruptedException {
		String phone = "600000000";
		assertTrue(!service_day.userGotPreviousRequest(phone));
		make1ExampleAppointment(phone);
		assertTrue(service_day.userGotPreviousRequest(phone));
		Thread.sleep(SLEEP_TIME*2);
		assertTrue(!service_day.userGotPreviousRequest(phone));
	}
	
	@Test
	public void testAdd24NewProvAppointmentAndWaitSleepTime() throws InterruptedException {
		String phone = "6000000";
		assertTrue(service_day.obtainRealizablePeticions() == 4);
		for(int i = 0;i < 24;i++){
			make1ExampleAppointment(phone+i);
		}
		assertTrue(service_day.obtainRealizablePeticions() == 0);
		Thread.sleep(SLEEP_TIME*3);
		for(int i = 0;i < 24;i++){
			assertTrue(!service_day.userGotPreviousRequest(phone+i));
		}
		assertTrue(service_day.obtainRealizablePeticions() == 4);
	}
	
	private void make1ExampleAppointment(String phone ){
		int num_furnitures = 1;
		int pointId = 1;
		try {
			assertNotNull(service_day.getAppointment(phone, num_furnitures, pointId));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

}
