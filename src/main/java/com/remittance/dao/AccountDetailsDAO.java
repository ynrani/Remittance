package com.remittance.dao;

import java.util.List;

import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;

public interface AccountDetailsDAO extends BaseDAO{

    List<AccountDetails> getAllAccounts() throws BaseException;
    AccountDetails getAccountById(Long accountId) throws BaseException;
}
