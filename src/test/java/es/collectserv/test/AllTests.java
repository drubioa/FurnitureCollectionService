package es.collectserv.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.collectserv.test.mybatis.TestMyBatis;
import es.collectserv.test.services.TestCollectionPoint;
import es.collectserv.test.services.TestDailyAppointmentService;
import es.collectserv.test.services.TestRequestManagement;
import es.collectserv.test.services.TestUserService;

@RunWith(Suite.class)
@SuiteClasses({ TestCollectionPoint.class, TestDailyAppointmentService.class,
		TestMyBatis.class, TestRequestManagement.class, TestUserService.class })
public class AllTests {

}
