package me.project.humanstarter.exceptions;

public class KafkaPropertiesException extends RuntimeException{
    public KafkaPropertiesException(){
        super("Expected topic name");
    }
    public KafkaPropertiesException(String msg) {
        super(msg);
    }
}
