package com.remittance.service;

import java.util.Objects;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.remittance.exception.BaseException;
import com.remittance.model.UserTransaction;
import com.remittance.utils.ApplicationConstant;
import com.remittance.utils.ApplicationUtil;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionService implements BaseService{	 
	/**
	 * Transfer fund between two accounts.
	 * @param transaction
	 * @return
	 * @throws BaseException
	 */
	@POST
	public Response transferFund(UserTransaction transaction) throws BaseException {
		if(Objects.isNull(transaction)) {
			throw new WebApplicationException("Transaction param can not be NULL", Response.Status.BAD_REQUEST);
		}
		String currency = transaction.getCurrencyCode();
		if (ApplicationUtil.validateCcyCode(currency)) {
			String transactionId = daoFactory.getTransferDetailsDAO().transferBalance(transaction);
			if (!Objects.isNull(transactionId) && transactionId.contains(ApplicationConstant.SUCCESS)) {
				return Response.ok(transactionId).build();
			} else {
				// transaction failed
				throw new WebApplicationException("Transaction failed, "+transactionId, Response.Status.BAD_REQUEST);
			}
		} else {
			throw new WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST);
		}
	}
	
	@GET
	@Path("/{transactionId}")
    public String getTransactionById(@PathParam("transactionId") Long transactionId) throws BaseException {
        final String accTxDetails = daoFactory.getTransferDetailsDAO().getTransactionDetailsById(transactionId);

        if(accTxDetails == null){
            throw new WebApplicationException("Transaction not found", Response.Status.NOT_FOUND);
        }
        return accTxDetails;
    }
}