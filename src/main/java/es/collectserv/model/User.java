package es.collectserv.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	private String name;
	private String phone_number;
	
	public User(){
		
	}
	
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
	
	public String getPhone_number() {
		return phone_number;
	}
	
	public void setPhone_number(String phone_number) {
		this.phone_number = phone_number;
	}
	
	 @Override
	 public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;

	    User that = (User) o;

	    if (phone_number != that.phone_number) return false;
	    if (name != null ? !name.equals(that.name) : 
	        that.name != null)return false;

	    return true;
	}
}
