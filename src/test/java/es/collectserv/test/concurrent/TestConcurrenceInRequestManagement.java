package es.collectserv.test.concurrent;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.collrequest.RequestManagementSingletonImp;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;
import es.collectserv.sqlconector.SqlConector;
import es.collectserv.sqlconector.SqlConectorImp;
import es.collectserv.test.collectserv.TestingRequestManagUtilities;

@RunWith(JUnit4.class)
public class TestConcurrenceInRequestManagement {
	public static RequestManagementSingletonImp mManagement;
	private static PhoneNumberGenerator mPhoneGenerator;
	private static TestingRequestManagUtilities utilities;
	private static final int MAX_FUNRITNURES_PER_DAY = 24;
	private static final int MAX_FURNIUTRES_PER_DAY_USER = 4;
	
	public TestConcurrenceInRequestManagement(){
		mPhoneGenerator = new PhoneNumberGenerator();
		utilities = new TestingRequestManagUtilities();
	}
	
	@Before
	public void setUp(){
		mManagement = RequestManagementSingletonImp.getInstance();
		mPhoneGenerator.resetValue();
	}
	
	/**
	 * Hilo empleado para testear la concurrencia,
	 * @author diegorubio
	 *
	 */
	public class ProvisionalAppointmentGetter extends Thread{
		private String mPhone;
		private int mNum_Furnitures;
		public boolean sucefull;
		public int mTotalFurnituresCollectionOrder;
		
		public ProvisionalAppointmentGetter(String phone,int num_furnitures){
			mPhone = phone;
			mNum_Furnitures = num_furnitures;
			sucefull = false;
		}
		
		public void setTelephone(String phone_number){
			mPhone = phone_number;
		}
		
		@Override
		public void run(){
			SqlConector connector = new SqlConectorImp();
			try {
				connector.addNewUser(new User("anonymous",mPhone));
				assertTrue(!(mManagement.userGotPreviosRequest(mPhone)));
				List<ProvisionalAppointment > appointments = 
						mManagement.getAppointmentToConfirm(mPhone, mNum_Furnitures, 1);
				assertTrue(mManagement.userGotPreviosRequest(mPhone));
				for(ProvisionalAppointment a :  appointments){
					assertTrue(a.getTelephone() == mPhone);	
					utilities.validAppointment(a);
					CollectionRequest collectionResquest = 
							utilities.createExampleCollectionRequest(a);
					mManagement.confirmProvisionalAppointment(collectionResquest);
					mTotalFurnituresCollectionOrder += collectionResquest.getNumFurnitures();
				}
				assertTrue(mManagement.userGotPreviosRequest(mPhone));
				mManagement.cancelPeendingRequest(mPhone);
				connector.deleteUser(mPhone);
				sucefull = true;
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			} catch (Exception e) {
				e.printStackTrace();
				fail();
			}
		}
	}
	
	@Test
	public void test1ProvisionalAppointmentGettersOf1Item() 
			throws InterruptedException{
		final int N = 1;
		final int items = 1;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test2ProvisionalAppointmentGettersOf1Item() 
			throws InterruptedException{
		final int N = 2;
		final int items = 1;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test1UserAnd3Days() 
			throws InterruptedException{
		final int N = 1;
		final int items = MAX_FURNIUTRES_PER_DAY_USER * 3;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test2DaysOfRequestOf1Item() 
			throws InterruptedException{
		final int N = MAX_FUNRITNURES_PER_DAY * 2;
		final int items = 1;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test2DaysOfRequestOfMaxNumOfItem() 
			throws InterruptedException{
		final int N = (MAX_FUNRITNURES_PER_DAY / MAX_FURNIUTRES_PER_DAY_USER);
		final int items = MAX_FURNIUTRES_PER_DAY_USER * 2; 
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test3DaysOfRequestOf4Users() 
			throws InterruptedException{
		final int N = 4;
		final int items = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1 ;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test3DaysOfRequestOf10Users() 
			throws InterruptedException{
		final int N = 10;
		final int items = MAX_FURNIUTRES_PER_DAY_USER * 2 + 1 ;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	@Test
	public void test10UsersRequest6Items() 
			throws InterruptedException{
		final int N = 10;
		final int items = 6 ;
		inicProvisionalAppointmentGetters(N,items);
	}
	
	private void inicProvisionalAppointmentGetters(int n,int items) 
			throws InterruptedException{
		int totalFurnituresToCollect = 0;
		List<ProvisionalAppointmentGetter> solicitors = 
				new ArrayList<ProvisionalAppointmentGetter>();
		for(int i = 0;i < n;i++){
			String phone = mPhoneGenerator.generate_phoneNumber();
			ProvisionalAppointmentGetter p = 
					new ProvisionalAppointmentGetter(phone,items);
			p.start();
			solicitors.add(p);
		}
		for(ProvisionalAppointmentGetter p : solicitors ){
			p.join();
			assertTrue(p.sucefull);
			totalFurnituresToCollect += p.mNum_Furnitures;
		}
		// Test if all request contains the correspondent furnitures to collect.
		assertTrue(items * n == totalFurnituresToCollect);
	}
	
}
