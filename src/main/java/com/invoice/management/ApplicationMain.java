package com.invoice.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Invoice Project.
 * This class is responsible for bootstrapping and launching the Spring Boot application.
 */
@SpringBootApplication
public class ApplicationMain {

    /**
     * The main entry point of the application.
     *
     * @param args Command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(ApplicationMain.class, args);
    }
}
