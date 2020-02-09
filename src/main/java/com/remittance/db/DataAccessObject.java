package com.remittance.db;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import com.remittance.dao.AccountDetailsDAO;
import com.remittance.dao.TransferDetailsDAO;
import com.remittance.dao.impl.AccountDetailsDAOImpl;
import com.remittance.dao.impl.TransferDetailsDAOImpl;
import com.remittance.utils.ApplicationConstant;

/**
 * This class is used to load the DB driver, DAO layer objects, Connection of Database.
 * @author Venkat Sivapuram
 *
 */
public class DataAccessObject implements DataAccessObjectBuilder {
	
	private static Logger log = Logger.getLogger(DataAccessObject.class);

	private AccountDetailsDAO accountDAO = null;
	private TransferDetailsDAO transferDAO = null;

	public DataAccessObject() {
		DbUtils.loadDriver(ApplicationConstant.DB_DRIVER);
	}
	public TransferDetailsDAO getTransferDetailsDAO() {
		transferDAO = new TransferDetailsDAOImpl();
		return transferDAO;
	}
	public AccountDetailsDAO getAccountDetailsDAO() {
		accountDAO = new AccountDetailsDAOImpl();
		return accountDAO;
	}
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(ApplicationConstant.DB_CONNECTION_URL, ApplicationConstant.DB_USER, ApplicationConstant.DB_PASSWORD);
	}

	@Override
	public void populateTestData(String fileName) {
		log.info("Populating Test User Table and data ..... ");
		try(Connection conn = DataAccessObject.getConnection();) {
			FileReader reader = new FileReader(fileName);
			RunScript.execute(conn, reader);
		} catch (SQLException | FileNotFoundException e) {
			log.error("populateTestData(): Error populating user data: ", e);
			throw new RuntimeException(e);
		}
	}
	@Override
	public void truncateTestedData(String dropScript){
		log.info("Truncating Testing data with tables..... ");
		try(Connection conn = DataAccessObject.getConnection();) {
			FileReader reader = new FileReader(dropScript);
			RunScript.execute(conn, reader);
		} catch (SQLException | FileNotFoundException e) {
			log.error("truncateTestedData(): Error while truncating the tables and data : ", e);
			throw new RuntimeException(e);
		}		
	}
}