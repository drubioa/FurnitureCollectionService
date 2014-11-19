package es.collectserv.conector;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpException;
import org.codehaus.jettison.json.JSONException;

import es.collectserv.model.CollectionPoint;

public interface CollectionPointServiceConector {
	
	/**
	 * 
	 * @param lng
	 * @param lat
	 * @param isRuralArea
	 * @return nearest collection point to the request.
	 * @throws URISyntaxException
	 * @throws JSONException
	 * @throws HttpException
	 * @throws IOException
	 */	
	public CollectionPoint getNearestCollectionPoint(final Double lng,
			final Double lat,final boolean isRuralArea) 
					throws URISyntaxException, JSONException, 
					HttpException, IOException;
}
