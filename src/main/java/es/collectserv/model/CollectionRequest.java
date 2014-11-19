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
		if(this.furnitures.size() == 0  
				|| this.getFch_collection() == null || this.getFch_request() == null ||
				getNumFurnitures() <= 0 ||this.getTelephone().isEmpty()){
			isCorrect = false;
		}
		return isCorrect;
	}
	
	@Override
	public String toString(){
		String cad = "Telephone: "+this.getTelephone()+"\n"+
				"numFurnitures"+this.getNumFurnitures()+"\n"+
				"fch_Collection"+this.getFch_collection()+"\n"+
				"fch_request"+this.getFch_request()+"\n"+
				"collectionPointId"+this.getCollectionPointId()+"\n";
		for(Furniture furniture : this.getFurnitures()){
			cad = cad + furniture.toString();
		}
		return cad;
	}
	
}
