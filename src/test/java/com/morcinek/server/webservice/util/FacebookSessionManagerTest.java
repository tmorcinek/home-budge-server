package com.morcinek.server.webservice.util;

import com.google.inject.Inject;
import com.morcinek.server.webservice.guice.SimpleGuiceJUnitRunner;
import com.morcinek.server.webservice.util.network.FakeWebGateway;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 2:52 PM
 */
@RunWith(SimpleGuiceJUnitRunner.class)
public class FacebookSessionManagerTest {

    @Inject
    private SessionManager sessionManager;

    @Test
    public void validateTokenFailedTest() throws Exception {
        // given
        String accessToken = FakeWebGateway.ACCESS_TOKEN_FAILED;

        // when
        boolean isTokenValid = sessionManager.validateToken(accessToken);

        // then
        Assertions.assertThat(isTokenValid).isFalse();
    }

    @Test
    public void validateTokenTest() throws Exception {
        // given
        String accessToken = FakeWebGateway.ACCESS_TOKEN_OK;

        // when
        boolean isTokenValid = sessionManager.validateToken(accessToken);

        // then
        Assertions.assertThat(isTokenValid).isTrue();
    }
}
