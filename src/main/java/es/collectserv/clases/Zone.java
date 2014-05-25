package es.collectserv.clases;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Zone {
	private int id;
	private String name;
	private List<CollectionPoint> points;
	
	public Zone(){
		
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<CollectionPoint> getPoints() {
		return points;
	}
	public void setPoints(List<CollectionPoint> points) {
		this.points = points;
	}
	
	/**
	 * This function find the nearest collection point for deposit furnitures
	 * @param point where the user got his furnitures. This point must 
	 * @return the nearest collection point for deposit his furnitures-
	 *   this point must belong to this zone
	 * @throws Exception if the point isn´t inside the zone or collection point
	 * cannot be located.
	 */
	public CollectionPoint nearestCollectionPoint(Point point) 
			throws Exception{
		double distMin = 200; //Minimun distance to the collection point in meters
		CollectionPoint colectionPoint = null;
		for(int i = 0;i < points.size();i++){
			double dist = calculateDistance(point,points.get(i));
			if(dist <= distMin){
				distMin = dist;
				colectionPoint = points.get(i);
			}
		}
		if(colectionPoint == null){
			throw new Exception("Collection Point annot be located");
		}
		return colectionPoint;
	}
	
	/**
	 * This function returns distance from the point origin a to the collection
	 * point b.
	 * @param a
	 * @param b
	 * @return
	 */
	private double calculateDistance(Point pos_a,CollectionPoint pos_b){
		double earthRadius = 3958.75;
		double lng1 = pos_a.getLongitude(),
			   lng2 = pos_b.getLongitude(),
			   lat1 = pos_a.getLatitude(),
			   lat2 = pos_b.getLatitude();
		double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * 
	               Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;

	    int meterConversion = 1609;

	    return (float) (dist * meterConversion);
	}
}
