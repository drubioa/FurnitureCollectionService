package es.collectserv.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Furniture {
	private int id;
	private int cantidad;
	
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

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	
	@Override
	public String toString(){
		return "furnitures id: "+id+"\n"+
				"cantidad: "+cantidad+"\n";
	}
}