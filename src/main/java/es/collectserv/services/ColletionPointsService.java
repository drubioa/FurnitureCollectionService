package es.collectserv.services;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.ibatis.session.SqlSession;

import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.Point;
import es.collectserv.clases.Zone;
import es.collectserv.factories.SimpleMyBatisSesFactory;

@Path("/point")
public class ColletionPointsService {

	public ColletionPointsService(){
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CollectionPoint getCollectionPoint(
			@QueryParam("lat") Double lat,
			@QueryParam("lng") Double lng,
			@QueryParam("zone") Integer zoneId){
		Point point;
		Zone zone;
		try{
			point = new Point(lat,lng);
			// Try to mapper Zone 
			try{
				SqlSession session = 
						new SimpleMyBatisSesFactory().getOpenSqlSesion();
				zone = session.selectOne("ZoneMapper.selectZone", 
						zoneId);
				session.close();
			}catch(Exception e){
				throw new WebApplicationException( 
						Response.Status.INTERNAL_SERVER_ERROR);
			}
			// Find the nearest collect point of this zone
			CollectionPoint collectPoint = zone.nearestCollectionPoint(point);
			if(collectPoint == null){
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
			return collectPoint;
		}catch(Exception e){
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}
