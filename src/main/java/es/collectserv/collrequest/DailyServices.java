package es.collectserv.collrequest;

import java.util.Date;

public interface DailyServices {
	
	/**
	 * 
	 * @param phone
	 * @param num_furnitures
	 * @return
	 * @throws Exception
	 */
	public ProvisionalAppointment getAppointment(String phone,
			int num_furnitures) throws Exception;
	
	/**
	 * Obtiene el número de enseres que el servicio puere recoger para un día especifico.
	 * @return int número de enseres que se pueden solicitar dicho día en función
	 * de las peticiones previamente realizadas
	 */
	public int obtainRealizablePeticions();
	
	/**
	 * Comprueba si el usuario con dicho número de teléfono tiene una solicitud previa
	 * @param phone
	 * @return
	 */
	public boolean userGotPreviousRequest(String phone);

	/**
	 * Devuelve el día al que corresponde dicho servicio diario
	 * @return
	 */
	public Date getDay();
	
}
