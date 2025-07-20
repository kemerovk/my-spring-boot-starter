package me.project.humanstarter.exceptions;

public class QueueOverflowException extends RuntimeException {
    public QueueOverflowException(String msg) {
        super(msg);
    }

    public QueueOverflowException() {
        super("Queue overflow");
    }

}
