package me.project.humanstarter.annotations;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTime {
    String message() default "Invalid date format. Expected ISO 8601 format";
}
