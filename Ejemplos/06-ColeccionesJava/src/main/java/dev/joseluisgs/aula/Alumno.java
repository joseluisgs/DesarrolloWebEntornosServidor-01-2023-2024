package dev.joseluisgs.aula;

public class Alumno {
    private final String nombre;
    private final Double nota;
    private final String curso;

    public Alumno(String nombre, Double nota, String curso) {
        this.nombre = nombre;
        this.nota = nota;
        this.curso = curso;
    }

    public String getNombre() {
        return nombre;
    }

    public Double getNota() {
        return nota;
    }

    public String getCurso() {
        return curso;
    }

    @Override
    public String toString() {
        return "Alumno{" +
                "nombre='" + nombre + '\'' +
                ", nota=" + nota +
                ", curso='" + curso + '\'' +
                '}';
    }
}