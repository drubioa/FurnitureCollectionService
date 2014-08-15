package es.collectserv.collrequest;

import java.util.Date;

public class ProvisionalAppointment 
	extends Request
	implements Runnable{
	static final int SLEEP_TIME = 5000; // Time which the appointment stays in the system before user confirm that,in milliseconds
	private DailyServices refDailyServices;
	
	public ProvisionalAppointment(){
		
	}
	
	public void setRequestManagement(DailyServices day){
		refDailyServices = day;
	}
	
	public ProvisionalAppointment(int num_furnitures,String telephone,int collectionPointId,Date fch_collection) throws Exception{
		super(num_furnitures,telephone,collectionPointId,fch_collection);
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
