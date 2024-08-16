package cz.itnetwork.exception;

/**
 * Exception thrown when an invoice is not found.
 * This exception is typically used when attempting to retrieve, update, or delete an invoice that does not exist.
 */
public class InvoiceNotFoundException extends RuntimeException {

    /**
     * Constructs a new InvoiceNotFoundException with the specified detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the getMessage() method)
     */
    public InvoiceNotFoundException(String message) {
        super(message);
    }
}