package me.project.humanstarter.exception_handler;

import me.project.humanstarter.exceptions.KafkaPropertiesException;
import me.project.humanstarter.exceptions.QueueOverflowException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QueueOverflowException.class)
    public ResponseEntity<?> handleQueueOverflow(QueueOverflowException ex) {
        return ResponseEntity
                .badRequest()
                .body("Queue overflow: " + ex.getMessage());
    }



}
