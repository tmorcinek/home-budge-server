package com.morcinek.server.webservice.guice;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.morcinek.server.webservice.util.facebook.FacebookSessionManager;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Tomasz Morcinek
 * Date: 6/2/13
 * Time: 1:06 AM
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class HBRequestFilter implements Filter {

    @Inject
    private FacebookSessionManager facebookSessionManager;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String accessToken = (String) ((HttpServletRequest) servletRequest).getHeader("accessToken");
        if (!facebookSessionManager.validateToken(accessToken)) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {
    }
}
