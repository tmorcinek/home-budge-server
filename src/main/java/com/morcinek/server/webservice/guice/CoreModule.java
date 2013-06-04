package com.morcinek.server.webservice.guice;

import com.google.inject.AbstractModule;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.facebook.FacebookSessionManager;
import com.morcinek.server.webservice.util.network.WebGateway;
import com.morcinek.server.webservice.util.network.WebGatewayInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: tomek
 * Date: 2/10/13
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoreModule extends AbstractModule {

    @Override
    protected void configure() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        bind(EntityManager.class).toInstance(factory.createEntityManager());
        bind(WebGatewayInterface.class).to(WebGateway.class);
        bind(SessionManager.class).to(FacebookSessionManager.class);
    }
}
