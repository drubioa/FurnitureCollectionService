package es.collectserv.test.collectserv;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.collectserv.collrequest.DailyServices;

/**
 * Conjunto de pruebas para testear la funcionalidad de que el servidor elimine de 
 * forma autom??tica las peticiones de recogida no confirmadas tras un SLEEP_TIME.
 * @author Diego Rubio Abujas
 *
 */
public class TestRemoveUnconfirmedAppointment {
	private DailyServices service_day;
	static final int SLEEP_TIME = 5000;
	
	@Before
	public void setUp() throws Exception {	
		LocalDate date = new LocalDate();
		service_day = new DailyServices(date.plusDays(1));
	}
	
	/**
	 * A??ade una peticion de recogida, espera el sleep time y comprueba si la solicitud
	 * se ha eliminado automaticamente del sistema.
	 */
	@Test
	public void testAddNewProvAppointAndWaitSleepTime() {
		String phone = "600000000";
		try {
			assertTrue(!service_day.checkIfuserGotPreviousRequest(phone));
			make1ExampleAppointment(phone);
			assertTrue(service_day.checkIfuserGotPreviousRequest(phone));
			Thread.sleep(SLEEP_TIME*2);
			assertTrue(!service_day.checkIfuserGotPreviousRequest(phone));
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}

	}
	
	@Test
	public void testAdd24NewProvAppointmentAndWaitSleepTime(){
		String phone = "6000000";
		try {
			assertTrue(service_day.obtainRealizablePeticions(phone) == 4);
			for(int i = 0;i < 24;i++){
				make1ExampleAppointment(phone+i);
			}
			assertTrue(service_day.obtainRealizablePeticions(phone) == 0);
			Thread.sleep(SLEEP_TIME*3);
			for(int i = 0;i < 24;i++){
				assertTrue(!service_day.checkIfuserGotPreviousRequest(phone+i));
			}
			assertTrue(service_day.obtainRealizablePeticions(phone) == 4);
		} catch (InterruptedException e) {
			fail(e.getMessage());
		}

	}
	
	@After
	public void tearDown() throws Exception {
		service_day = null;
	}

	private void make1ExampleAppointment(String phone ){
		int num_furnitures = 1;
		int pointId = 1;
		try {
			assertNotNull(service_day.getProvisionalAppointment(phone, num_furnitures, pointId));
		} catch (Exception e) {
			fail(e.toString());
		}
	}

}
