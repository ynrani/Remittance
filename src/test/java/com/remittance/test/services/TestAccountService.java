package com.remittance.test.services;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.remittance.model.AccountDetails;

public class TestAccountService extends TestService {

	@Test
    public void testGetAccountByUserName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountDetails account = mapper.readValue(jsonString, AccountDetails.class);
        assertTrue(account.getUserName().equals("Vincent"));
    }

    @Test
    public void testGetAllAccounts() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/all").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String jsonString = EntityUtils.toString(response.getEntity());
        AccountDetails[] accounts = mapper.readValue(jsonString, AccountDetails[].class);
        assertTrue(accounts.length > 0);
    }

    @Test
    public void testGetAccountBalance() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/1/balance").build();
        HttpGet request = new HttpGet(uri);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 200);
        String balance = EntityUtils.toString(response.getEntity());
        BigDecimal res = new BigDecimal(balance).setScale(4, RoundingMode.HALF_EVEN);
        BigDecimal db = new BigDecimal(200).setScale(4, RoundingMode.HALF_EVEN);
        assertTrue(res.equals(db));
    }
}