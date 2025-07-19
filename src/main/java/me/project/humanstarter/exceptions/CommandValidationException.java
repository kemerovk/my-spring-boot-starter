package me.project.humanstarter.exceptions;

public class CommandValidationException extends Exception {
    public CommandValidationException() {

    }
    public CommandValidationException(final String message) {
        super(message);
    }
}
