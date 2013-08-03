package com.morcinek.server.webservice.guice.modules;

import com.morcinek.server.webservice.guice.WebserviceModule;
import com.morcinek.server.webservice.jackson.CustomJacksonJaxbJsonProvider;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

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

        bind(JacksonJaxbJsonProvider.class).to(CustomJacksonJaxbJsonProvider.class).asEagerSingleton();

        serve("/*").with(GuiceContainer.class);
    }

}