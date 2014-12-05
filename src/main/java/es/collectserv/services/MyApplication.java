package es.collectserv.services;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("resources")
public class MyApplication extends ResourceConfig {
	
    public MyApplication() {
    	packages("es.collectserv.services;jersey-media-json-jackson");
    }

}
