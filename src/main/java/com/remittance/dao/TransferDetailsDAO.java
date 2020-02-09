package com.remittance.dao;

import com.remittance.exception.BaseException;
import com.remittance.model.UserTransaction;

public interface TransferDetailsDAO extends BaseDAO{
	String getTransactionDetailsById(Long transactionId) throws BaseException;
    String transferBalance(UserTransaction userTransaction) throws BaseException;
}
