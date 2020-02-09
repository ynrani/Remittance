package com.remittance.test.util;

import org.junit.Test;
import org.junit.gen5.api.Assertions;

import com.remittance.utils.ApplicationUtil;

public class ApplicationUtilTest{
	
	@Test
	public void testGetIntegerProperty(){
		Assertions.assertTrue(1 == ApplicationUtil.getIntegerProperty("test",1));
	}
	
	@Test
	public void testgetStringProperty(){
		Assertions.assertTrue("test".equals(ApplicationUtil.getStringProperty("test","test")));
	}
	
	@Test
	public void testValidateCcyCode(){
		Assertions.assertTrue(!ApplicationUtil.validateCcyCode("test"));
	}
	
	@Test(expected= NullPointerException.class)
	public void testloadConfig(){
		ApplicationUtil.loadConfig("test.txt");
	}
	
	@Test
	public void testFilNullloadConfig(){
		ApplicationUtil.loadConfig(null);
	}
}