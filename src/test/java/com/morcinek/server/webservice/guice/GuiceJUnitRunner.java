package com.morcinek.server.webservice.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/27/13
 * Time: 11:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class GuiceJUnitRunner extends BlockJUnit4ClassRunner {

    private final Injector injector;

    public GuiceJUnitRunner(Class<?> klass) throws InitializationError {
        super(klass);
        injector = Guice.createInjector(new CoreTestModule());
    }

    @Override
    protected Object createTest() throws Exception {
        Object object = super.createTest();
        injector.injectMembers(object);
        return object;
    }


}
