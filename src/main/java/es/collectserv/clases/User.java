package es.collectserv.clases;

public class User {
	private int id;
	private String name;
	private String phone_number;
	
	public User(String name,String phone_number){
		this.name = name;
		this.phone_number = phone_number;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getPhone_number() {
		return phone_number;
	}
	
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
}
