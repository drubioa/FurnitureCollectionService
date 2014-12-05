package es.collectserv.model;

import org.joda.time.LocalDate;


public abstract class Request {
	private String telephone;
	private int collectionPointId;
	protected int collectionDay,collectionMonth,collectionYear;
	protected int requestDay,requestMonth,requestYear;
	private LocalDate fch_collection;
	private LocalDate fch_request;
	private int num_furnitures;
	
	public Request(){
		
	}
	
	public Request(int num_furnitures,String telephone,int collectionPointId,
			LocalDate fch_collection,LocalDate fch_request){
		setNumFurnitures(num_furnitures);
		this.telephone = telephone;
		this.fch_collection = fch_collection;
		this.fch_request = fch_request;
		this.collectionPointId = collectionPointId;
		this.fch_request = fch_request;
	}
	
	public LocalDate getFch_request() {
		if(fch_request == null && requestDay != 0 && 
				requestMonth != 0 && requestYear != 0){
			fch_request = new LocalDate(requestYear,requestMonth,requestDay);
		}
		return fch_request;
	}
	
	public LocalDate getFch_collection() {
		if(fch_collection == null && collectionDay != 0 && 
				collectionMonth != 0 && collectionYear != 0){
			fch_collection = new LocalDate(collectionYear,collectionMonth,collectionDay);
		}
		return fch_collection;
	}

	public void setCollectionDay(int day){
		collectionDay = day;
	}
	
	public int getCollectionDay(){
		return collectionDay;
	}
	
	public void setCollectionMonth(int month){
		collectionMonth = month;
	}
	
	public int getCollectionMonth(){
		return collectionMonth;
	}

	public void setCollectionYear(int year){
		collectionYear = year;
	}
	
	public int getCollectionYear(){
		return collectionYear;
	}
	
	public void setRequestDay(int day){
		requestDay = day;
	}
	
	public int getRequestDay(){
		return requestDay;
	}
	
	public void setRequestMonth(int month){
		requestMonth = month;
	}

	public int getRequestMonth(){
		return requestMonth;
	}
	
	public void setRequestYear(int year){
		requestYear = year;
	}
	
	public int getRequestYear(){
		return requestYear;
	}
	
	
	public void setFch_request(LocalDate fch_request) {
		this.fch_request = fch_request;
	}

	public void setFch_collection(LocalDate fch_collection) {
		this.fch_collection = fch_collection;
	}


	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public int getNumFurnitures() {
		return num_furnitures;
	}

	public void setNumFurnitures(int furnitures) throws IllegalArgumentException{
		this.num_furnitures = furnitures;
	}

	public int getCollectionPointId() {
		return collectionPointId;
	}

	public void setCollectionPointId(int collectionPointId) {
		this.collectionPointId = collectionPointId;
	}
	
}
