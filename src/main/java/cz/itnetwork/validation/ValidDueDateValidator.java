package cz.itnetwork.validation;

import cz.itnetwork.dto.InvoiceDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for the {@link ValidDueDate} annotation.
 * This class implements the logic to check if the due date is after the issue date in an invoice.
 */
public class ValidDueDateValidator implements ConstraintValidator<ValidDueDate, InvoiceDTO> {

    /**
     * Initializes the validator.
     * This method is empty as no initialization is needed for this validator.
     *
     * @param constraintAnnotation the annotation instance
     */
    @Override
    public void initialize(ValidDueDate constraintAnnotation) {
    }

    /**
     * Validates that the due date is after the issue date in the given invoice.
     *
     * @param invoice the invoice to validate
     * @param context the constraint validator context
     * @return true if the due date is after the issue date, or if either date is null; false otherwise
     */
    @Override
    public boolean isValid(InvoiceDTO invoice, ConstraintValidatorContext context) {
        if (invoice.getIssued() == null || invoice.getDueDate() == null) {
            return true;
        }
        return invoice.getDueDate().isAfter(invoice.getIssued());
    }
}