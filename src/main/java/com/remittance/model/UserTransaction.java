package com.remittance.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserTransaction {

	@JsonProperty(required = true)
	private String currencyCode;

	@JsonProperty(required = true)
	private BigDecimal amount;

	@JsonProperty(required = true)
	private Long fromAccountId;

	@JsonProperty(required = true)
	private Long toAccountId;
	
	@JsonProperty(required = true)
	private String txTime;

	public UserTransaction() {
	}

	public UserTransaction(String currencyCode, BigDecimal amount, Long fromAccountId, Long toAccountId
			, String txTime) {
		this.currencyCode = currencyCode;
		this.amount = amount;
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.txTime = txTime;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Long getFromAccountId() {
		return fromAccountId;
	}

	public Long getToAccountId() {
		return toAccountId;
	}
	
	public String getTxTime() {
		return txTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		UserTransaction that = (UserTransaction) o;

		if (!currencyCode.equals(that.currencyCode))
			return false;
		if (!amount.equals(that.amount))
			return false;
		if (!fromAccountId.equals(that.fromAccountId))
			return false;
		if (!txTime.equals(that.txTime))
			return false;
		return toAccountId.equals(that.toAccountId);

	}

	@Override
	public int hashCode() {
		int result = currencyCode.hashCode();
		result = 31 * result + amount.hashCode();
		result = 31 * result + fromAccountId.hashCode();
		result = 31 * result + toAccountId.hashCode();
		result = 31 * result + txTime.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "UserTransaction{" + "currencyCode='" + currencyCode + '\'' + ", amount=" + amount + ", fromAccountId="
				+ fromAccountId + ", toAccountId=" + toAccountId +  ", txTime=" + txTime + '}';
	}
}
