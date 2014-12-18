package es.collectserv.collrequest;

import java.util.List;

import org.joda.time.LocalDate;

/**
 * 
 * @author Diego Rubio Abujas
 *
 */
public abstract class AppointmentManteiner{

	/**
	 * Check if all DailyServices are after than current date.
	 * @param date
	 * @param days
	 * @return true if all dailyService are after than SystemDate
	 */
	public static boolean isValidDailyServices(List<DailyServices> days){
		for(DailyServices d : days){
			if(!isValidDailyServices(d)){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check if daily service is after than current date.
	 * @param d
	 * @return
	 */
	private static boolean isValidDailyServices(DailyServices d){
		final LocalDate today = new LocalDate();
		return d.getServiceDate().isAfter(today);
	}
	
	/**
	 * Remove invalid dailyServicesDays.
	 * @param days
	 * @return
	 */
	public static List<DailyServices> removeInvalidServiceDate(
			List<DailyServices> days){
		for(DailyServices d : days){
			if(!isValidDailyServices(d)){
				days.remove(d);
			}
		}
		return days;
	}
	
}
