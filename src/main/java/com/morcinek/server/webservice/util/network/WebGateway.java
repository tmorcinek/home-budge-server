package com.morcinek.server.webservice.util.network;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.webservice.util.GenericParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class WebGateway implements WebGatewayInterface {

    @Inject
    private GenericParser genericParser;

    @Override
    public String executeGetRequest(String url, HashMap<String, String> params) throws IOException {
        return genericParser.parseText(getHttpResponse(url, params));
    }

    @Override
    public <T> T executeGetRequest(String url, HashMap<String, String> params, Class<T> type) throws IOException {
        return genericParser.parseObject(getHttpResponse(url, params), type);
    }

    @Override
    public Properties executeGetRequestForProperties(String url, HashMap<String, String> params) throws IOException {
        Properties properties = new Properties();
        properties.load(getHttpResponse(url, params));
        return properties;
    }

    private InputStream getHttpResponse(String stringUrl, HashMap<String, String> params) throws IOException {
        URL url = new URL(prepareUrlWithParameters(stringUrl, params));
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        return urlConnection.getInputStream();
    }


    protected String prepareUrlWithParameters(String url, Map<String, String> params) throws UnsupportedEncodingException {
        final StringBuffer query = new StringBuffer();
        query.append(url);
        final String paramsQuery = createQuery(params);
        if (paramsQuery.length() > 0) {
            query.append("?");
            query.append(paramsQuery);
        }
        return query.toString();
    }

    private String createQuery(final Map<String, String> params) throws UnsupportedEncodingException {
        if (params == null) {
            return "";
        }
        final StringBuffer paramsString = new StringBuffer();

        String key;
        String value;

        final Set<Map.Entry<String, String>> entrySet = params.entrySet();

        for (final Map.Entry<String, String> entry : entrySet) {
            key = entry.getKey();
            value = entry.getValue();

            if (key != null && value != null) {
                paramsString.append("&");
                paramsString.append(key);
                paramsString.append("=");
                paramsString.append(URLEncoder.encode(value, "utf-8"));
            }
        }

        paramsString.delete(0, 1);

        return paramsString.toString();
    }
}
