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
			final LocalDate TODAY = new LocalDate();
			if(!d.getDate().isAfter(TODAY)){
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Remove invalid dailyServicesDays.
	 * @param days
	 * @return
	 */
	public static List<DailyServices> removeInvalidServiceDate(
			List<DailyServices> days){
		final LocalDate TODAY = new LocalDate();
		for(DailyServices d : days){
			if(!d.getDate().isAfter(TODAY)){
				days.remove(d);
			}
		}
		return days;
	}
	
}
