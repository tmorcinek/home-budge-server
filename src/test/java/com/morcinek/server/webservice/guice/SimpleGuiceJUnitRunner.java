package com.morcinek.server.webservice.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.morcinek.server.webservice.guice.modules.CoreTestModule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 2/27/13
 * Time: 11:54 PM
 */
public class SimpleGuiceJUnitRunner extends BlockJUnit4ClassRunner {

    private final Injector injector;

    public SimpleGuiceJUnitRunner(Class<?> klass) throws InitializationError {
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
