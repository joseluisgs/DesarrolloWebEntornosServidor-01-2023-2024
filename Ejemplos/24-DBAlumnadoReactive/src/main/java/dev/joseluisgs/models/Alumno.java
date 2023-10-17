package dev.joseluisgs.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Alumno {
    private Long id;
    private String nombre;
    @Builder.Default
    private double calificacion = 0.0;
    @Builder.Default
    private UUID uuid = UUID.randomUUID();
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}