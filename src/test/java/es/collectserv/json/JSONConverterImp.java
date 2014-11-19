package es.collectserv.json;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import es.collectserv.model.CollectionPoint;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;

public class JSONConverterImp implements JSONConverter{
	
	public JSONObject userToJSON(User user) throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user.getName());
		dato.put("phone_number", user.getPhone_number());
		return dato;
	}

	public CollectionPoint JSONtoCollectionPoint(JSONObject respJSON) 
			throws JSONException{
		CollectionPoint collectionPoint = new CollectionPoint();
		collectionPoint.setDirection(respJSON.getString("direction"));
		collectionPoint.setLatitude(respJSON.getDouble("latitude"));
		collectionPoint.setLongitude(respJSON.getDouble("longitude"));
		collectionPoint.setPointId(respJSON.getInt("pointId"));
		collectionPoint.setZone(respJSON.getInt("zone"));
		return collectionPoint;
	}
	
	public JSONObject CollectionRequestToJSON(CollectionRequest appointment) 
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("telephone", appointment.getTelephone());
		obj.put("collectionPointId", appointment.getCollectionPointId());
		obj.put("fch_request",convDateToCompatibleJSONFormat(
				appointment.getFch_request()));
		obj.put("fch_collection",convDateToCompatibleJSONFormat(
				appointment.getFch_collection()));
		obj.put("num_furnitures",appointment.getNumFurnitures());
		JSONArray furnitures = new JSONArray();
		for(Furniture furniture : appointment.getFurnitures()){
			JSONObject item = new JSONObject();
			item.put("id", furniture.getId());
			item.put("cantidad", furniture.getCantidad());
			furnitures.put(item);
		}
		obj.put("furnitures",  furnitures);
		System.out.println(obj.toString());
		return obj;
	}

	public List<ProvisionalAppointment> JSONtoProvisionalAppointment(JSONObject arg) 
			throws Exception{
		List<ProvisionalAppointment> appointments 
			= new ArrayList<ProvisionalAppointment>();
		JSONArray objects  = arg.getJSONArray("provisionalAppointment");
		for(int i = 0; i < objects.length();i++){
			JSONObject object = objects.getJSONObject(i);
	        ProvisionalAppointment appointment = new ProvisionalAppointment();
	        appointment.setCollectionPointId(object.getInt("collectionPointId"));
	        appointment.setFch_collection
	        			(convStringToDate(object.getString("fch_collection")));
	        appointment.setFch_request(convStringToDate(object.getString("fch_request")));
	        appointment.setNumFurnitures(object.getInt("numFurnitures"));
	        appointment.setTelephone(object.getString("telephone"));
	        appointments.add(appointment);
		}
		return appointments;
	}
	
	/**
	 * 
	 * @param Date
	 * @return String of date in format yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 */
	private String convDateToCompatibleJSONFormat(Date date){
		// Format: 2001-07-04T12:08:56.235-07:00
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		return df.format(date);
	}
	
	/**
	 * 
	 * @param String of date in format yyyy-MM-dd'T'HH:mm:ss.SSSXXX
	 * @return Date
	 * @throws ParseException
	 */
	private Date convStringToDate(String inputDate) throws ParseException{
		// Format: 2001-07-04T12:08:56.235-07:00
		Date date 
		  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"	).parse(inputDate);	
	    return date;
	}
}
