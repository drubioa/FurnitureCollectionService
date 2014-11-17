package es.collectserv.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement
public class CollectionRequest extends Request{
	private int id;
	private List<Furniture> furnitures;
	@XmlTransient
	private CollectionPoint collectionPoint;
	
	public CollectionRequest(){
	
	}
	
	public CollectionRequest(ProvisionalAppointment appointment,List<Furniture> furnitures) throws Exception{
		super(appointment.getNumFurnitures(),
				appointment.getTelephone(),
				appointment.getCollectionPointId(),
				appointment.getFch_collection());
		this.furnitures = furnitures;
	}
	
	public List<Furniture> getFurnitures() {
		return furnitures;
	}

	public void setFurnitures(List<Furniture> furnitures) {
		this.furnitures = furnitures;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CollectionPoint getCollectionPoint() {
		return collectionPoint;
	}

	public void setCollectionPoint(CollectionPoint collectionPoint) {
		this.collectionPoint = collectionPoint;
		super.setCollectionPointId(collectionPoint.getPointId());
	}

}
