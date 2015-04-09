package es.collectserv.converter;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;

@XmlRootElement(name = "collectionRequest")
public class CollectionRequestConverter {
	private CollectionRequest entity = null;
	private static DateTimeFormatter dtfOut =
			DateTimeFormat.forPattern("yyyy-MM-dd");
	
	 public CollectionRequestConverter(){
		  entity = new CollectionRequest();
	 }
	 
	 
	 public CollectionRequestConverter(CollectionRequest entity){
		 this.entity = entity;
	 }

	 @XmlElement
	 public int getId(){
		 return entity.getId();
	 }

	 @XmlElement
	 public List<Furniture> getFurnitures(){
		 return entity.getFurnitures();
	 }
	 
	 @XmlElement
	 public int getNumFurnitures(){
		 return entity.getNumFurnitures();
	 }
	  
	  
	 @XmlElement
	 public String getTelephone(){
		 return entity.getTelephone();
	 }
	  
	 @XmlElement
	 public int getCollectionPointId(){
		 return entity.getCollectionPointId();
	 }
	  
	 @XmlElement
	 public String getFch_collection(){
		 LocalDate date =  entity.getFch_collection();
		 return dtfOut.print(date);
	 }
	  
	 @XmlElement
	  public String getFch_request(){
		  LocalDate date =  entity.getFch_request();
		  return dtfOut.print(date);
	  }

	  public void setNumFurnitures(int furnitures) throws Exception{
		  	entity.setNumFurnitures(furnitures);
	  }
	  
	  public void setTelephone(String telephone){
		  entity.setTelephone(telephone);
	  }
	  
	  public void setCollectionPointId(int collectionPointId){
		  entity.setCollectionPointId(collectionPointId);
	  }
	  
	  public void setFch_collection(LocalDate fch){
		  entity.setFch_collection(fch);
	  }
	  
	  public void setFch_request(LocalDate fch){
		  entity.setFch_request(fch);
	  }
}
