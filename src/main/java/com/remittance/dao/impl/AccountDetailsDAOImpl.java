package com.remittance.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.remittance.dao.AccountDetailsDAO;
import com.remittance.db.DataAccessObject;
import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;
import com.remittance.utils.ApplicationConstant;

public class AccountDetailsDAOImpl implements AccountDetailsDAO {

	private static Logger logger = Logger.getLogger(AccountDetailsDAOImpl.class);

	/**
	 * Used to get all accounts.
	 */
	@Override
	public List<AccountDetails> getAllAccounts() throws BaseException {
		logger.info("AccountDetailsDAOImpl ~ getAllAccounts()");
		try(Connection conn = DataAccessObject.getConnection();
				PreparedStatement stmt = conn.prepareStatement(ApplicationConstant.SQL_GET_ALL_ACC);
				ResultSet rs = stmt.executeQuery();) {
			return getListOfAccountDetails(rs);
		} catch (SQLException exp) {
			throw new BaseException("getAccountById() ~ Error reading account data", ApplicationConstant.SQL_ERROR_CODE, exp);
		} 
	}

	/**
	 * Get Account By Id
	 * @param accountId
	 * @exception BaseException
	 */
	@Override
	public AccountDetails getAccountById(Long accountId) throws BaseException {
		logger.info("AccountDetailsDAOImpl ~ getAccountById()");
		if(Objects.isNull(accountId)){
			throw new BaseException("getAccountById(): Error reading account data id");
		}
		ResultSet rs = null;
		try (Connection conn = DataAccessObject.getConnection();
				PreparedStatement stmt = conn.prepareStatement(ApplicationConstant.SQL_GET_ACC_BY_ID);) {
			stmt.setObject(1, accountId);
			rs = stmt.executeQuery();
			List<AccountDetails> allAccounts = getListOfAccountDetails(rs);
			return allAccounts.size()>0 ? allAccounts.get(0) : null;
		} catch (SQLException exp) {
			throw new BaseException("getAccountById(): Error reading account data", ApplicationConstant.SQL_ERROR_CODE, exp);
		} finally {
			DbUtils.closeQuietly( rs);
		}
	}
}