package com.invoice.management.exception;

/**
 * Exception thrown when a person is not found.
 * This exception is typically used when attempting to retrieve, update, or delete a person that does not exist.
 */
public class PersonNotFoundException extends RuntimeException {

    /**
     * Constructs a new PersonNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public PersonNotFoundException(String message) {
        super(message);
    }
}
