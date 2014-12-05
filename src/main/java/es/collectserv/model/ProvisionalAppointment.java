package es.collectserv.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.joda.time.LocalDate;

import es.collectserv.collrequest.DailyServices;

@XmlRootElement
public class ProvisionalAppointment 
	extends Request
	implements Runnable{
	@XmlTransient
	static final int SLEEP_TIME = 100000; // 10 minutes
	// Time which the appointment stays in the system before user confirm that,
	// in milliseconds
	@XmlTransient
	private DailyServices refDailyServices;
	
	public ProvisionalAppointment(){
		
	}
	
	public void setRequestManagement(DailyServices day){
		refDailyServices = day;
	}
	
	public ProvisionalAppointment(int num_furnitures,String telephone,
			int collectionPointId,LocalDate fch_collection){
		super(num_furnitures,telephone,collectionPointId,fch_collection,new LocalDate());
	}

	public void run() {
        try {
			Thread.sleep(SLEEP_TIME);
			refDailyServices.removeUnconfirmedAppointment(this);
			this.finalize();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
