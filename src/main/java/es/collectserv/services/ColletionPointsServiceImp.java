package es.collectserv.services;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import es.collectserv.clases.CollectionPoint;
import es.collectserv.clases.Point;
import es.collectserv.clases.Area;

@Path("/point")
public class ColletionPointsServiceImp implements ColletionPointsService{
	private Area rural,urbana;
	
	public ColletionPointsServiceImp(){
		String configFile = "mybatis-config.xml";
		InputStream is;
		SqlSessionFactory sqlSesionFac;
		try {
			is = Resources.getResourceAsStream(configFile);
			sqlSesionFac = new SqlSessionFactoryBuilder().build(is);
			SqlSession session = sqlSesionFac.openSession();
			List<CollectionPoint> puntos = 
					session.selectList("CollectionPointMapper.selectUrbanAreaPoints");
			urbana = new Area(puntos);
			puntos = session.selectList("CollectionPointMapper.selectRuralPoints");
			rural = new Area(puntos);
			session.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CollectionPoint getCollectionPoint(
			@QueryParam("lat") Double lat,
			@QueryParam("lng") Double lng,
			@QueryParam("isRuralArea") Boolean isRuralArea){
		Point point;
		Area zone;
		if(isRuralArea == null){
			System.out.print("isRuralArea is null");
		}
		try{
			point = new Point(lat,lng);
			if(isRuralArea){
				zone = rural;
			}
			else{
				zone = urbana;			
			}
			if(zone == null){
				throw new NullPointerException("zone is null");
			}
			CollectionPoint collectPoint = zone.nearestCollectionPoint(point);
			if(collectPoint == null){
				System.out.print("collectPoint is null");
				throw new WebApplicationException(Response.Status.NOT_FOUND);
			}
			return collectPoint;
		}catch(Exception e){
			System.out.print("ERROR: "+e.toString());
			throw new WebApplicationException(Response.Status.BAD_REQUEST);
		}
	}
}
