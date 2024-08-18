# Invoice Management Nexus - Backend

This is the backend server for the Invoice Management Nexus system. It's built with Spring Boot and provides a robust RESTful API for managing invoices, persons, and generating comprehensive financial statistics.

## Features

- Full-featured RESTful API for CRUD operations on invoices and persons
- Advanced pagination and sorting capabilities for efficient data retrieval
- Sophisticated financial statistics generation for business insights
- Seamless MySQL database integration for reliable data persistence
- Interactive Swagger UI for comprehensive API documentation and testing

## Technologies Used

- Java 19
- Spring Boot 3.0.2
- Spring Data JPA for efficient database operations
- MySQL 8.0 for robust data storage
- Swagger UI (SpringDoc OpenAPI) for interactive API documentation
- MapStruct for streamlined DTO-Entity mapping
- Lombok for reducing boilerplate code and improving readability

## Prerequisites

Ensure you have the following installed:

- Java Development Kit (JDK) 19 or later
- Maven 3.6 or later
- MySQL 8.0 or later

## Getting Started

Follow these steps to get the backend server running locally:

1. Clone the repository:
   ```
   git clone https://github.com/your-username/invoice-management-nexus-backend.git
   ```

2. Navigate to the project directory:
   ```
   cd invoice-management-nexus-backend
   ```

3. Configure the database connection in `src/main/resources/application.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost/InvoiceDatabase?createDatabaseIfNotExist=true
       username: your_username
       password: your_password
   ```

4. Build the project:
   ```
   mvn clean install
   ```

5. Run the application:
   ```
   mvn spring-boot:run
   ```

The server will start and be accessible at [http://localhost:8080](http://localhost:8080).

## API Documentation

Once the application is running, you can explore the comprehensive API documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

This interactive documentation allows you to test API endpoints directly from your browser.

## Key Endpoints

- `/api/invoices`: Manage invoices (Create, Read, Update, Delete)
- `/api/persons`: Manage persons/clients (Create, Read, Update, Delete)
- `/api/invoices/statistics`: Retrieve detailed invoice statistics
- `/api/persons/statistics`: Access comprehensive person-related financial data

For in-depth information about request/response formats and available operations, please refer to the Swagger UI documentation.

## Testing

To run the comprehensive test suite, execute:

```
mvn test
```

This will run unit tests and integration tests to ensure the reliability of the application.

## Building for Production

To create a production-ready build, run:

```
mvn clean package
```

This command will generate a JAR file in the `target` directory, ready for deployment.

## Frontend Repository

The frontend application that complements this backend can be found at [Invoice Management Nexus Frontend](https://github.com/Trisstrender/invoice-management-nexus-frontend).