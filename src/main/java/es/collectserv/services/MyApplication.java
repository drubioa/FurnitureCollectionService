package es.collectserv.services;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.sun.jersey.api.json.JSONConfiguration;

@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {
	
    public MyApplication() {
        packages("es.collectserv.services"
        		+ ";org.glassfish.jersey.examples.jsonmoxy");
    	JSONConfiguration.natural().build();
    }

}
