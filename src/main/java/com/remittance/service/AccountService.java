package com.remittance.service;

import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.remittance.exception.BaseException;
import com.remittance.model.AccountDetails;

/**
 * Account Service 
 * @author Venkat Sivapuram 
 */
@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountService implements BaseService{
       
    /**
     * Find all accounts
     * @return
     * @throws BaseException
     */
    @GET
    @Path("/all")
    public List<AccountDetails> getAllAccounts() throws BaseException {
        return daoFactory.getAccountDetailsDAO().getAllAccounts();
    }

    /**
     * Find by account id
     * @param accountId
     * @return
     * @throws BaseException
     */
    @GET
    @Path("/{accountId}")
    public AccountDetails getAccount(@PathParam("accountId") Long accountId) throws BaseException {
        return daoFactory.getAccountDetailsDAO().getAccountById(accountId);
    }
    
    /**
     * Find balance by account Id
     * @param accountId
     * @return
     * @throws BaseException
     */
    @GET
    @Path("/{accountId}/balance")
    public BigDecimal getBalance(@PathParam("accountId") Long accountId) throws BaseException {
        final AccountDetails account = daoFactory.getAccountDetailsDAO().getAccountById(accountId);

        if(account == null){
            throw new WebApplicationException("Account not found", Response.Status.NOT_FOUND);
        }
        return account.getBalance();
    }
}