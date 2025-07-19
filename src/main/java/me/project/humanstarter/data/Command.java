package me.project.humanstarter.data;

import jakarta.validation.constraints.Size;
import me.project.humanstarter.annotations.ValidTime;

public record Command(
        @Size(min = 1, max = 1000)
        String description,

        Priority priority,

        @Size(min = 3, max = 100)
        String author,

        @ValidTime
        String time) {
}
