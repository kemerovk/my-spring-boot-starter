package me.project.humanstarter.exceptions;

public class CommandValidationException extends RuntimeException {
    public CommandValidationException() {

    }
    public CommandValidationException(final String message) {
        super(message);
    }
}
