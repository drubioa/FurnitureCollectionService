package es.collectserv.clases;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CollectionRequest{
	private int id;
	private Date fch_request;
	private Date fch_collection;
	private String telephone_number;
	private CollectionPoint collectionPoint;
	private List<Furniture> furnitures;
	
	public CollectionRequest(){
		
	}

	public Date getFch_request() {
		return fch_request;
	}

	public void setFch_request(Date fch_request) {
		this.fch_request = fch_request;
	}

	public Date getFch_collection() {
		return fch_collection;
	}

	public void setFch_collection(Date fch_collection) {
		this.fch_collection = fch_collection;
	}

	public String getTelepehone_number() {
		return telephone_number;
	}

	public void setTelepehone_number(String telepehone_number) {
		this.telephone_number = telepehone_number;
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
	}

}
