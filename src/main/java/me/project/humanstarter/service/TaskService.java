package me.project.humanstarter.service;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.project.humanstarter.data.Command;
import me.project.humanstarter.exceptions.QueueOverflowException;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
@Service
public class TaskService {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final PriorityBlockingQueue<Command> queue = new PriorityBlockingQueue<>();

    @Autowired
    private MetricsService metricsService;


    public void process(@Valid Command command) throws QueueOverflowException {
        boolean isAdded = queue.offer(command);
        if (!isAdded) {
            log.warn("Queue overflow: unable to enqueue command from {}", command.author());
            throw new QueueOverflowException("Command queue is full");
        }
        metricsService.incrementQueueSize();
        log.info("Queued command '{}' from '{}', priority: {}", command.description(), command.author(), command.priority());
    }

    @PostConstruct
    private void startQueueProcessor() {
        executor.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Command cmd = queue.take();
                    metricsService.decrementQueueSize();
                    metricsService.jobCompleted(cmd.author());
                    log.info("Executing command: [description={}, author={}, priority={}, time={}]", cmd.description(), cmd.author(), cmd.priority(), cmd.time());
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
        log.info("Command queue processor started");
    }
}