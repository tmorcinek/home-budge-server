package com.morcinek.server.webservice.guice;

import com.google.inject.AbstractModule;

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
public class CoreModule extends AbstractModule{

    @Override
    protected void configure() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("persistenceUnit");
        bind(EntityManager.class).toInstance(factory.createEntityManager());
    }
}
