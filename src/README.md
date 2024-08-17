# Invotriss Backend

This is the backend server for the Invotriss invoice management system. It's built with Spring Boot and provides a RESTful API for managing invoices, persons, and generating financial statistics.

## Features

- RESTful API for CRUD operations on invoices and persons
- Pagination and sorting for list endpoints
- Financial statistics generation
- MySQL database integration
- Swagger UI for API documentation

## Technologies Used

- Java 17
- Spring Boot 3.0.2
- Spring Data JPA
- MySQL
- Swagger UI (SpringDoc OpenAPI)
- MapStruct for DTO mapping
- Lombok for reducing boilerplate code

## Prerequisites

Before you begin, ensure you have met the following requirements:

- Java Development Kit (JDK) 17 or later
- Maven 3.6 or later
- MySQL 8.0 or later

## Getting Started

To get the backend server running locally:

1. Clone the repository:
   ```
   git clone https://github.com/your-username/invotriss-backend.git
   ```

2. Navigate to the project directory:
   ```
   cd invotriss-backend
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

The server should now be running on [http://localhost:8080](http://localhost:8080).

## API Documentation

Once the application is running, you can view the API documentation at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Main Endpoints

- `/api/invoices`: CRUD operations for invoices
- `/api/persons`: CRUD operations for persons
- `/api/invoices/statistics`: Get invoice statistics
- `/api/persons/statistics`: Get person statistics

For detailed information about request/response formats, please refer to the Swagger UI documentation.

## Testing

To run the tests, execute:

```
mvn test
```

## Building for Production

To build the application for production, run:

```
mvn clean package
```

This will create a JAR file in the `target` directory.