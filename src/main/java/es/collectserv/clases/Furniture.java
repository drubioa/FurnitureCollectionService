package es.collectserv.clases;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Furniture {
	private int id;
	private int cantidad;
	
	public Furniture(){
		
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
}
