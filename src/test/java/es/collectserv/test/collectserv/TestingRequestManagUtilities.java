package es.collectserv.test.collectserv;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.ProvisionalAppointment;

public class TestingRequestManagUtilities {
	
	public TestingRequestManagUtilities(){
		
	}
	
	/**
	 * Se comprueba que todos los campos de una cita no sean nulos 
	 * y tengan un formato valido.
	 * @param apointmnet
	 */
	public void validAppointment(ProvisionalAppointment apointmnet){
		assertNotNull(apointmnet.getTelephone());
		assertTrue(apointmnet.getTelephone().charAt(0) == '6');
		assertTrue(apointmnet.getNumFurnitures() > 0);
		assertNotNull(apointmnet.getFch_collection());
		assertNotNull(apointmnet.getFch_request());
		assertNotNull(apointmnet.getCollectionPointId());

	}

	/**
	 * Se genera una solicitud de recogida para la solicitud provisional creando
	 * 'num_Furnitures' muebles de ejemplo.
	 * @param a
	 * @param num_Furnitures
	 * @return
	 * @throws Exception
	 */
	public CollectionRequest createExampleCollectionRequest(ProvisionalAppointment a) throws Exception{
		List<Furniture> furnitures = new ArrayList<Furniture>();
		for(int i = 0;i < a.getNumFurnitures();i++){
			furnitures.add(new Furniture(1+i,1));
		}
		return new CollectionRequest(a,furnitures);
	}
	
}
