package es.collectserv.collrequest;

import java.util.Date;

public interface DailyServices {
	
	/**
	 * Para los parametros indicados obtiene una solicitud de recogida pendiente de confirmar
	 * @param phone
	 * @param num_furnitures
	 * @param pointId
	 * @return
	 * @throws Exception el número de muebles debe ser inferior al máximo permitido por la organización
	 */
	public ProvisionalAppointment getAppointment(String phone,
			int num_furnitures,int pointId) throws Exception;
	
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
	
	/**
	 * Confirma una solicitud pendiente de confirmar. Esta es registrada en la base de datos
	 * y e liminada del listado de solicitudes pendientes de confirmar.
	 * @param phone telefono del usuario
	 */
	public void confirmProvisionalAppointment(String phone) throws Exception;
}
