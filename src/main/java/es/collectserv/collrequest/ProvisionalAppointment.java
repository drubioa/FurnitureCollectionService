package es.collectserv.collrequest;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import es.collectserv.clases.Request;

@XmlRootElement
public class ProvisionalAppointment extends Request{
	final static int LIVE_TIME = 60;
	
	ProvisionalAppointment(){
		
	}
	
	ProvisionalAppointment(String telephone,int num_furnitures,Date fch_collection){
		super(telephone,num_furnitures,fch_collection);
	}
	
}
