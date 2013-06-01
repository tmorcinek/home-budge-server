package com.morcinek.server.webservice.util.network;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface WebGatewayInterface {

    public Properties executeGetRequestForProperties(String url, HashMap<String, String> params) throws IOException;

    public String executeGetRequest(String url, HashMap<String, String> params) throws IOException;

    public <T> T executeGetRequest(String url, HashMap<String, String> params, Class<T> type) throws IOException;
}
