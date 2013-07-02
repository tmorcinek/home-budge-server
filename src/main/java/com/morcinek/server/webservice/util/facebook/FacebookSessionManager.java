package com.morcinek.server.webservice.util.facebook;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.webservice.util.SessionManager;
import com.morcinek.server.webservice.util.facebook.model.Data;
import com.morcinek.server.webservice.util.facebook.model.ResponseData;
import com.morcinek.server.webservice.util.network.WebGatewayInterface;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/1/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class FacebookSessionManager implements SessionManager {

    private final static String MAIN_URL = "https://graph.facebook.com/oauth/access_token";
    private final static String VALIDATE_URL = "https://graph.facebook.com/debug_token";

    private final static String CLIENT_ID = "181670995334141";
    private final static String CLIENT_SECRET = "584377ad0b58170f9756c458156640ae";

    private final WebGatewayInterface webGateway;

    private final String appToken;

    private final Map<String, Long> tokensMap;

    @Inject
    public FacebookSessionManager(WebGatewayInterface webGateway) throws IOException {
        this.webGateway = webGateway;
        tokensMap = createTokensMap();
        appToken = getAccessToken();
    }

    private Map<String, Long> createTokensMap() {
//        return new MapMaker()
//                .expiration(2, TimeUnit.HOURS)
//                .makeMap();
        return new HashMap<String, Long>();
    }

    private String getAccessToken() throws IOException {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);
        params.put("grant_type", "client_credentials");
        return webGateway.executeGetRequestForProperties(MAIN_URL, params).getProperty("access_token");
    }


    @Override
    public boolean validateToken(String accessToken) {
        if (accessToken == null) {
            return false;
        }

        if (tokensMap.containsKey(accessToken.hashCode())) {
            return true;
        }

        Data data = getDataFromToken(accessToken);
        if (data.isIs_valid()) {
            tokensMap.put(accessToken, data.getUser_id());
            return true;
        }

        return false;
    }

    @Override
    public Long getUserIdFromToken(String accessToken) {
        return tokensMap.get(accessToken);
    }

    @Override
    public Long getUserIdFromRequest(HttpServletRequest request) {
        String accessToken = request.getHeader("accessToken");
        return getUserIdFromToken(accessToken);

    }

    private Data getDataFromToken(String accessToken) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("input_token", accessToken);
        params.put("access_token", appToken);
        try {
            return webGateway.executeGetRequest(VALIDATE_URL, params, ResponseData.class).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

//    // TODO needs to finish this function to get either error or properties from stream.
//    private void extendTokenLife(String accessToken){
//        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("grant_type", "fb_exchange_token");
//        params.put("client_id", CLIENT_ID);
//        params.put("client_secret", CLIENT_SECRET);
//        params.put("fb_exchange_token", appToken);
//        try {
//            webGateway.executeGetRequest(MAIN_URL, params);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
