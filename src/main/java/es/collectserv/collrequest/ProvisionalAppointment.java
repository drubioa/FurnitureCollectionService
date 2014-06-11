package es.collectserv.collrequest;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProvisionalAppointment extends Request{
	final static int LIVE_TIME = 60;

	public ProvisionalAppointment(){
		
	}
	
	public ProvisionalAppointment(int num_furnitures,String telephone,int collectionPointId,Date fch_collection) throws Exception{
		super(num_furnitures,telephone,collectionPointId,fch_collection);
	}

}
