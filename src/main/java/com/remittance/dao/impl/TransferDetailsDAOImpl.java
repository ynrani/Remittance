package com.remittance.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import com.remittance.dao.TransferDetailsDAO;
import com.remittance.db.DataAccessObject;
import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;
import com.remittance.model.UserTransaction;
import com.remittance.utils.ApplicationConstant;

public class TransferDetailsDAOImpl implements TransferDetailsDAO {

	private static Logger logger = Logger.getLogger(TransferDetailsDAOImpl.class);


	/***
	 * Remittance/ transferring the amount.
	 * @param userTransaction
	 * @return String
	 * @exception BaseException
	 */
	@Override
	public String transferBalance(UserTransaction userTransaction) throws BaseException {
		logger.info("TransferDetailsDAOImpl ~  transferBalance()");
		if(Objects.isNull(userTransaction)){
			throw new BaseException("transferBalance(): Error reading while checking user transaction data");
		}
		int result = -1;
		AccountDetails accountFrom = null;
		AccountDetails accountTo = null;
		String status = null;
		String errorMsg = null; 
		String transactionId = null;
		Connection conn = null;
		PreparedStatement updateRestrictStmt = null;
		PreparedStatement modifyStmt = null;
		ResultSet rs = null;
		try {
			conn = DataAccessObject.getConnection();
			conn.setAutoCommit(false);
			updateRestrictStmt = conn.prepareStatement(ApplicationConstant.SQL_LOCK_ACC_BY_ID);
			updateRestrictStmt.setLong(1, userTransaction.getFromAccountId());
			rs = updateRestrictStmt.executeQuery();
			List<AccountDetails> accountDetails = getListOfAccountDetails(rs);
			if(accountDetails != null && accountDetails.size()>0)
				accountFrom = accountDetails.get(0);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(updateRestrictStmt);
			updateRestrictStmt = conn.prepareStatement(ApplicationConstant.SQL_LOCK_ACC_BY_ID);
			updateRestrictStmt.setObject(1, userTransaction.getToAccountId());
			rs = updateRestrictStmt.executeQuery();
			List<AccountDetails> accountDetails2 = getListOfAccountDetails(rs);
			if(accountDetails2 != null && accountDetails2.size()>0)
				accountTo = accountDetails2.get(0);
			BigDecimal accountCheck = checkTransaction(accountFrom, accountTo, userTransaction, status, errorMsg);

			modifyStmt = conn.prepareStatement(ApplicationConstant.SQL_UPDATE_ACC_BALANCE);
			modifyStmt.setObject(1, accountCheck);
			modifyStmt.setObject(2, userTransaction.getFromAccountId());
			modifyStmt.addBatch();
			modifyStmt.setObject(1, accountTo.getBalance().add(userTransaction.getAmount()));
			modifyStmt.setObject(2, userTransaction.getToAccountId());
			modifyStmt.addBatch();
			int[] rowsUpdated = modifyStmt.executeBatch();
			result = rowsUpdated[0] + rowsUpdated[1];
			if (logger.isDebugEnabled()) {
				logger.debug("Number of rows updated for the transfer : " + result);
			}
			conn.commit();
			status = ApplicationConstant.SUCCESS;
		} catch (SQLException se) {
			logger.error("transferAccountBalance(): User Transaction Failed, rollback initiated for: " + userTransaction,
					se);
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException exp) {
				errorMsg = "Fail to rollback transaction";
				status = ApplicationConstant.FAILED;
				throw new BaseException(errorMsg, ApplicationConstant.SQL_ERROR_CODE, exp);
			}
		} finally {
			transactionId = auditEachTransaction(userTransaction, status, errorMsg);
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(updateRestrictStmt);
			DbUtils.closeQuietly(modifyStmt);
		}
		return transactionId;
	}

	/**
	 * Validating the transaction
	 * @param accountFrom
	 * @param accountTo
	 * @param userTransaction
	 * @param strStatus
	 * @param strErrorMsg
	 * @return
	 * @throws BaseException
	 */
	public BigDecimal checkTransaction(AccountDetails accountFrom,AccountDetails accountTo
			, UserTransaction userTransaction, String strStatus, String strErrorMsg) 
					throws BaseException {
		logger.info("TransferDetailsDAOImpl ~  checkTransaction()");
		if (accountFrom == null || accountTo == null) {
			strErrorMsg = "Failed in locking both accounts for write";
			strStatus = ApplicationConstant.FAILED;
			throw new BaseException(strErrorMsg);
		}
		if (!accountFrom.getCurrencyCode().equals(userTransaction.getCurrencyCode())) {
			strErrorMsg = "Fail to remittance, transaction curreny are different from source/destination";
			strStatus = ApplicationConstant.FAILED;
			throw new BaseException(strErrorMsg);
		}
		if (!accountFrom.getCurrencyCode().equals(accountTo.getCurrencyCode())) {
			strErrorMsg = "Fail to remittance, the source and destination account are in different currency";
			strStatus = ApplicationConstant.FAILED;
			throw new BaseException(strErrorMsg);
		}
		BigDecimal fromAccountLeftOver = accountFrom.getBalance().subtract(userTransaction.getAmount());
		if (fromAccountLeftOver.compareTo(ApplicationConstant.MOUNT_VALUE) < 0) {
			strErrorMsg = "Not enough Fund from source Account ";
			strStatus = ApplicationConstant.FAILED;
			throw new BaseException(strErrorMsg);
		}
		return fromAccountLeftOver;
	}

	/**
	 * Audit each transaction 
	 * @param userTransaction
	 * @param status
	 * @param errorMsg
	 * @return
	 * @throws BaseException
	 */
	public String auditEachTransaction(UserTransaction userTransaction, String status, String errorMsg) 
			throws BaseException {
		logger.info("TransferDetailsDAOImpl ~  auditEachTransaction()");
		if (Objects.isNull(userTransaction)) {
			throw new BaseException("User Transaction is null and cannot be recorded");
		}
		ResultSet generatedKeys = null;
		try (Connection conn = DataAccessObject.getConnection();
				PreparedStatement stmt = conn.prepareStatement(ApplicationConstant.SQL_INSERT_BANK_TRANSACTION);){
			java.sql.Timestamp  transactionTime = new java.sql.Timestamp(new java.util.Date().getTime());
			stmt.setObject(1, transactionTime.getTime());
			stmt.setObject(2, userTransaction.getFromAccountId());
			stmt.setObject(3, userTransaction.getToAccountId());
			stmt.setObject(4, userTransaction.getAmount());
			stmt.setObject(5, "IMPS/NFT");
			stmt.setObject(6, userTransaction.getCurrencyCode());
			stmt.setObject(7, status);
			stmt.setObject(8, errorMsg);
			stmt.setTimestamp(9, transactionTime);
			int affectedRows = stmt.executeUpdate();
			if (affectedRows == 0) {
				logger.error("insertTransaction(): no rows affected --> inserting transaction failed.");
				throw new BaseException("Insertion Transaction Cannot be recorded");
			}
			generatedKeys = stmt.getGeneratedKeys();
			if (!Objects.isNull(transactionTime.getTime())) {
				return transactionTime.getTime() +" - "+status;
			} else {
				logger.error("No ID obtained,recording transaction failed.");
				throw new BaseException("Transaction Cannot be created");
			}
		} catch (SQLException exp) {
			logger.error("Error while Inserting Transaction  " + userTransaction.toString());
			throw new BaseException("insertTransaction(): Error creating user transaction " +  userTransaction.toString()
					, ApplicationConstant.SQL_ERROR_CODE, exp);
		} finally {
			DbUtils.closeQuietly(generatedKeys);
		}
	}

	@Override
	public String getTransactionDetailsById(Long transactionId) throws BaseException {
		logger.info("TransferDetailsDAOImpl ~  getTransactionDetailsById()");
		if(Objects.isNull(transactionId)) {
			throw new BaseException("getTransactionById(): Transaction Id should not be empty.");
		}
		ResultSet rs = null;
		StringBuffer result = new StringBuffer();
		try (Connection conn = DataAccessObject.getConnection();
				PreparedStatement stmt = conn.prepareStatement(ApplicationConstant.SQL_GET_TX_BY_ID);
				) {
			stmt.setLong(1, transactionId);
			 rs = stmt.executeQuery();
			while(rs.next()){
					if(Objects.nonNull(rs.getString("ErrorMsg")))
							result.append(rs.getString("Status")).append(" - ").append(rs.getString("ErrorMsg"));
					else
					result.append("Source Account :").append(rs.getString("AccountIdFrom")).append(" ,Destination Account :").append(rs.getString("AccountIdTo")).append(", Amount :").append(rs.getString("Amount")).append(",Status :").append(rs.getString("Status"));
			}
			return result.toString();
		} catch (SQLException exp) {
			throw new BaseException("getTransactionById(): Error reading transaction data"
					, ApplicationConstant.SQL_ERROR_CODE, exp);
		}finally{
			DbUtils.closeQuietly(rs);
		}
	}
}