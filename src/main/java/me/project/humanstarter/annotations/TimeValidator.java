package me.project.humanstarter.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Instant;
import java.time.format.DateTimeParseException;

public class TimeValidator implements ConstraintValidator<ValidTime, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        try {
            Instant.parse(value);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
