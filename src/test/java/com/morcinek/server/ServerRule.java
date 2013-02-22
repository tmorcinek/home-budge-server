package com.morcinek.server;

import com.google.inject.servlet.GuiceFilter;
import com.morcinek.server.webservice.Main;
import com.morcinek.server.webservice.guice.GuiceConfiguration;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.rules.ExternalResource;

import javax.servlet.DispatcherType;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.EnumSet;

public class ServerRule extends ExternalResource {

	private int port;

	private final String contextPath;

	private Server server;

	public ServerRule(int port, String contextPath) {
		this.port = port;
		this.contextPath = contextPath;
	}

	public void start() throws Exception {
        server = new Server(port);
        ServletContextHandler root = new ServletContextHandler(server, contextPath, ServletContextHandler.SESSIONS);

        root.addEventListener(new GuiceConfiguration());
        root.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        root.addServlet(ServletContainer.class, "/*");

        server.start();
	}

	public static void main(String[] args) throws Exception {
		ServerRule webStart = new ServerRule(Main.PORT, Main.CONTEXT_PATH);

		webStart.start();

		System.err.println("Running. Hit <Enter> to Stop");

		new BufferedReader(new InputStreamReader(System.in)).readLine();

		webStart.stop();

	}

	public void stop() throws Exception {
		//server.join();
		server.stop();
	}

	@Override
	protected void before() throws Throwable {
		start();
	}

	@Override
	protected void after() {
		try {
			stop();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}