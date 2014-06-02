package es.collectserv.clases;

import java.util.Date;

public class Request {
	private Date fch_request;
	private Date fch_collection;
	private int num_furnitures;
	private String telephone;
	final static int MAX_FURNITURES_PER_REQ = 4;
	
	public Request(){
		
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

	public int getFurnitures() {
		return num_furnitures;
	}

	public void setFurnitures(int furnitures) throws Exception{
		if(furnitures > 4 || furnitures < 1){
			throw new Exception("Invalid number of furnitres in this request");
		}
		this.num_furnitures = furnitures;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
}
