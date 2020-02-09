package com.remittance.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.ResultSetIterator;
import org.apache.log4j.Logger;

import com.remittance.model.AccountDetails;

public interface BaseDAO{
	Logger logger = Logger.getLogger(BaseDAO.class);
	
	default List<AccountDetails> getListOfAccountDetails(ResultSet resultSet){
		List<AccountDetails> allAccounts = new ArrayList<AccountDetails>();
		ResultSetIterator.iterable(resultSet).spliterator().forEachRemaining(rss ->{
			try {
				AccountDetails acc = new AccountDetails(resultSet.getLong("AccountId"), resultSet.getString("UserName"),
						resultSet.getBigDecimal("Balance"), resultSet.getString("CurrencyCode"));
				allAccounts.add(acc);
			} catch (Exception exp) {
					logger.error("getListOfAccountDetails() ~ Error iterating the data"+ exp.getMessage());
			}
		});
		return allAccounts;
	}
}
