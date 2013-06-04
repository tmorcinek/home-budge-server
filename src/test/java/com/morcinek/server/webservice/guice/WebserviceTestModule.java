package com.morcinek.server.webservice.guice;

import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

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

        serve("/*").with(GuiceContainer.class);
    }

}