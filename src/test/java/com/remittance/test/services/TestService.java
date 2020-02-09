package com.remittance.test.services;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remittance.service.AccountService;
import com.remittance.service.ServiceExceptionMapper;
import com.remittance.service.TransactionService;
import com.remittance.test.BaseTestUtil;
import com.remittance.utils.ApplicationConstant;

public abstract class TestService implements BaseTestUtil{
	protected static Server server = null;
	protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();

	protected static HttpClient client ;
	protected ObjectMapper mapper = new ObjectMapper();
	protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:9084");

	@BeforeClass
	public static void setup() throws Exception {
		h2DaoFactory.populateTestData(ApplicationConstant.SQL_FILE_NAME);
		startServer();
		connManager.setDefaultMaxPerRoute(100);
		connManager.setMaxTotal(200);
		client= HttpClients.custom()
				.setConnectionManager(connManager)
				.setConnectionManagerShared(true)
				.build();

	}

	@AfterClass
	public static void closeClient() throws Exception {
		HttpClientUtils.closeQuietly(client);
		h2DaoFactory.truncateTestedData(ApplicationConstant.SQL_DELETE_SCRIPT);
	}


	private static void startServer() throws Exception {
		if (server == null) {
			server = new Server(9084);
			ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
			context.setContextPath("/");
			server.setHandler(context);
			ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
			servletHolder.setInitParameter("jersey.config.server.provider.classnames",
					AccountService.class.getCanonicalName() + "," +
							ServiceExceptionMapper.class.getCanonicalName() + "," +
							TransactionService.class.getCanonicalName());
			server.start();
		}
	}
}