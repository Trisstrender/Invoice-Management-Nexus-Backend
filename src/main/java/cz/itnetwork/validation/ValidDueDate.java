package cz.itnetwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to validate that the due date of an invoice is after its issue date.
 * This annotation can be applied to classes, typically invoice-related classes.
 */
@Documented
@Constraint(validatedBy = ValidDueDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDueDate {

    /**
     * Error message to be used when the validation fails.
     * @return the error message
     */
    String message() default "Due date must be after the issue date";

    /**
     * Groups the constraint belongs to.
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * Payload associated with the constraint.
     * @return the payload
     */
    Class<? extends Payload>[] payload() default {};
}