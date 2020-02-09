package com.remittance;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

import com.remittance.db.DataAccessObject;
import com.remittance.db.DataAccessObjectBuilder;
import com.remittance.service.AccountService;
import com.remittance.service.ServiceExceptionMapper;
import com.remittance.service.TransactionService;
import com.remittance.utils.ApplicationConstant;

/**
 * Main Class (Application starting point) 
 * @author Venkat Sivapuram
 */
public class Launcher {

	private static Logger log = Logger.getLogger(Launcher.class);

	public static void main(String[] args){
		try{
			log.info("Initialize demo .....");
			DataAccessObject object = (DataAccessObject) DataAccessObjectBuilder.getH2Object();
			object.populateTestData(ApplicationConstant.SQL_FILE_NAME);
			log.info("Initialization Complete....");
			startService();
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	/***
	 * To start the server and service
	 * @throws Exception
	 */
	private static void startService() throws Exception {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		Server server = new Server(8980);
		server.setHandler(context);
		ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
		servletHolder.setInitParameter("jersey.config.server.provider.classnames",
				AccountService.class.getCanonicalName() + ","
						+ ServiceExceptionMapper.class.getCanonicalName() + ","
						+ TransactionService.class.getCanonicalName());
		try{
			server.start();
			server.join();
		} finally {
			server.destroy();
		}
	}
}