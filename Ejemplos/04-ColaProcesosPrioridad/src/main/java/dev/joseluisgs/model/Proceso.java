package dev.joseluisgs.model;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class Proceso {
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private int priority = new Random().nextInt(8) - 1;
    private LocalDateTime updatedAt = LocalDateTime.now();
    private String name;
    private int duration;


    public Proceso(String name, int duration, int priority) {
        this.name = name;
        this.duration = duration;
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Process{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", priority=" + priority +
                ", name='" + name + '\'' +
                ", updatedAt=" + updatedAt +
                ", duration=" + duration +
                '}';
    }
}
