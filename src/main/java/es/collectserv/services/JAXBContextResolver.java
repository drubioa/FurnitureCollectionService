package es.collectserv.services;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

import es.collectserv.converter.ProvisionalAppointmentConverter;

@Provider
public class JAXBContextResolver implements ContextResolver< JAXBContext > {

    private JAXBContext context;
    @SuppressWarnings("rawtypes")
	private Class[] types = {ProvisionalAppointmentConverter.class};

    public JAXBContextResolver() throws Exception {
        this.context = 
        	new JSONJAXBContext(JSONConfiguration.mapped()
        			.arrays("provisionalAppointment").build(),types);
    }

    public JAXBContext getContext(Class<?> objectType) {
        System.out.println("TUG CALL");
        for (@SuppressWarnings("rawtypes") Class type : types) {
            if (type == objectType) {
                return context;
            }
        }
        
        
        return null;
    }
}