package com.morcinek.server.webservice.util;

import com.google.inject.Inject;
import com.morcinek.server.webservice.guice.SimpleGuiceJUnitRunner;
import com.morcinek.server.webservice.util.facebook.FacebookSessionManager;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(SimpleGuiceJUnitRunner.class)
public class FacebookSessionManagerTest {

    @Inject
    private FacebookSessionManager sessionManager;

    @Test
    public void validateTokenFailedTest() throws Exception {
        // given
        String accessToken = "CAAClOpMkkZC0BAHnuGZB7AoqjMHlkhiCc3NdcRuR7pn60APTeG05AbWxIRGYn3CdtE0eLRkM25gAKqg9Yj31Srss8IRGdXYI9UWvGmRkwUk3PpWncpPOtump3Xr7STJMYd8u6U25LNy2hO5o9EJyZCRjb7teQAZD";

        // when
        boolean isTokenValid = sessionManager.validateToken(accessToken);

        // then
        Assertions.assertThat(isTokenValid).isFalse();
    }
}
