package com.remittance.service;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.remittance.exception.BaseException;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<BaseException> {
	private static Logger logger = Logger.getLogger(ServiceExceptionMapper.class);

	/**
	 * Default Constructor
	 */
	public ServiceExceptionMapper() {
	}

	/**
	 * Return the response code based on exception raised
	 */
	public Response toResponse(BaseException exception) {
		if (logger.isDebugEnabled()) {
			logger.debug("Exception to Response");
		}
		BaseException errorResponseMsg = new BaseException();
		errorResponseMsg.setErrorCode(exception.getMessage());
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponseMsg).type(MediaType.APPLICATION_JSON).build();
	}
}