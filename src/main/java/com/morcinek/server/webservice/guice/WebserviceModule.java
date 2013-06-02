package com.morcinek.server.webservice.guice;

import com.google.inject.servlet.ServletModule;
import com.morcinek.server.webservice.resources.*;
import com.owlike.genson.ext.jersey.GensonJsonConverter;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

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

        // genson have advantage of serializing Lists
        bind(MessageBodyReader.class).to(GensonJsonConverter.class);
        bind(MessageBodyWriter.class).to(GensonJsonConverter.class);

        filter("/*").through(HBRequestFilter.class);
        serve("/*").with(GuiceContainer.class);
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