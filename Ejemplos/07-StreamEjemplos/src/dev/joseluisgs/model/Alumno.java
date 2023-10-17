package dev.joseluisgs.model;

public class Alumno {
    private int id;
    private String dni;
    private String nombre;
    private String apellidos;
    private String nombreCurso;
    private double nota;
    private int edad;

    public Alumno() {

    }

    public Alumno(int id, String dni, String nombre, String apellidos, String nombreCurso, double nota, int edad) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.nombreCurso = nombreCurso;
        this.nota = nota;
        this.edad = edad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    @Override
    public String toString() {
        return id+" | "+dni+" | "+ nombre +" | "+apellidos+" | Curso: "+nombreCurso+" | Nota: "+nota+" | Edad: "+edad;
    }
}