package com.morcinek.server.webservice.resources;

import javax.ws.rs.*;

/**
 * A simple class for benchmarking.
 */
@Path("/bench")
@Produces("text/plain")
public class BenchResource {
    private static final char[] contentSource = new char[1024];
    private static final char[] hexDigits = "0123456789ABCDEF".toCharArray();

    static {
        for (int i = 0; i < contentSource.length; i++) {
            contentSource[i] = hexDigits[i % 16];
        }
    }

    @GET
    public String getBench(@QueryParam("size") Integer size) {
        return getResponse(size);
    }

    @POST
    public String postBench(@QueryParam("size") Integer size) {
        return getResponse(size);
    }

    @PUT
    public String putBench(@QueryParam("size") Integer size) {
        return getResponse(size);
    }

    @DELETE
    public String deleteBench(@QueryParam("size") Integer size) {
        return getResponse(size);
    }

    private static String getResponse(Integer size) {
        int theSize = (size != null) ? size.intValue() : 16;
        StringBuilder content = new StringBuilder();

        while (theSize > contentSource.length) {
            content.append(contentSource);
            theSize -= contentSource.length;
        }

        if (theSize > 0) {
            for (int i = 0; i < theSize; i += 1) {
                content.append(hexDigits[i % 16]);
            }
        }

        return content.toString();
    }
}
