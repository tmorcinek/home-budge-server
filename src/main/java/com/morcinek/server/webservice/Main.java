package com.morcinek.server.webservice;


import com.google.inject.servlet.GuiceFilter;
import com.morcinek.server.webservice.guice.GuiceConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Starts an embedded Jetty server.
 */
public class Main {

    public static final String CONTEXT_PATH = "/api";
    public static final int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = new Server(PORT);
        ServletContextHandler root = new ServletContextHandler(server, CONTEXT_PATH, ServletContextHandler.SESSIONS);

        root.addEventListener(new GuiceConfiguration());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(ServletContainer.class, "/*");

        server.start();
    }
}
