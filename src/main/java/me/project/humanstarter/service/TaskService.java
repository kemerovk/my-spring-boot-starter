package me.project.humanstarter.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import me.project.humanstarter.data.Command;
import me.project.humanstarter.data.Priority;
import me.project.humanstarter.exceptions.QueueOverflowException;
import org.springframework.stereotype.Service;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TaskService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<Command> queue = new ArrayBlockingQueue<>(100);

    public void process(Command command) throws QueueOverflowException {
        if (command.priority() == Priority.CRITICAL) {
            logCommand(command);
        } else {
            if (!queue.offer(command)) {
                log.warn("Queue overflow: unable to enqueue command from {}", command.author());
                throw new QueueOverflowException();
            }
            log.info("Queued COMMON command from {}", command.author());
        }
    }

    @PostConstruct
    public void startQueueProcessor() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (true) {
                try {
                    Command cmd = queue.take();
                    logCommand(cmd);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Command processor thread interrupted", e);
                    break;
                } catch (Exception e) {
                    log.error("Unexpected error during command execution", e);
                }
            }
        });
        log.info("Command queue processor started");
    }

    private void logCommand(Command command) {
        log.info("Executing command: [author={}, priority={}, description={}, time={}]",
                command.author(), command.priority(), command.description(), command.time());
      //  MetricsRegistry.incrementExecuted(command.author());
    }


}
