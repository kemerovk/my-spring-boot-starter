package me.project.humanstarter.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.project.humanstarter.configuration.TimeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTime {
    String message() default "Invalid date format. Expected ISO 8601 format";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
