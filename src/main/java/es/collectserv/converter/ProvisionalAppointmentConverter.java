package es.collectserv.converter;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import es.collectserv.model.ProvisionalAppointment;

@XmlRootElement(name = "provisionalAppointment")
public class ProvisionalAppointmentConverter {
	  private ProvisionalAppointment entity = null;
	
	  public ProvisionalAppointmentConverter(){
		  entity = new ProvisionalAppointment();
	  }
	  
	  public ProvisionalAppointmentConverter(ProvisionalAppointment entity){
		  this.entity = entity;
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
	  public Date getFch_collection(){
		  return entity.getFch_collection();
	  }
	  
	  @XmlElement
	  public Date getFch_request(){
		  return entity.getFch_request();
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
	  
	  public void setFch_collection(Date fch){
		  entity.setFch_collection(fch);
	  }
	  
	  public void setFch_request(Date fch){
		  entity.setFch_request(fch);
	  }
}
