package com.morcinek.server.webservice.jackson;

import com.google.inject.Singleton;
import org.codehaus.jackson.jaxrs.Annotations;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;

@Singleton
@Provider
//@javax.ws.rs.Consumes({"application/json", "text/json"})
//@javax.ws.rs.Produces({"application/json", "text/json"})
public class MessageBodyWriterJSON extends JacksonJaxbJsonProvider{

    public MessageBodyWriterJSON() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MessageBodyWriterJSON(Annotations... annotationsToUse) {
        super(annotationsToUse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public MessageBodyWriterJSON(ObjectMapper mapper, Annotations[] annotationsToUse) {
        super(mapper, annotationsToUse);    //To change body of overridden methods use File | Settings | File Templates.
    }

    //    @Override
//    public ObjectMapper locateMapper(Class<?> type, MediaType mediaType) {
//        ObjectMapper mapper = super.locateMapper(type, mediaType);
//        //DateTime in ISO format "2012-04-07T17:00:00.000+0000" instead of 'long' format
////        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
//        return mapper;
//    }
}