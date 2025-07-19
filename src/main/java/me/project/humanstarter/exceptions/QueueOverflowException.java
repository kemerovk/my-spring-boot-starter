package me.project.humanstarter.exceptions;

public class QueueOverflowException extends Exception {
    public QueueOverflowException(String msg) {
        super(msg);
    }

    public QueueOverflowException() {
        super("Queue overflow");
    }

}
