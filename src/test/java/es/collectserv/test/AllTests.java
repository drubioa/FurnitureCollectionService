package es.collectserv.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import es.collectserv.test.mybatis.TestArea;
import es.collectserv.test.mybatis.TestCollectionDays;
import es.collectserv.test.mybatis.TestCollectionRequest;
import es.collectserv.test.mybatis.TestFurnituresMapper;
import es.collectserv.test.mybatis.TestUserMapper;
import es.collectserv.test.services.TestCollectionPoint;
import es.collectserv.test.services.TestDailyAppointmentService;
import es.collectserv.test.services.TestRequestManagement;
import es.collectserv.test.services.TestUserService;

@RunWith(Suite.class)
@SuiteClasses({ TestCollectionPoint.class, TestDailyAppointmentService.class,
		TestArea.class, TestCollectionRequest.class,TestCollectionDays.class,
		TestFurnituresMapper.class, TestUserMapper.class,
		TestRequestManagement.class, TestUserService.class })
public class AllTests {

}