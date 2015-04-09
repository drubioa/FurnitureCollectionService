package es.collectserv.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.LocalDate;

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
				appointment.getFch_collection(),
				appointment.getFch_request());
		super.setNumFurnitures(furnitures.size());
		this.furnitures = furnitures;
	}
	
	public List<Furniture> getFurnitures() {
		return furnitures;
	}

	public void setFurnitures(List<Furniture> furnitures) {
		super.setNumFurnitures(furnitures.size());
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
	

	public int getNumFurnitures(){
		int totalFurnitures = 0;
		for(Furniture furniture : furnitures){
			totalFurnitures = totalFurnitures + furniture.getCantidad();
		}
		return totalFurnitures;
	}
	
	

	/**
	 * Comprueba que todos los campos estan correctamente.
	 * @return
	 */
	public boolean checkCorrectRequest(){
		boolean isCorrect = true;
		if(furnitures.size() == 0  || 
			getFch_collection() == null ||
			getFch_request() == null ||
			getNumFurnitures() <= 0 ||this.getTelephone().isEmpty()){
				isCorrect = false;
		}
		if((!getFch_collection().isAfter(getFch_request())
				||(!getFch_collection().isAfter(LocalDate.now())))){
			isCorrect = false;
		}
		return isCorrect;
	}
	
	@Override
	public String toString(){
		String cad = "Telephone: "+this.getTelephone()+"\n"+
				"numFurnitures: "+this.getNumFurnitures()+"\n"+
				"fch_collection: "+this.getFch_collection()+"\n"+
				"fch_request: "+this.getFch_request()+"\n"+
				"collectionPointId: "+this.getCollectionPointId();
		for(Furniture furniture : this.getFurnitures()){
			cad = cad + furniture.toString();
		}
		return cad;
	}

}
