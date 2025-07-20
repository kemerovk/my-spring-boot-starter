package me.project.humanstarter.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsService {
    private final MeterRegistry registry;
    private final AtomicInteger queueSize = new AtomicInteger(0);

    private final ConcurrentHashMap<String, Counter> jobsByAuthor = new ConcurrentHashMap<>();

    public MetricsService(MeterRegistry registry) {
        this.registry = registry;

        Gauge.builder("android.queue.size", queueSize::get)
                .description("Current number of tasks in queue")
                .register(registry);
    }

    public void incrementQueueSize() {
        queueSize.incrementAndGet();
    }

    public void decrementQueueSize() {
        queueSize.decrementAndGet();
    }

    public void jobCompleted(String author) {
        jobsByAuthor.computeIfAbsent(
                author,
                k -> registry.counter("android.jobs.completed", "author", k)
        ).increment();
    }
}
