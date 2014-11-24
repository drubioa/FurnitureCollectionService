package es.collectserv.test.concurrent;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.DailyServices;
import es.collectserv.model.ProvisionalAppointment;

@RunWith(JUnit4.class)
public class TestConcurrenceInDailyServices {
	private static DailyServices mDailyService;
	private static final int MAX_FUNRITNURES_PER_DAY = 24;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	private PhoneNumberGenerator phoneGenerator;
	
	public TestConcurrenceInDailyServices() throws Exception{
		java.util.Date dt = new java.util.Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOne = dtOrg.plusDays(1);
		mDailyService = new DailyServices(dtPlusOne.toDate());
		phoneGenerator = new PhoneNumberGenerator();
	}
	
	/**
	 * Se reinicia el valor del generador de numeros de telefono aleatorios.
	 */
	@Before
	public void setUp(){
		phoneGenerator.resetValue();
	}
	
	/**
	 * Clase solicitador de citas.
	 * @author Diego Rubio Abujas.
	 *
	 */
	public class AppointmentSolicitor extends Thread{
		private String mPhone;
		private int mNumFurnitures,mPointId;
		public boolean gotAppointment;
		
		public AppointmentSolicitor(String phone,int numFurnitures,int PointId){
			mPhone = phone;
			mNumFurnitures = numFurnitures;
			mPointId = PointId;
			gotAppointment = false;
		}
		
		@Override
		public void run(){
			try {
				ProvisionalAppointment a = mDailyService.getProvisionalAppointment(
						mPhone, mNumFurnitures, mPointId);
				assertNotNull(a);
				assertTrue(a.getTelephone().equals(mPhone));
				gotAppointment = true;
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}
		
	}
	

	/**
	 * Clase solicitador de citas que tambien las confirma.
	 * @author Diego Rubio Abujas.
	 *
	 */
	public class AppointmentGetAndConfirmSolicitor extends Thread{
		private String mPhone;
		private int mNumFurnitures,mPointId;
		public boolean success;
		
		public AppointmentGetAndConfirmSolicitor(String phone,int numFurnitures,int PointId){
			mPhone = phone;
			mNumFurnitures = numFurnitures;
			mPointId = PointId;
			success = false;
		}
		
		@Override
		public void run(){
			try {
				ProvisionalAppointment a = mDailyService.getProvisionalAppointment(
						mPhone, mNumFurnitures, mPointId);
				assertNotNull(a);
				assertTrue(a.getTelephone().equals(mPhone));
				mDailyService.confirmProvisionalAppointment(mPhone);
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}
		
	}
	
	/**
	 * Clase obtenedora de numero de solicitudes diarias disponibles
	 * @author Diego Rubio Abujas;
	 *
	 */
	public class obtainRealizablePeticionsSolicitor extends Thread{
		private int n;
		private int mValorEsperado;
		private String mPhone;
		
		public obtainRealizablePeticionsSolicitor(String phone,int valorEsperado){
			mValorEsperado = valorEsperado;
			mPhone = phone;
		}
		
		public boolean valorEsperado(){
			return n == mValorEsperado;
		}
		
		@Override
		public void run(){
			try {
				n  = mDailyService.obtainRealizablePeticions(mPhone);
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}
		
	}

