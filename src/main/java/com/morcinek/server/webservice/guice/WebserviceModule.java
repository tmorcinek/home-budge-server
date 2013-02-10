package com.morcinek.server.webservice.guice;

import com.google.inject.servlet.ServletModule;
import com.morcinek.server.webservice.resources.BenchResource;
import com.morcinek.server.webservice.resources.JacksonResource;
import com.morcinek.server.webservice.resources.SampleResource;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebserviceModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bindRestResources();

        /* bind jackson converters for JAXB/JSON serialization */
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.feature.Trace", "true");
        serve("*").with(GuiceContainer.class, initParams);
    }

    /**
     * bind the REST resources
     */
    private void bindRestResources() {
        bind(BenchResource.class);
        bind(SampleResource.class);
        bind(JacksonResource.class);
    }

}
