# Rewards Program Application

This is a Spring Boot application for managing customer transactions and calculating rewards points.

## How to Run

### Prerequisites

- Java 17 or higher installed
- Maven installed

### How to Run the Application

Build the application using Maven:

- Maven:

```bash
mvn clean install
```

Run the application:

- Using Maven:

```bash
mvn spring-boot:run
```

Once the application is running, access the Swagger API documentation:
[Swagger Documentation](http://localhost:8080/swagger-ui/index.html)

## APIs Available

- Customer APIs: Manage customers' details.
    - GET /customers: Retrieve all customers.
    - POST /customers: Add a new customer.
    - GET /customers/{id}: Get customer by ID.
    - PUT /customers/{id}: Update customer details.
    - DELETE /customers/{id}: Delete a customer.

- Transaction APIs: Manage transactions.
    - GET /transactions: Retrieve all transactions.
    - POST /transactions: Add a new transaction.
    - GET /transactions/{id}: Get transaction by ID.
    - PUT /transactions/{id}: Update transaction details.
    - DELETE /transactions/{id}: Delete a transaction.

- Rewards Calculation APIs: Calculate rewards points for customers.
    - GET /rewards/calculate: Calculate rewards for the last three months.

## Technologies Used

- Spring Boot
- Swagger (OpenAPI) for API documentation
- Lombok
- Slf4j
- H2 Database
