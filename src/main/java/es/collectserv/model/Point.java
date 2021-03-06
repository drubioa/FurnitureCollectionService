package es.collectserv.model;

public class Point {
	private double longitude,latitude;
	private String direction;
	
	public Point(double latitude,double longitude){
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Point(){
		
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String toString(){
		return "longitude: "+longitude+"\n"+
	"latitude: "+latitude+"\n"+
				"direction: "+direction;
	}
}
