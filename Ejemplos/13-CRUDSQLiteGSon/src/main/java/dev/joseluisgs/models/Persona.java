package dev.joseluisgs.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Mi consejo es que a partir de poco uses UUIDs para identificar a las personas.
 * y no claves autonumericas, pronto lo entenderás
 */
@Data
@Builder
public class Persona {
    private int id;
    private UUID uuid;
    private String nombre;
    private int edad;
    private LocalDate createdAt;
}
