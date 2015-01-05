package es.collectserv.services;

import java.util.ArrayList;

import es.collectserv.converter.CollectionRequestConverter;

/**
 * This clas represent the service of consult and remove collection request.
 * @author Diego Rubio Abujas
 *
 */
public interface CollectionRequestService {
	
	/**
	 * Obtiene todas las solicitudes de recogida confirmadas de un usuario.
	 * @param phone
	 * @return
	 */
	public ArrayList<CollectionRequestConverter> getCollectionRequest(String phone);

}
