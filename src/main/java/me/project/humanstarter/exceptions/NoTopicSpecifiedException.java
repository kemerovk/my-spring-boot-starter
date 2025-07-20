package me.project.humanstarter.exceptions;

public class NoTopicSpecifiedException extends RuntimeException{
    public NoTopicSpecifiedException(){
        super("Expected topic name");
    }
    public NoTopicSpecifiedException(String msg) {
        super(msg);
    }
}
