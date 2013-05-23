package com.morcinek.server.webservice.guice;

import com.google.inject.servlet.ServletModule;
import com.morcinek.server.webservice.resources.*;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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

        Map<String, String> initParams = new HashMap<String, String>();
        initParams.put("com.sun.jersey.config.feature.Trace", "true");
        serve("*").with(GuiceContainer.class, initParams);
    }

    /**
     * bind the REST resources
     */
    private void bindRestResources() {
        bind(SampleResource.class);
        bind(UserResource.class);
        bind(AccountResource.class);
        bind(RecordResource.class);
        bind(AuthorResource.class);
    }

}