package com.morcinek.server.webservice.guice;

import com.google.inject.servlet.ServletModule;
import com.morcinek.server.webservice.resources.*;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

//import com.owlike.genson.ext.jersey.GensonJsonConverter;

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

        filter("/*").through(HBRequestFilter.class);
        Map<String, String> params = new HashMap<String, String>();
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        serve("/api/*").with(GuiceContainer.class, params);
    }

    /**
     * bind the REST resources
     */
    protected void bindRestResources() {
        bind(UserResource.class);
        bind(AccountResource.class);
        bind(RecordResource.class);
        bind(BalanceResource.class);
        bind(AuthorResource.class);
    }

}