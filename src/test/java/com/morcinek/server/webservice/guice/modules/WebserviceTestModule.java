package com.morcinek.server.webservice.guice.modules;

import com.morcinek.server.webservice.guice.WebserviceModule;
import com.morcinek.server.webservice.jackson.JacksonConfigurator;
import com.morcinek.server.webservice.jackson.MessageBodyWriterJSON;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.ext.ContextResolver;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class WebserviceTestModule extends WebserviceModule {

    @Override
    protected void configureServlets() {
        bindRestResources();

        bind(JacksonJaxbJsonProvider.class).to(MessageBodyWriterJSON.class).asEagerSingleton();
//        bind(JacksonJaxbJsonProvider.class).to(MessageBodyWriterJSON.class);

        serve("/*").with(GuiceContainer.class);
    }

}