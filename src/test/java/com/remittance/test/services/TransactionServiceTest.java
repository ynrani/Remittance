package com.remittance.test.services;


import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.gen5.api.Assertions;

import com.remittance.exception.BaseException;
import com.remittance.model.UserTransaction;

/**
 * Integration testing for RestAPI
 * Test data are initialised from src/test/resources/db_structure.sql
 * <p>
 */
public class TransactionServiceTest extends TestService {
	
	@Test
	public void testTransactionEnoughFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("USD", amount, 3L, 4L, "31-05-18 18:34:24");

		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}
	
	@Test
	public void testTransactionIncorrectFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(10000).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("USD", amount, 3L, 4L, "31-05-18 18:34:24");
		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 500);
	}
	
	@Test
	public void testNullTransactionEnoughFund() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		UserTransaction transaction = null;
		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 400);
	}

	@Test
	public void testTransactionDifferentCcy() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("USD", amount, 1L, 4L, "31-05-13 11:34:24");
		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 500);
	}
	
	@Test
	public void testInvalidTransactionDifferentCcy() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(100).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("Test", amount, 1L, 4L, "31-05-13 11:34:24");
		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 400);
	}

	@Test
	public void testGetTransactionById() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction/20181103233045").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		assertTrue(jsonString.contains("SUCCESS"));
	}
	
	@Test
	public void testGetInvalidTransactionById() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction/2013045").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
		String jsonString = EntityUtils.toString(response.getEntity());
		assertTrue(jsonString.contains(""));
	}
 
	@Test
	public void testGetTransactionByIdInvalidId() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction/null").build();
		HttpGet request = new HttpGet(uri);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 404);
	}
	
	@Test
	public void testTransactionDO() throws IOException, URISyntaxException {
		URI uri = builder.setPath("/transaction").build();
		BigDecimal amount = new BigDecimal(10).setScale(4, RoundingMode.HALF_EVEN);
		UserTransaction transaction = new UserTransaction("USD", amount, 3L, 4L, "04-11-18 12:34:24");
		String jsonInString = mapper.writeValueAsString(transaction);
		StringEntity entity = new StringEntity(jsonInString);
		HttpPost request = new HttpPost(uri);
		request.setHeader("Content-type", "application/json");
		request.setEntity(entity);
		HttpResponse response = client.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();
		assertTrue(statusCode == 200);
	}
	
	@Test
	public void testBaseExceptionClass(){
		BaseException baseException =  new BaseException("12345", "BasicError");
		Assertions.assertTrue(baseException.getErrorCode().equals("12345"));
		BaseException baseException1 =  new BaseException("12345", "BasicError",new BaseException());
		Assertions.assertTrue(baseException1.getErrorCode().equals("12345"));
	}
}