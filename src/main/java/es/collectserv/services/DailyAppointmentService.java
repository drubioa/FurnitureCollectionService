package es.collectserv.services;

import java.util.List;

import es.collectserv.converter.ProvisionalAppointmentConverter;
import es.collectserv.model.CollectionRequest;

/**
 * This service manage the Daily Appointments for Collection of Furnitures.
 * @author Diego Rubio Abujas
 *
 */
public interface DailyAppointmentService {
	
	/**
	 * 
	 * @param phone_number phone number of user
	 * @param itemsRequest number of items request
	 * @param collection_point_id id of point where the furniture will collected
	 * @return A list of Provisional Appointments (Must be confirmed by user later).
	 */
	public List<ProvisionalAppointmentConverter> getProvisionalAppintments(
			String phone_number,int itemsRequest,int collection_point_id);
	
	/**
	 * The user sends the collectionRequest and confirms the request. It is 
	 * saved in the system, to future collection.
	 * @param collectionRequest
	 */
	public void confirmCollectionRequest(
			CollectionRequest collectionRequest);
}
