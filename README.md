# Rest API for Remittance

A Java RESTful API for Remittance among accounts

### Technologies
- Java 8
- Mockitos
- JAX-RS API
- H2 database
- Log4j
- Jetty Container
- Apache HTTP Client


### How to run
```sh
mvn exec:java
```

Application starts a jetty server on localhost port 8980 and used in memory database through H2 data To view

### Http Status
- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### REST Services available

HTTP METHODS		~			PATH					~			Description
====================================================================================
GET					~ /account/{accountId}				~	Used to get account details by account Id
GET					~ /account/all						~	Used to get all account details
GET					~ /account/{accountId}/balance		~	Used to get balance of specific account through account Id
POST				~ /transaction						~	this service will send from, to account details with amount in json
GET					~ /transaction/{transactionId}		~	this service used to fetch transaction details through transaction Id.

#### Transferring the amount from one account to another account through json :
{  
   "currencyCode":"USD",
   "amount":10.0000,
   "fromAccountId":3L,
   "toAccountId":1L
}

#### Java 8 Features Used:
- Default method in interface
- Stream API
- Spliterator
- Preparedstatement new methods

#### Code Coverage % : 81%