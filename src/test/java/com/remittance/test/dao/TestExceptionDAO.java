package com.remittance.test.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.gen5.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.remittance.dao.impl.TransferDetailsDAOImpl;
import com.remittance.db.DataAccessObject;
import com.remittance.exception.BaseException;
import com.remittance.test.BaseTestUtil;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DataAccessObject.class,TransferDetailsDAOImpl.class})
public class TestExceptionDAO implements BaseTestUtil{
	private static Logger logger = Logger.getLogger(TestExceptionDAO.class);
	@Mock
	Connection con;
	@Mock
	PreparedStatement pst;
	
	@Test(expected= BaseException.class)
	public void testAccountSqlException() throws Exception {
		logger.info("started from testAccountSqlException");
		PowerMockito.mockStatic(DataAccessObject.class);
		Mockito.when(DataAccessObject.getConnection()).thenReturn(con);
		Mockito.when(con.prepareStatement(Mockito.anyString())).thenReturn(pst);
		Mockito.doThrow(new SQLException()).when(con).rollback();
		transferDAO.transferBalance(transaction);
	}
	
	@Test
	public void TestGetTransactionDetailsByIdExceptionTest() throws BaseException, SQLException{
		Long lId = 1L;
		logger.info("started from TestGetTransactionDetailsByIdExceptionTest");
		Assertions.assertThrows(BaseException.class, () -> {
			transferDAO.getTransactionDetailsById(null);
	    });
		PowerMockito.mockStatic(DataAccessObject.class);
		Mockito.when(DataAccessObject.getConnection()).thenReturn(con);
		Mockito.when(con.prepareStatement(Mockito.anyString())).thenReturn(pst);
		Mockito.doThrow(new SQLException()).when(pst).executeQuery();
		Assertions.assertThrows(BaseException.class, () -> {
			transferDAO.getTransactionDetailsById(lId);
	    });
	}
	
	@Test
	public void testBaseExceptionClass(){
		BaseException baseException =  new BaseException("12345", "BasicError");
		Assertions.assertTrue(baseException.getErrorCode().equals("12345"));
		BaseException baseException1 =  new BaseException("12345", "BasicError",new BaseException());
		Assertions.assertTrue(baseException1.getErrorCode().equals("12345"));
	}
	
	@Test(expected= BaseException.class)
	public void testTransferData() throws Exception {
		logger.info("started from testTransferData");
		transferDAO.transferBalance(null);
	}
}