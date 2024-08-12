package cz.itnetwork.validation;

import cz.itnetwork.dto.InvoiceDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidDueDateValidator implements ConstraintValidator<ValidDueDate, InvoiceDTO> {

    @Override
    public boolean isValid(InvoiceDTO invoice, ConstraintValidatorContext context) {
        if (invoice.getIssued() == null || invoice.getDueDate() == null) {
            return true; // Let @NotNull handle this
        }
        return invoice.getDueDate().isAfter(invoice.getIssued());
    }
}