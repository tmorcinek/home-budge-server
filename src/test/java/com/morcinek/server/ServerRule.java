package com.morcinek.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.morcinek.server.webservice.guice.CoreTestModule;
import com.morcinek.server.webservice.guice.WebserviceTestModule;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.rules.ExternalResource;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.DispatcherType;
import java.util.EnumSet;

public class ServerRule extends ExternalResource {

    private int port;

    private final String contextPath;

    private Server server;

    private TestGuiceServletContextListener servletContextListener;

    public ServerRule(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

    @Override
    protected void before() throws Throwable {
        server = new Server(port);

        ServletContextHandler root = new ServletContextHandler(server, contextPath, ServletContextHandler.SESSIONS);
        servletContextListener = new TestGuiceServletContextListener();
        root.addEventListener(servletContextListener);
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(ServletContainer.class, "/*");

        server.start();
    }

    private class TestGuiceServletContextListener extends  GuiceServletContextListener{

        @Override
        protected Injector getInjector() {
            return Guice.createInjector(new CoreTestModule(), new WebserviceTestModule());
        }

    }

    @Override
    protected void after() {
        try {
            //server.join();
            server.stop();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Injector getInjector() {
        return servletContextListener.getInjector();
    }
}