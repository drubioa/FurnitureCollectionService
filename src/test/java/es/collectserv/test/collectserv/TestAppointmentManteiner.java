package es.collectserv.test.collectserv;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.DailyServices;
import es.collectserv.collrequest.RequestManagementSingletonImp;
import es.collectserv.model.ProvisionalAppointment;


@RunWith(JUnit4.class)
public class TestAppointmentManteiner{
	private static TestingRequestManagUtilities utilities = 
			new TestingRequestManagUtilities();
	private static RequestManagementSingletonImp mManagement;
	private final LocalDate TOMORROW = new LocalDate().plusDays(1);
	private final LocalDate TODAY = new LocalDate();
	private final LocalDate YESTERDAY = new LocalDate().minusDays(1);
	private DailyServices invalidServicesDay,validServicesDay;
	
	public TestAppointmentManteiner(){
		mManagement = RequestManagementSingletonImp.getInstance();
	}

	private List<DailyServices> generateValidServDay(int n) throws IOException{
		List<DailyServices> days = new ArrayList<DailyServices>();
		LocalDate TODAY = new LocalDate();
		for(int i = 1;i  <= n;i++){
			DailyServices  d = new DailyServices(TODAY.plusDays(i));
			days.add(d);
		}
		return days;
	}
	
	private List<DailyServices> generateInvalidServDay(int n) throws IOException{
		List<DailyServices> days = new ArrayList<DailyServices>();
		LocalDate TODAY = new LocalDate();
		for(int i = 1;i  <= n;i++){
			DailyServices  d = new DailyServices();
			d.setDate(TODAY.minusDays(i));
			days.add(d);
		}
		return days;
	}
	
	@Test
	public void testIsManagerRemoves1InvalidServDaysFor1AppointReq(){
		final String tel = "600010101";
		final int items = 1;
		final int collectionPoint = 1;
		List<DailyServices> days;
		try {
			days = generateValidServDay(2);
			days.addAll(generateInvalidServDay(1));
			mManagement.setListDaylyServices(days);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<ProvisionalAppointment> provisionalAppointments = 
					mManagement.getAppointmentToConfirm(tel, items, collectionPoint);
			assertNotNull(provisionalAppointments);
			assertTrue(provisionalAppointments.size() == 1);
			for(ProvisionalAppointment p : provisionalAppointments){
				assertTrue(p.getFch_collection().isAfter(TODAY));
			}
		} catch (Exception e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testIsManagerRemoves1InvalidServDaysFor2AppointReq(){
		final String tel = "600010101";
		final int items = 4 + 1;
		final int collectionPoint = 1;
		List<DailyServices> days;
		try {
			days = generateValidServDay(2);
			days.addAll(generateInvalidServDay(3));
			mManagement.setListDaylyServices(days);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<ProvisionalAppointment> provisionalAppointments = 
					mManagement.getAppointmentToConfirm(tel, items, collectionPoint);
			assertNotNull(provisionalAppointments);
			assertTrue(provisionalAppointments.size() == 2);
			for(ProvisionalAppointment p : provisionalAppointments){
				assertTrue(p.getFch_collection().isAfter(TODAY));
			}
		} catch (Exception e) {
			fail(e.toString());
		}		
	}
	
	@Test
	public void testIsManagerRemovesInvalidServDaysEmptyValidDays(){
		final String tel = "600010101";
		final int items =  12;
		final int collectionPoint = 1;
		List<DailyServices> days;
		try {
			days = generateValidServDay(0);
			days.addAll(generateInvalidServDay(10));
			mManagement.setListDaylyServices(days);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			List<ProvisionalAppointment> provisionalAppointments = 
					mManagement.getAppointmentToConfirm(tel, items, collectionPoint);
			assertNotNull(provisionalAppointments);
			assertTrue(provisionalAppointments.size() == 3);
			for(ProvisionalAppointment p : provisionalAppointments){
				assertTrue(p.getFch_collection().isAfter(TODAY));
			}
		} catch (Exception e) {
			fail(e.toString());
		}		
	}
}
