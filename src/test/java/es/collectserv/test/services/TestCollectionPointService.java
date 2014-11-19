package es.collectserv.test.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import es.collectserv.conector.CollectionPointServiceConector;
import es.collectserv.conector.CollectionPointServiceConectorImp;
import es.collectserv.model.CollectionPoint;

@RunWith(JUnit4.class)
public class TestCollectionPointService {

	CollectionPointServiceConector conector;
	
	@Before
	public void setUp(){
		conector = new CollectionPointServiceConectorImp();		
	}
	
	/**
	 * Try to get the nearest collection point from valid urban location.
	 */
	@Test
	public void testGetValidUrbanCollectionPoint(){
		final Double lng = -6.193095;
		final Double lat = 36.536233;
		final boolean isRuralArea = false;
		try {
			CollectionPoint point = 
					conector.getNearestCollectionPoint(lng,lat,isRuralArea);
			assertNotNull(point);
			// Compare with correspondient collection point.
			assertTrue(point.getLongitude() == -6.193095 
					&& point.getLatitude() == 36.536233);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	/**
	 * Test with invalid location longitude 0 and latitude 0.
	 */
	@Test
	public void testInvalidLocation(){
		final Double lng = 0.;
		final Double lat = 0.;
		final boolean isRuralArea = false;
		try {
			conector.getNearestCollectionPoint(lng,lat,isRuralArea);
		} catch (Exception e) {
			if(e.getMessage().contains("Failed : HTTP error code : 400")){
				assertTrue(true);
			}
			else{
				fail(e.getMessage());
			}
		}		
	}
	
}
