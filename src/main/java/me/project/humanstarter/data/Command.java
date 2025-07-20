package me.project.humanstarter.data;

import jakarta.validation.constraints.Size;
import me.project.humanstarter.annotations.ValidTime;

import java.time.Instant;

public record Command(
        @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters") String description,
        Priority priority,
        @Size(min = 3, max = 100, message = "Author must be between 3 and 100 characters") String author,
        @ValidTime String time)
        implements Comparable<Command> {

    @Override
    public int compareTo(Command other) {
        int cmp = Integer.compare(other.priority.ordinal(), this.priority.ordinal());
        if (cmp != 0) return cmp;

        return Instant.parse(this.time).compareTo(Instant.parse(other.time));
    }
}