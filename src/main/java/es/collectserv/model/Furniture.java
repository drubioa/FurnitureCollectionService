package es.collectserv.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Furniture {
	private int id;
	private int cantidad;
	private String mName;
	
	public Furniture(){
		
	}
	
	public Furniture(int id,int cantidad){
		this.id = id;
		this.cantidad = cantidad;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getCantidad() {
		return cantidad;
	}
	
	public void setName(String name){
		mName = name;
	}

	public String getName(){
		return mName;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	@Override
	public String toString(){
		return "furnitures id: "+id+"\n"+
				"cantidad: "+cantidad+"\n";
	}
}
