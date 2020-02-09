package com.remittance.service;

import com.remittance.db.DataAccessObjectBuilder;

public interface BaseService{
	public final DataAccessObjectBuilder daoFactory = DataAccessObjectBuilder.getH2Object();
}
