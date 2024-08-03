package cz.itnetwork.controller.advice;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.webjars.NotFoundException;

/**
 * Global exception handler for entity not found exceptions.
 * This class handles exceptions related to entities not being found in the database.
 */
@ControllerAdvice
public class EntityNotFoundExceptionAdvice {

    /**
     * Handles NotFoundException and EntityNotFoundException by returning a 404 status.
     * This method is called automatically when either of these exceptions is thrown in any controller.
     */
    @ExceptionHandler({NotFoundException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFoundException() {
        // Method intentionally left empty as we only need to set the response status
    }
}