package dev.joseluisgs.models;

public class Alumno {
    private Long id;
    private String nombre;

    public Alumno(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return String.format("Alumno: %d, %s", this.id, this.nombre);
    }
}
