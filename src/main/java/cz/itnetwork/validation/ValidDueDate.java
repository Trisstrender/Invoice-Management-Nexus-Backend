package cz.itnetwork.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDueDateValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDueDate {
    String message() default "Due date must be after the issue date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}