package es.collectserv.test.collectserv;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.LocalDate;

import es.collectserv.model.ProvisionalAppointment;

public abstract class AppointmentValidator {
	/**
	 * Validate if the appointment is correct, and all these fields are in correct format.
	 * @param appointment
	 */
	public static void validAppointment(ProvisionalAppointment appointment){
		assertNotNull(appointment);
		assertNotNull(appointment.getTelephone());
		assertTrue(appointment.getTelephone().charAt(0) == '6'
				||appointment.getTelephone().charAt(0) == '9');
		assertTrue(appointment.getNumFurnitures() > 0);
		assertNotNull(appointment.getFch_collection());
		LocalDate collection_date = appointment.getFch_collection();
		assertNotNull(appointment.getFch_request());
		LocalDate request_date = appointment.getFch_request();
		assertTrue(collection_date.isAfter(request_date));
		assertTrue(appointment.getFch_request().equals(new LocalDate()));
		assertTrue(collection_date.isAfter(new LocalDate()));
		assertNotNull(appointment.getCollectionPointId());
	}
	
	public static void validAppointment(List<ProvisionalAppointment> appointments){
		for(ProvisionalAppointment appointment : appointments){
			AppointmentValidator.validAppointment(appointment);
		}
	}
}
