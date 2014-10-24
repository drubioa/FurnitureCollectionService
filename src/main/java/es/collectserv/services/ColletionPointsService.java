package es.collectserv.services;

import es.collectserv.clases.CollectionPoint;


/**
 * This class represent a Collection Points Service, this service find nearest collection 
 * point to the furniture collection point service.
 * @author Diego Rubio Abujas [dbiosag@gmail.com]
 *
 */
public interface ColletionPointsService {
	
	/**
	 * Receives a position of user's home and return the nearest 
	 * collection point.
	 * @param lat
	 * @param lng
	 * @param isRuralArea
	 * @return nearest collection point in JSON format.
	 */
	public CollectionPoint getCollectionPoint(
			Double lat,Double lng,Boolean isRuralArea);
	
}
