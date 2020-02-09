package com.remittance.test;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetIterator;

import com.remittance.dao.AccountDetailsDAO;
import com.remittance.dao.TransferDetailsDAO;
import com.remittance.db.DataAccessObjectBuilder;
import com.remittance.model.AccountDetails;
import com.remittance.model.UserTransaction;

public interface BaseTestUtil{
	DataAccessObjectBuilder h2DaoFactory = DataAccessObjectBuilder.getH2Object();
	TransferDetailsDAO transferDAO = h2DaoFactory.getTransferDetailsDAO();
	AccountDetailsDAO accountDAO = h2DaoFactory.getAccountDetailsDAO();
	
	AccountDetails accountDetails = new AccountDetails(1L, "Venkat", new BigDecimal(100), "EUR");
	UserTransaction transaction = new UserTransaction("EUR", new BigDecimal(100), 3L, 4L, "31-10-18 18:34:24");
	
	default List<AccountDetails> getListOfAccountDetails(ResultSet resultSet){
		List<AccountDetails> allAccounts = new ArrayList<AccountDetails>();
		ResultSetIterator.iterable(resultSet).spliterator().forEachRemaining(rss ->{
			try {
				AccountDetails acc = new AccountDetails(resultSet.getLong("AccountId"), resultSet.getString("UserName"),
						resultSet.getBigDecimal("Balance"), resultSet.getString("CurrencyCode"));
				allAccounts.add(acc);
			} catch (Exception e) {
			}
		});
		return allAccounts;
	}
}