package com.remittance.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ApplicationConstant {
	
	public final static String SQL_ERROR_CODE = "250001";
	
	public static final BigDecimal MOUNT_VALUE = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILED";
	
	public final static String SQL_TEMP_FILE_NAME = "src/test/resources/Fault.sql";
	public final static String SQL_FILE_NAME = "src/test/resources/Db_Structure.sql";
	public final static String SQL_DELETE_SCRIPT = "src/test/resources/Drop_Script.sql";
	public final static String SQL_GET_ACC_BY_ID = "SELECT AccountId,UserName,Balance,CurrencyCode FROM AccountDetails WHERE AccountId = ? ";
	public final static String SQL_LOCK_ACC_BY_ID = "SELECT AccountId,UserName,Balance,CurrencyCode FROM AccountDetails WHERE AccountId = ? FOR UPDATE";
	public final static String SQL_UPDATE_ACC_BALANCE = "UPDATE AccountDetails SET Balance = ? WHERE AccountId = ? ";
	public final static String SQL_INSERT_BANK_TRANSACTION = "INSERT INTO TransactionDetails(Id,AccountIdFrom,AccountIdTo, Amount, Transaction_Type, CurrencyCode, Status, ErrorMsg, TransactionTime) "
			+ "VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public final static String SQL_GET_TX_BY_ID = "SELECT Status,ErrorMsg,AccountIdFrom,AccountIdTo, Amount FROM TransactionDetails WHERE Id = ? ";
	public final static String SQL_GET_ALL_ACC = "SELECT AccountId,UserName,Balance,CurrencyCode FROM AccountDetails";
	
	/*
	 * DB related properties loaded
	 */
	public static final String DB_DRIVER = ApplicationUtil.getStringProperty("driver");
	public static final String DB_CONNECTION_URL = ApplicationUtil.getStringProperty("connection_url");
	public static final String DB_USER = ApplicationUtil.getStringProperty("user");
	public static final String DB_PASSWORD = ApplicationUtil.getStringProperty("password");
}