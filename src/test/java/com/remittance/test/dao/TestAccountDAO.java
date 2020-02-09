package com.remittance.test.dao;

import static junit.framework.TestCase.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;
import com.remittance.test.BaseTestUtil;
import com.remittance.utils.ApplicationConstant;

public class TestAccountDAO implements BaseTestUtil{
	@BeforeClass
	public static void setup() {
	}

	@After
	public void tearDown() {

	}

	@Test(expected= RuntimeException.class )
	public void testPopulateTestData() {
		h2DaoFactory.populateTestData(ApplicationConstant.SQL_TEMP_FILE_NAME);
	}
	
	@Test
	public void testGetAllAccounts() throws BaseException {
		List<AccountDetails> allAccounts = accountDAO.getAllAccounts();
		assertTrue(allAccounts.size() > 1);
		AccountDetails account1 = allAccounts.get(0);
		AccountDetails account2 = allAccounts.get(0);
		AccountDetails account3 = new AccountDetails("test", new BigDecimal(12345), "");
		assertTrue(!account1.equals(null));
		assertTrue(account1.hashCode()== account2.hashCode());
		assertTrue(!account3.equals(account2));
		account3 = new AccountDetails(account2.getAccountId(),account2.getUserName(), new BigDecimal(12345), "");
		assertTrue(!account3.equals(account2));
		account3 = new AccountDetails(account2.getAccountId(),account2.getUserName(), account2.getBalance(), "");
		assertTrue(!account3.equals(account2));
	}

	@Test
	public void testGetAccountById() throws BaseException {
		AccountDetails account = accountDAO.getAccountById(1L);
		assertTrue(account.getUserName().equals("Vincent"));
	}
	
	@Test(expected= BaseException.class)
	public void testGetAccountByIdWithNull() throws BaseException {
		accountDAO.getAccountById(null);
	}

	@Test(expected= RuntimeException.class)
	public void testGetNonExistingAccById() throws BaseException {
		AccountDetails account = accountDAO.getAccountById(100L);
		assertTrue(account == null);
	}
	
	@Test
	public void testGetTransactionById() throws BaseException {
		String res = transferDAO.getTransactionDetailsById(20181103233045L);
		assertTrue(res.contains("SUCCESS"));
	}
	
	@Test
	public void testAccountEqualWithHashCode() throws BaseException {
		AccountDetails account1 = accountDetails;
		AccountDetails account3 = new AccountDetails("test", new BigDecimal(12345), "");
		assertTrue(!account1.equals(null));
		assertTrue(accountDetails.hashCode()== account1.hashCode());
		assertTrue(!account3.equals(account1));
		account3 = new AccountDetails(account1.getAccountId(),account1.getUserName(), new BigDecimal(12345), "");
		assertTrue(!account3.equals(account1));
		account3 = new AccountDetails(account1.getAccountId(),account1.getUserName(), account1.getBalance(), "");
		assertTrue(!account3.equals(account1));
	}
}