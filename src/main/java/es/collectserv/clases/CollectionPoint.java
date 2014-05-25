package es.collectserv.clases;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CollectionPoint extends Point{
	private int id;

	private int zone;
	
	public CollectionPoint(){
		
	}
	
	public int getZone() {
		return zone;
	}

	public void setZone(int zone) {
		this.zone = zone;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
