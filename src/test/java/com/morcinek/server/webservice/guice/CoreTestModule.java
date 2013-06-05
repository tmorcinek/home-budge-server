package com.morcinek.server.webservice.guice;

import com.google.inject.AbstractModule;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.facebook.FacebookSessionManager;
import com.morcinek.server.webservice.util.network.FakeWebGateway;
import com.morcinek.server.webservice.util.network.WebGatewayInterface;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 4/5/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CoreTestModule extends AbstractModule {

    @Override
    protected void configure() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnitTest");
        bind(EntityManager.class).toInstance(factory.createEntityManager());
        bind(WebGatewayInterface.class).to(FakeWebGateway.class);
        bind(SessionManager.class).to(FacebookSessionManager.class);
    }

}
