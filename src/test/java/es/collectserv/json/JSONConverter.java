package es.collectserv.json;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import es.collectserv.model.CollectionPoint;
import es.collectserv.model.CollectionRequest;
import es.collectserv.model.Furniture;
import es.collectserv.model.ProvisionalAppointment;
import es.collectserv.model.User;

public abstract class JSONConverter{
	DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
	
	public static JSONObject userToJSON(User user) throws JSONException{
		JSONObject dato = new JSONObject();
		dato.put("name", user.getName());
		dato.put("phone_number", user.getPhone_number());
		return dato;
	}

	public static CollectionPoint JSONtoCollectionPoint(JSONObject respJSON) 
			throws JSONException{
		CollectionPoint collectionPoint = new CollectionPoint();
		collectionPoint.setDirection(respJSON.getString("direction"));
		collectionPoint.setLatitude(respJSON.getDouble("latitude"));
		collectionPoint.setLongitude(respJSON.getDouble("longitude"));
		collectionPoint.setPointId(respJSON.getInt("pointId"));
		collectionPoint.setZone(respJSON.getInt("zone"));
		return collectionPoint;
	}
	
	public static JSONObject CollectionRequestToJSON(CollectionRequest appointment) 
			throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("telephone", appointment.getTelephone());
		obj.put("collectionPointId", appointment.getCollectionPointId());
		obj.put("collectionDay", appointment.getFch_collection().getDayOfMonth());
		obj.put("collectionMonth", appointment.getFch_collection().getMonthOfYear());
		obj.put("collectionYear", appointment.getFch_collection().getYear());
		obj.put("requestDay", appointment.getFch_request().getDayOfMonth());
		obj.put("requestMonth", appointment.getFch_request().getMonthOfYear());
		obj.put("requestYear", appointment.getFch_request().getYear());
		obj.put("num_furnitures",appointment.getNumFurnitures());
		JSONArray furnitures = new JSONArray();
		for(Furniture furniture : appointment.getFurnitures()){
			JSONObject item = new JSONObject();
			item.put("id", furniture.getId());
			item.put("cantidad", furniture.getCantidad());
			furnitures.put(item);
		}
		obj.put("furnitures",  furnitures);
		return obj;
	}

	public static List<ProvisionalAppointment> JSONtoProvisionalAppointment(JSONObject arg)
			throws JSONException, ParseException {
		List<ProvisionalAppointment> appointments 
			= new ArrayList<ProvisionalAppointment>();
		JSONArray objects  = arg.getJSONArray("provisionalAppointment");
		for(int i = 0; i < objects.length();i++){
			JSONObject object = objects.getJSONObject(i);
	        ProvisionalAppointment appointment = new ProvisionalAppointment();
	        appointment.setCollectionPointId(object.getInt("collectionPointId"));
	        LocalDate fch_collection = new LocalDate(object.getString("fch_collection"));
	        appointment.setFch_collection(fch_collection);
	        LocalDate fch_request = new LocalDate(object.getString("fch_request"));
	        appointment.setFch_request(fch_request);
	        appointment.setNumFurnitures(object.getInt("numFurnitures"));
	        appointment.setTelephone(object.getString("telephone"));
	        appointments.add(appointment);
		}
		return appointments;
	}
	
	public static List<CollectionRequest> JSONtoCollectionRequest(JSONObject arg)
			throws JSONException, ParseException {
		List<CollectionRequest> requests 
			= new ArrayList<CollectionRequest>();
		JSONArray objects  = arg.getJSONArray("collectionRequest");
		for(int i = 0; i < objects.length();i++){
			JSONObject object = objects.getJSONObject(i);
			CollectionRequest req = new CollectionRequest();
	        req.setCollectionPointId(object.getInt("collectionPointId"));
	        LocalDate fch_collection = new LocalDate(object.getString("fch_collection"));
	        req.setFch_collection(fch_collection);
	        LocalDate fch_request = new LocalDate(object.getString("fch_request"));
	        req.setFch_request(fch_request);
	        req.setNumFurnitures(object.getInt("numFurnitures"));
	        req.setTelephone(object.getString("telephone"));
	        // Incluir furnitures
	        List<Furniture> furnitures = new ArrayList<Furniture>();	       
	        try{
	        	JSONArray jsonFurnitures = object.getJSONArray("furnitures");
		        for(int j = 0;j < jsonFurnitures.length();j++){
		        	JSONObject obj = jsonFurnitures.getJSONObject(j);
		        	int cantidad = obj.getInt("cantidad");
		        	int furnitureId = obj.getInt("id");
		        	Furniture f = new Furniture(furnitureId,cantidad);
		        	furnitures.add(f);
		        }
	        }catch(JSONException e){
	        	JSONObject furniture = object.getJSONObject("furnitures");
	        	int cantidad = furniture.getInt("cantidad");
	        	int furnitureId = furniture.getInt("id");
	        	Furniture f = new Furniture(furnitureId,cantidad);
	        	furnitures.add(f);
	        }
	        req.setFurnitures(furnitures);
	        requests.add(req);
		}
		return requests;
	}

}
