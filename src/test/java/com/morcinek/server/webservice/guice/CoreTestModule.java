package com.morcinek.server.webservice.guice;

import com.google.inject.AbstractModule;

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

    private EntityManager entityManager;

    public CoreTestModule() {
    }

    public CoreTestModule(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    protected void configure() {
        if (entityManager == null) {
            EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnitTest");
            bind(EntityManager.class).toInstance(factory.createEntityManager());
        } else {
            bind(EntityManager.class).toInstance(entityManager);
        }
    }

}
