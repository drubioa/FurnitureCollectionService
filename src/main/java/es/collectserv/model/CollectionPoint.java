package es.collectserv.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CollectionPoint extends Point{
	private int pointId;
	private int zone;
	
	public CollectionPoint(){
		
	}
	
	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}
	
}
