package com.remittance.db;

import com.remittance.dao.AccountDetailsDAO;
import com.remittance.dao.TransferDetailsDAO;

public interface DataAccessObjectBuilder {

	public abstract AccountDetailsDAO getAccountDetailsDAO();
	
	public abstract TransferDetailsDAO getTransferDetailsDAO();

	public abstract void populateTestData(String fileName);

	public static DataAccessObjectBuilder getH2Object() {
		return new DataAccessObject();
	}
	
	public void truncateTestedData(String dropScript);
}
