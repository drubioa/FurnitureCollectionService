package es.collectserv.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestCollectionPoint.class, TestDailyAppointmentService.class,
		TestMyBatis.class, TestRequestManagement.class, TestUserService.class })
public class AllTests {

}
