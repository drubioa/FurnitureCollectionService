package es.collectserv.test;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.ProvisionalAppointment;
import es.collectserv.collrequest.RequestManagement;
import es.collectserv.collrequest.RequestManagementimp;

@RunWith(JUnit4.class)
public class TestRequestManagement {
	RequestManagement management;
	
	public TestRequestManagement(){
		this.management = new RequestManagementimp();
	}
	
	/**
	 * Se compueba que un nuevo usuario no tiene anteriores solicitudes pendientes 
	 * de confirmar
	 */
	@Test
	public void testUserIsNotGotPreviosReq(){
		assertFalse(management.userGotPreviosRequest("600000000"));
	}
	
	/** Se crea una solicitud previa **/
	@Test
	public void tesGetAppointmentToConfirm1Item(){
		String phone = "612345678";
		int num_items = 1;
		try {
			List<ProvisionalAppointment> list =
					management.getAppointmentToConfirm(phone, num_items);
			assertNotNull(list);
			assertTrue(list.size() == 1);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void tesGetAppointmentToConfirm5Item(){
		String phone = "612345678";
		int num_items = 5;
		try {
			List<ProvisionalAppointment> list =
					management.getAppointmentToConfirm(phone, num_items);
			assertNotNull(list);
			assertTrue(list.size() >= 2);
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
}
