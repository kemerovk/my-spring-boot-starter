package me.project.humanstarter.util;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import me.project.humanstarter.annotations.ValidTime;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;

@Component
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
