package com.morcinek.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.morcinek.server.webservice.guice.CoreTestModule;
import com.morcinek.server.webservice.guice.WebserviceModule;
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
    private EntityManager entityManager;

    private Server server;

    public ServerRule(int port, String contextPath) {
        this.port = port;
        this.contextPath = contextPath;
    }

    @Override
    protected void before() throws Throwable {
        server = new Server(port);

        ServletContextHandler root = new ServletContextHandler(server, contextPath, ServletContextHandler.SESSIONS);
        root.addEventListener(new GuiceServletContextListener() {
            @Override
            protected Injector getInjector() {
                entityManager = Persistence.createEntityManagerFactory("persistenceUnitTest").createEntityManager();
                return Guice.createInjector(new CoreTestModule(entityManager), new WebserviceModule());
            }
        });
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(ServletContainer.class, "/*");

        server.start();
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


    public EntityManager getEntityManager() {
        return entityManager;
    }
}