--This script is used for unit test cases, DO NOT CHANGE!

DROP TABLE IF EXISTS AccountDetails;

CREATE TABLE AccountDetails (AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
UserName VARCHAR(30),
Balance DECIMAL(19,4),
CurrencyCode VARCHAR(30),
TransactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

CREATE UNIQUE INDEX id_AccountDetails on AccountDetails(UserName);

INSERT INTO AccountDetails (AccountId,UserName,Balance,CurrencyCode, TransactionTime) VALUES (1,'Vincent',200.0000,'EUR', '2018-11-03 12:47:52.69');
INSERT INTO AccountDetails (AccountId,UserName,Balance,CurrencyCode, TransactionTime) VALUES (2,'Paul',1000.0000,'EUR', '2018-11-03 12:47:52.69');
INSERT INTO AccountDetails (AccountId,UserName,Balance,CurrencyCode, TransactionTime) VALUES (3,'David',500.0000,'USD', '2018-11-03 12:47:52.69');
INSERT INTO AccountDetails (AccountId,UserName,Balance,CurrencyCode, TransactionTime) VALUES (4,'Krishna',500.0000,'USD', '2018-11-03 12:47:52.69');



/*
 * This follwoing script used for transaction details
 */
DROP TABLE IF EXISTS TransactionDetails;

CREATE TABLE TransactionDetails (Id LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
AccountIdFrom LONG not null,
AccountIdTo LONG not null,
Amount DECIMAL(19,4),
Transaction_Type VARCHAR(20),
CurrencyCode VARCHAR(30),
Status VARCHAR(30),
ErrorMsg VARCHAR(1000),
TransactionTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
);

CREATE UNIQUE INDEX id_TransactionDetails on TransactionDetails(Id);

INSERT INTO TransactionDetails (Id,AccountIdFrom,AccountIdTo, Amount, Transaction_Type, CurrencyCode, Status, ErrorMsg, TransactionTime) 
VALUES (20181103233045, 1,2,50.0000, 'IMPS', 'USD', 'SUCCESS', null, '2018-11-04 06:47:52.69');