	/**
	 * Se teste si al solicitar una cita provisional se responde correctamente a
	 * dicha solicitud
	 * 
	 */
	@Test
	public void testGet1Appointment(){
		try {
			AppointmentSolicitor t1 = 
					new AppointmentSolicitor(phoneGenerator.generate_phoneNumber(),1,1);
			t1.start();
			t1.join();
			assertTrue(t1.gotAppointment);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	/**
	 * Se teste si al solicitar dos citas provisionales se responde correctamente a
	 * dicha solicitud
	 */
	@Test
	public void testGet2Appointment(){
		try {
			AppointmentSolicitor t1 = 
					new AppointmentSolicitor(phoneGenerator.generate_phoneNumber(),1,1);
			AppointmentSolicitor t2 = 
					new AppointmentSolicitor(phoneGenerator.generate_phoneNumber(),1,1);
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			assertTrue(t1.gotAppointment);
			assertTrue(t2.gotAppointment);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	/**
	 * Se realizan el maximo numero de solicitudes permitidas por un dia.
	 */
	@Test
	public void testGetAppointmentOfMAX_FUNRITNURES_PER_DAY(){
		try {
			List<AppointmentSolicitor> solicitors = 
					new ArrayList<AppointmentSolicitor>();
			assertTrue(mDailyService.obtainRealizablePeticions
					(phoneGenerator.generate_phoneNumber()) == 
						MAX_FURNIUTRES_PER_DAY_USER);
			for(int i = 0; i < MAX_FUNRITNURES_PER_DAY;i++){
				AppointmentSolicitor t = 
						new AppointmentSolicitor(phoneGenerator.generate_phoneNumber()
						,1,1);
				solicitors.add(t);
				t.start();
			}
			for(AppointmentSolicitor t : solicitors){
				t.join();
				assertTrue(t.gotAppointment);
			}	
			/* Al haber realizado todas las solicitudes deberia de indicar a un nuevo telefono
			que no hay mas peticiones disponibles para dicho dia */
			assertTrue(mDailyService.obtainRealizablePeticions
					(phoneGenerator.generate_phoneNumber()) == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}	
	}
	
	/**
	 * Se comprueba que la clase DailyServices lanza correctamente una excepcion
	 * si se solicita un mayor numero de peticiones de las permitidas al dia.
	 */
	@Test
	public void testGetAppointmentOfMoreThanMAX_FUNRITNURES_PER_DAYDay(){
		try {
			int N = MAX_FUNRITNURES_PER_DAY + 1; // numero de solicitudes
			List<AppointmentSolicitor> solicitors = 
					new ArrayList<AppointmentSolicitor>();
			for(int i = 0; i < N;i ++ ){
				AppointmentSolicitor a = 
						new AppointmentSolicitor(phoneGenerator.generate_phoneNumber()
						,1,1);
				solicitors.add(a);
				a.start();
			}
			for(AppointmentSolicitor a : solicitors){
				a.join();
			}			
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Resquest is not realizable"));
		}	
	}
	
	/**
	 * Se comprueba que la clase DailyServices lanza una excepcion al solicitar mas
	 * muebles de los permitidos por usuario y dia.
	 */
	@Test
	public void testGetAppointmentoOfMoreThanMaxFurnitures(){
		try {
			AppointmentSolicitor t1 = 
					new AppointmentSolicitor(phoneGenerator.generate_phoneNumber(),
							MAX_FURNIUTRES_PER_DAY_USER + 1,1);
			t1.start();
			t1.join();
			assertFalse(t1.gotAppointment);
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Resquest is not realizable"));
		}		
	}
	
	/**
	 * Se realizan peticiones con el maximo numero de muebles permitidos por usuario
	 * hasta agotar el cupo de citas provisionales diarias.
	 */
	@Test
	public void testGetAllAppointmentoOfMaxFurnitures(){
		try {
			int N = MAX_FUNRITNURES_PER_DAY / MAX_FURNIUTRES_PER_DAY_USER; 
			List<AppointmentSolicitor> solicitors = 
					new ArrayList<AppointmentSolicitor>();
			for(int i = 0; i < N;i++){
				AppointmentSolicitor t = new AppointmentSolicitor(phoneGenerator.generate_phoneNumber()
						,MAX_FURNIUTRES_PER_DAY_USER,1);
				solicitors.add(t);
				t.start();
			}
			for(AppointmentSolicitor a : solicitors){
				a.join();
				assertTrue(a.gotAppointment);
			}
			/* Al haber realizado todas las solicitudes deberia de indicar a un nuevo telefono
			que no hay mas peticiones disponibles para dicho dia */
			assertTrue(mDailyService.obtainRealizablePeticions
					(phoneGenerator.generate_phoneNumber()) == 0);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}	
	}
	
	private void runAndTestObtainRealizablePeticionsSolicitor(final int num_threads,
			final int N){
		List<obtainRealizablePeticionsSolicitor> solicitors = 
				new ArrayList<obtainRealizablePeticionsSolicitor>();
		for(int i = 0;i < num_threads;i++){
			String phone = phoneGenerator.generate_phoneNumber();
			obtainRealizablePeticionsSolicitor t = 
					new obtainRealizablePeticionsSolicitor(phone,N);
			solicitors.add(t);
			t.start();
		}
		for(obtainRealizablePeticionsSolicitor t : solicitors){
			try {
				t.join();
				assertTrue(t.valorEsperado());
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail();
			}
		}	
	}
	
	@Test 
	public void getAndConfirmProvisionalAppointment(){
		try {
			int N = MAX_FUNRITNURES_PER_DAY; // numero de solicitudes
			List<AppointmentGetAndConfirmSolicitor> solicitors = 
					new ArrayList<AppointmentGetAndConfirmSolicitor>();
			for(int i = 0; i < N;i ++ ){
				AppointmentGetAndConfirmSolicitor a = new AppointmentGetAndConfirmSolicitor(
						phoneGenerator.generate_phoneNumber(),1,1);
				solicitors.add(a);
				a.start();
			}
			for(AppointmentGetAndConfirmSolicitor a : solicitors){
				a.join();
				assertTrue(a.success);
			}			
		} catch (Exception e) {
			assertTrue(e.getMessage().contains("Resquest is not realizable"));
		}			
	}
	
	@Test
	public void testObtainRealizablePeticionsFor1000Req(){
		int N = 1000;
		runAndTestObtainRealizablePeticionsSolicitor(N,MAX_FURNIUTRES_PER_DAY_USER);
		testGetAllAppointmentoOfMaxFurnitures();
		runAndTestObtainRealizablePeticionsSolicitor(N,0);
	}
}
