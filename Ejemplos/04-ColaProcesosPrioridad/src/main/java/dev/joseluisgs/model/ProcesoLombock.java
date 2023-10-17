package dev.joseluisgs.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

@Data
@Builder
@RequiredArgsConstructor
public class ProcesoLombock {
    private final UUID id = UUID.randomUUID();
    private final LocalDateTime createdAt = LocalDateTime.now();
    private int priority = new Random().nextInt(8) - 1;
    private LocalDateTime updatedAt = LocalDateTime.now();
    private String name;
    private int duration;
}
