package es.collectserv.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.clases.Furniture;
import es.collectserv.collrequest.CollectionRequest;
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
		assertFalse(management.userGotPreviosRequest("1414"));
	}
	
	/** 
	 * Se crea una solicitud pendiente de confirmar para 1 item 
	 **/
	@Test
	public void testGetAppointmentToConfirm1Item(){
		String phone = "612345678";
		int num_items = 1;
		int collectionPointId = 1;
		try {
			validAppointmentList(
					management.getAppointmentToConfirm(phone, num_items,collectionPointId));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	private void validAppointmentList(List<ProvisionalAppointment> appointments){
		assertNotNull(appointments);
		for(int i = 0;i < appointments.size();i++){
			validAppointment(appointments.get(i));
		}
	}
	
	private void validAppointment(ProvisionalAppointment apointmnet){
		assertNotNull(apointmnet.getTelephone());
		assertTrue(apointmnet.getTelephone().charAt(0) == '6');
		assertTrue(apointmnet.getNumFurnitures() > 0);
		assertNotNull(apointmnet.getFch_collection());
		assertNotNull(apointmnet.getFch_request());
		assertNotNull(apointmnet.getCollectionPointId());
	}
	
	/**
	 * Se crean solicitudes pendiente de confirmar para 5 item 
	 */
	@Test
	public void testGetAppointmentToConfirm5Item(){
		String phone = "612345678";
		int num_items = 5;
		int collectionPointId = 1;
		try {
			List<ProvisionalAppointment> list =
					management.getAppointmentToConfirm(phone, num_items,collectionPointId);
			validAppointmentList(list);
			assertTrue(list.size() > 1);
			assertTrue(list.get(0).getFch_collection() != list.get(1).getFch_collection());
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Se crea un solicitud pen diente de confirmar para 1 item y se confirma.
	 */
	@Test
	public void testGetAppointmentAndConfirm1Item(){
		String phone = "600000000";
		int num_items = 1;
		int collectionPointId = 1;
		List<Furniture> furnitures = new ArrayList<Furniture>();
		furnitures.add(new Furniture(1,1));
		try {
			// Se obtiene una solicitud pendiente de confirmar
			List<ProvisionalAppointment> list =
					management.getAppointmentToConfirm(phone, num_items,collectionPointId);
			assertTrue(list.size() == 1);
			validAppointmentList(list);
			// Se confirma la solicitud
			management.confirmProvisionalAppointmen(new CollectionRequest(list.get(0),furnitures));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	/**
	 * Se crea un solicitud pen diente de confirmar para 1 item y se confirma.
	 */
	@Test
	public void testGet2AppointmentsAndConfirm(){
		String phone = "600000000";
		int num_items = 5;
		int collectionPointId = 1;
		List<Furniture> furnitures1 = new ArrayList<Furniture>();
		furnitures1.add(new Furniture(1,2));
		try {
			// Se obtiene una solicitud pendiente de confirmar
			List<ProvisionalAppointment> list =
					management.getAppointmentToConfirm(phone, num_items,collectionPointId);
			assertTrue(list.size() > 1);
			validAppointmentList(list);
			// Se confirman las solicitudes
			management.confirmProvisionalAppointmen(
					new CollectionRequest(list.get(0),furnitures1));
			management.confirmProvisionalAppointmen(
						new CollectionRequest(list.get(1),furnitures1));
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
}
