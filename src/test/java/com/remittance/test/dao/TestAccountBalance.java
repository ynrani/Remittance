package com.remittance.test.dao;

import static junit.framework.TestCase.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.remittance.dao.impl.TransferDetailsDAOImpl;
import com.remittance.db.DataAccessObject;
import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;
import com.remittance.model.UserTransaction;
import com.remittance.test.BaseTestUtil;
import com.remittance.utils.ApplicationConstant;

public class TestAccountBalance implements BaseTestUtil{

	private static Logger log = Logger.getLogger(TestAccountBalance.class);
	
	@BeforeClass
	public static void setup() {
		h2DaoFactory.populateTestData(ApplicationConstant.SQL_FILE_NAME);
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testAccountSingleThreadSameCurrencyTransfer() throws BaseException {
		BigDecimal transferAmount = new BigDecimal(50.01234).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("USD", transferAmount, 3L, 4L, "31-10-18 18:34:24");
		UserTransaction transaction2 = new UserTransaction("EUR", transferAmount, 4L, 3L, "30-10-18 18:34:24");
		long startTime = System.currentTimeMillis();
		transferDAO.transferBalance(transaction);
		long endTime = System.currentTimeMillis();
		log.info("TransferAccountBalance finished, time taken: " + (endTime - startTime) + "ms");
		AccountDetails accountFrom = accountDAO.getAccountById(3L);
		AccountDetails accountTo = accountDAO.getAccountById(4L);
		log.debug("Account From: " + accountFrom);

		log.debug("Account From: " + accountTo);

		assertTrue(
				accountFrom.getBalance().compareTo(new BigDecimal(449.9877).setScale(4, RoundingMode.HALF_EVEN)) == 0);
		assertTrue(accountTo.getBalance().equals(new BigDecimal(550.0123).setScale(4, RoundingMode.HALF_EVEN)));
		assertTrue(!transaction.equals(null));
		assertTrue(transaction.hashCode()== transaction.hashCode());
		assertTrue(!transaction.equals(transaction2));
	}
	
	@Test
	public void testTransferFailOnDBLock() throws BaseException, SQLException {
		final String LOCK_ACCOUNT_STATEMNT = "SELECT AccountId,Username,Balance,CurrencyCode FROM AccountDetails WHERE AccountId = 4 FOR UPDATE";
		Connection conn = null;
		PreparedStatement lockStmt = null;
		ResultSet rs = null;
		AccountDetails accountFrom = null;

		try {
			conn = DataAccessObject.getConnection();
			conn.setAutoCommit(false);
			// lock account for writing:
			lockStmt = conn.prepareStatement(LOCK_ACCOUNT_STATEMNT);
			rs = lockStmt.executeQuery();
			List<AccountDetails> accountDetails2 = getListOfAccountDetails(rs);
			if(accountDetails2 != null && accountDetails2.size()>0)
				accountFrom = accountDetails2.get(0);
			if (accountFrom == null) {
				throw new BaseException("Error during the test in locking ~ " + LOCK_ACCOUNT_STATEMNT);
			}
			BigDecimal transferAmount = new BigDecimal(50).setScale(4, RoundingMode.HALF_EVEN);
			UserTransaction transaction = new UserTransaction("USD", transferAmount, 3L, 4L, "31-10-18 18:34:24");
			transferDAO.transferBalance(transaction);
			conn.commit();
		} catch (Exception e) {
			log.error("Exception occurred, initiate a rollback",e);
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException re) {
				log.error("Fail to rollback transaction", re);
			}
		} finally {
			DbUtils.closeQuietly(conn);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(lockStmt);
		}
		// now inspect account 3 and 4 to verify no transaction occurred
		BigDecimal originalBalance = new BigDecimal(550.0123).setScale(4, RoundingMode.HALF_EVEN);
		BigDecimal beforeBalance2  = accountDAO.getAccountById(4L).getBalance();
		log.info("balance ----> "+beforeBalance2);
		assertTrue(beforeBalance2.equals(originalBalance));
	}
	
	@Test(expected= BaseException.class)
	public void testAuditEachTransaction() throws BaseException {
		TransferDetailsDAOImpl dao = new TransferDetailsDAOImpl();
		dao.auditEachTransaction(null, null, null);
	}
	
	@Test(expected= BaseException.class)
	public void testCheckTransaction() throws BaseException {
		TransferDetailsDAOImpl dao = new TransferDetailsDAOImpl();
		dao.checkTransaction(null,null,null, null, null);
	}
	
	@Test(expected= BaseException.class)
	public void testWithAccountsCheckTransaction() throws BaseException {
		TransferDetailsDAOImpl dao = new TransferDetailsDAOImpl();
		AccountDetails details1 = accountDAO.getAccountById(3L);
		UserTransaction transaction = new UserTransaction("USD", new BigDecimal(5000).setScale(4, RoundingMode.HALF_EVEN), 3L, 1L, "31-10-18 18:34:24");
		AccountDetails details2 = accountDAO.getAccountById(1L);
		
		dao.checkTransaction(details1,details2,transaction, null, null);
	}
}