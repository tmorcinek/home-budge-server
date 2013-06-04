package com.morcinek.server.webservice.util.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.webservice.util.GenericParser;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/3/13
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FakeWebGateway implements WebGatewayInterface {

    public static final String ACCESS_TOKEN_OK = "token_ok";
    public static final String ACCESS_TOKEN_FAILED = "token_failed";

    @Inject
    private GenericParser genericParser;

    @Override
    public Properties executeGetRequestForProperties(String url, HashMap<String, String> params) throws IOException {
        Properties properties = new Properties();
        properties.setProperty("access_token", "application_token");
        return properties;
    }

    @Override
    public String executeGetRequest(String url, HashMap<String, String> params) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public <T> T executeGetRequest(String url, HashMap<String, String> params, Class<T> type) throws IOException {
        if (params.get("input_token").equals(ACCESS_TOKEN_OK)) {
            return genericParser.parseObject(getClass().getResourceAsStream("/debug_request_ok.json"), type);
        } else {
            return genericParser.parseObject(getClass().getResourceAsStream("/debug_request_failed.json"), type);
        }
    }
}
