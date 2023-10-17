package dev.joseluisgs;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.AlumnosRepository;
import dev.joseluisgs.repositories.AlumnosRepositoryImpl;
import dev.joseluisgs.services.DatabaseManager;

import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, SQLException {
        System.out.println("Hola Base de datos con Java y JDBC");

        // Cargamos nuestro Managger de Base de Datos que nos permitirá hacer las operaciones
        // CRUD sobre la base de datos
        DatabaseManager db = DatabaseManager.getInstance();
        // Podemos decirle que cargue un script de SQL para inicializar la base de datos
        // Esto también lo podemos hacer desde el manejador si queremos y lee el fichero de configuración properties
        db.executeScript("init.sql", false);

        // Si pasa de aquí es que todo ha ido bien y se ha creado la base de datos

        // Ahora podemos hacer las operaciones CRUD sobre la base de datos
        // CRUD: Create, Read, Update, Delete
        // Lo mejor es que esto te lo lleves a un repositorio y lo hagas allí

        // Insertar dos alumnos, creamos la sentencia y la ejecutamos
        PreparedStatement insertStmt = db.getConnection()
                .prepareStatement("INSERT INTO alumnos (nombre) VALUES (?)");

        // El orden de los parámetros es importante y se corresponde con los ? de la sentencia
        // Además, el tipo de dato también es importante
        insertStmt.setString(1, "Pedro");
        insertStmt.executeUpdate(); // Ejecutamos la sentencia

        insertStmt.setString(1, "Maria");
        insertStmt.executeUpdate();
        insertStmt.close(); // Cerramos la sentencia

        // Seleccionar todos los alumnos
        // Siempre es un Result Set
        Statement stmt = db.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM alumnos");
        // Ahora recorremos el result set y lo pasamos a alumnos, podríamos guardarlo en un array
        var lista = new ArrayList<Alumno>();
        while (rs.next()) {
            // Creamos un alumno
            var alumno = new Alumno(
                    rs.getLong("id"),
                    rs.getString("nombre")
            );
            // Lo añadimos a la lista
            lista.add(alumno);
        }
        // Mostramos la lista
        lista.forEach(System.out::println);
        // Cierra el result set y el statement si no lo vas a usar más
        rs.close();
        stmt.close();

        // Seleccionar alumno con id específico
        PreparedStatement selectStmt = db.getConnection().prepareStatement("SELECT * FROM alumnos WHERE id = ?");
        selectStmt.setInt(1, 1);
        rs = selectStmt.executeQuery();
        while (rs.next()) {
            Alumno alumno = new Alumno(
                    rs.getLong("id"),
                    rs.getString("nombre")
            );
            System.out.println(alumno);
        }
        rs.close();
        selectStmt.close();

        // Actualizar alumno
        PreparedStatement updateStmt = db.getConnection().prepareStatement("UPDATE alumnos SET nombre = ? WHERE id = ?");
        updateStmt.setString(1, "Pedro");
        updateStmt.setInt(2, 1);
        updateStmt.executeUpdate();
        updateStmt.close();
        // Seleccionar todos los alumnos después de la actualización

        // Borrar alumno
        PreparedStatement deleteStmt = db.getConnection().prepareStatement("DELETE FROM alumnos WHERE id = ?");
        deleteStmt.setInt(1, 2);
        deleteStmt.executeUpdate();
        deleteStmt.close();


        // Seleccionar todos los alumnos después de la actualización y eliminación
        rs = stmt.executeQuery("SELECT * FROM alumnos");
        lista.clear();
        while (rs.next()) {
            var alumno = new Alumno(
                    rs.getLong("id"),
                    rs.getString("nombre")
            );
            lista.add(alumno);
        }
        lista.forEach(System.out::println);
        rs.close();
        stmt.close();

        // Ahora te voy a dar un truco para que sepas como obtener la clave autonumerica al insertar del tiron
        // Esto es muy útil para cuando queremos insertar un objeto y que nos devuelva el id
        PreparedStatement insertStmtAndKey = db.getConnection()
                .prepareStatement("INSERT INTO alumnos (nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        insertStmtAndKey.setString(1, "Andrea");
        // Mira que ahora lo recojo
        var res = insertStmtAndKey.executeUpdate();
        // Ahora puedo obtener la clave
        if (res > 0) {
            rs = insertStmtAndKey.getGeneratedKeys();
            while (rs.next()) {
                System.out.println("Clave generada: " + rs.getInt(1));
            }
            rs.close();
        }


        // Cerrar la conexión


        // Ahora te lo voy a repetir con un repositorio, para que veas que todo queda encapsulado y más limpio
        AlumnosRepository alumnosRepository = AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance());

        // Obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosRepository.findAll().forEach(System.out::println);

        // Obtenemos un alumno por id
        System.out.println("Alumno con id 1");
        System.out.println(alumnosRepository.findById(1L));

        // Obtenemos los alumnos por nombre
        System.out.println("Alumnos con nombre Juan");
        alumnosRepository.findByNombre("Juan").forEach(System.out::println);

        // Insertamos 3 alumnos
        System.out.println("Insertamos 3 alumnos");
        var alumno = new Alumno(0L, "Ana"); // Le ponemos 0 porque es autonumerico
        System.out.println(alumnosRepository.save(alumno));
        alumno = new Alumno(0L, "Luis");
        System.out.println(alumnosRepository.save(alumno));
        alumno = new Alumno(0L, "Sara");
        System.out.println(alumnosRepository.save(alumno));

        // Obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosRepository.findAll().forEach(System.out::println);

        // Actualizamos el alumno con id 1
        System.out.println("Actualizamos el alumno con id 1");
        alumno = new Alumno(1L, "Pedro");
        System.out.println(alumnosRepository.update(alumno));

        // obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosRepository.findAll().forEach(System.out::println);

        // Borramos el alumno con id 1
        System.out.println("Borramos el alumno con id 1");
        alumno = new Alumno(1L, "Maria");
        var deleted = alumnosRepository.deleteById(alumno.getId());
        if (deleted) {
            System.out.println("Alumno borrado: " + alumno);
        } else {
            System.out.println("Alumno no borrado porque no existe");
        }

        // obtenemos todos los alumnos
        System.out.println("Todos los alumnos");
        alumnosRepository.findAll().forEach(System.out::println);


    }
}