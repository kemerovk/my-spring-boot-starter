package me.project.humanstarter.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.project.humanstarter.data.Command;
import me.project.humanstarter.exceptions.QueueOverflowException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TaskService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final PriorityBlockingQueue<Command> queue = new PriorityBlockingQueue<>();

    public void process(@Valid Command command) throws QueueOverflowException {
        if (!queue.offer(command)) {
            log.warn("Queue overflow: unable to enqueue command from {}", command.author());
            throw new QueueOverflowException("Command queue is full");
        }
        log.info("Queued command '{}' from '{}', priority: {}", command.description(), command.author(), command.priority());
    }

    @PostConstruct
    public void startQueueProcessor() {
        try {
            Thread.sleep(10000);
        }
        catch (InterruptedException e) {
            log.error("Thread interrupted", e);
        }
        executor.submit(() -> {
            log.info("Command queue processor started");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Command cmd = queue.take();
                    executeCommand(cmd);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Command processor thread interrupted", e);
                    break;
                } catch (Exception e) {
                    log.error("Unexpected error during command execution", e);
                }
            }
        });
    }

    private void executeCommand(Command command) {
        log.info("Executing command: [author={}, priority={}, description={}, time={}]",
                command.author(), command.priority(), command.description(), command.time());
    }
}


