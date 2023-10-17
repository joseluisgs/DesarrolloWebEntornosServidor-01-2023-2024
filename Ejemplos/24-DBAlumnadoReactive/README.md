# Proyecto Java con SQLite

Este proyecto es una aplicación simple en Java que utiliza SQLite como base de datos. A continuación, se describen los
pasos para configurar y ejecutar el proyecto.

## Requisitos

- Java 8 o superior
- Gradle

## Configuración

### Paso 1: Dependencias de Gradle

Agrega las siguientes dependencias a tu archivo `build.gradle`:

```kotlin
plugins {
    java
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Añadimos las dependencias de las librerías JDBC que vayamos a usar
    // SQLite
    implementation("org.xerial:sqlite-jdbc:3.41.2.2")
    // H2, solo usa una
    implementation("com.h2database:h2:2.2.224")
    // Ibatis lo usaremos para leer los scripts SQL desde archivos
    implementation("org.mybatis:mybatis:3.5.13")
    // Lombook para generar código
    implementation("org.projectlombok:lombok:1.18.26")
}
```

### Paso 2: Crear la clase Alumno

Crea una clase `Alumno` con los atributos `id` y `nombre`.

```java
public class Alumno {
    private Integer id;
    private String nombre;

    // getters y setters
}
```

### Paso 3: Crear la clase DatabaseManager

Crea la clase `DatabaseManager` que maneja la conexión a la base de datos SQLite y la inicialización de las tablas. Esta
clase es un singleton y tiene un método `getInstance` para obtener la instancia. También tiene un método `executeScript`
que ejecuta un script SQL desde un archivo.

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.jdbc.ScriptRunner;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final Connection conn;

    private DatabaseManager(boolean initTables) {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
            if (initTables) {
                initTables();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseManager getInstance(boolean initTables) {
        if (instance == null) {
            instance = new DatabaseManager(initTables);
        }
        return instance;
    }

    private void openConnection() throws SQLException {
        conn = DriverManager.getConnection(url);
    }

    public Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                openConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

    private void initTables() {
        // Aquí va el código para inicializar las tablas si es necesario
    }

    public void executeScript(String scriptSqlFile, boolean logWriter) throws FileNotFoundException {
        ScriptRunner sr = new ScriptRunner(conn);
        var file = ClassLoader.getSystemResource(scriptSqlFile).getFile();
        Reader reader = new BufferedReader(new FileReader(file));
        sr.setLogWriter(logWriter ? new PrintWriter(System.out) : null);
        sr.runScript(reader);
    }
}
```

## Ejecución

### Paso 1: Inicializar la base de datos

En el método `main`, obtén la instancia de `DatabaseManager` y usa el método `executeScript` para ejecutar un script SQL
desde un archivo:

```java
public class Main {
    public static void main(String[] args) {
        // Obtener la instancia de DatabaseManager y la conexión
        DatabaseManager dbManager = DatabaseManager.getInstance(true);

        // Ejecutar script SQL desde un archivo
        dbManager.executeScript("init.sql");

        // el resto de tu código ...
    }
}
```

Asegúrate de que el archivo `init.sql` esté en la carpeta `resources` de tu proyecto y que contenga el script SQL que
deseas ejecutar.

*** Importante: No olvides abrir o cerrar tu conexión con el servidor cuando toque y de todos los recursos asociados ***

## Repository

Es una forma mucho mejor de encapsular tu código.

```java
package dev.joseluisgs.repositories;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.database.DatabaseManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// Esta es la clase que se encarga de la persistencia de los alumnos
public class AlumnosRepositoryImpl implements AlumnosRepository {
    // Singleton
    private static AlumnosRepositoryImpl instance;
    // Base de datos
    private final DatabaseManager db;

    private AlumnosRepositoryImpl(DatabaseManager db) {
        this.db = db;
    }

    public static AlumnosRepositoryImpl getInstance(DatabaseManager db) {
        if (instance == null) {
            instance = new AlumnosRepositoryImpl(db);
        }
        return instance;
    }

    @Override
    public List<Alumno> findByNombre(String nombre) throws SQLException {
        // Vamos a usar Like para buscar por nombre
        var stmt = db.getConnection()
                .prepareStatement("SELECT * FROM alumnos WHERE nombre LIKE '%" + nombre + "%'");
        var rs = stmt.executeQuery();
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
        // cerramos todo
        stmt.close();
        rs.close();
        db.closeConnection();
        return lista;
    }

    @Override
    public Alumno save(Alumno alumno) throws SQLException {
        // importante debemos devolver el alumno con la clave, por eso usamos RETURN_GENERATED_KEYS
        var stmt = db.getConnection()
                .prepareStatement("INSERT INTO alumnos (nombre) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, alumno.getNombre());
        var res = stmt.executeUpdate();
        // Ahora puedo obtener la clave
        if (res > 0) {
            ResultSet rs = stmt.getGeneratedKeys();
            while (rs.next()) {
                // Asignamos la clave al alumno
                alumno.setId(rs.getLong(1));
            }
            rs.close();
        }
        // cerramos todo
        stmt.close();
        db.closeConnection();
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) throws SQLException {
        var stmt = db.getConnection()
                .prepareStatement("UPDATE alumnos SET nombre = ? WHERE id = ?");
        stmt.setString(1, alumno.getNombre());
        stmt.setLong(2, alumno.getId());
        stmt.executeUpdate();
        // cerramos todo
        stmt.close();
        db.closeConnection();
        return alumno;
    }

    @Override
    public Optional<Alumno> findById(Long aLong) throws SQLException {
        var stmt = db.getConnection()
                .prepareStatement("SELECT * FROM alumnos WHERE id = ?");
        stmt.setLong(1, aLong);
        var rs = stmt.executeQuery();
        Optional<Alumno> alumno = Optional.empty();
        while (rs.next()) {
            // Creamos un alumno
            alumno = Optional.of(new Alumno(
                    rs.getLong("id"),
                    rs.getString("nombre")
            ));
        }
        // cerramos todo
        stmt.close();
        rs.close();
        db.closeConnection();
        return alumno;
    }

    @Override
    public List<Alumno> findAll() throws SQLException {
        var stmt = db.getConnection().prepareStatement("SELECT * FROM alumnos");
        var rs = stmt.executeQuery();
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
        // cerramos todo
        stmt.close();
        rs.close();
        db.closeConnection();
        return lista;

    }

    @Override
    public boolean deleteById(Long aLong) throws SQLException {
        var stmt = db.getConnection()
                .prepareStatement("DELETE FROM alumnos WHERE id = ?");
        stmt.setLong(1, aLong);
        var res = stmt.executeUpdate();
        // cerramos todo
        stmt.close();
        db.closeConnection();
        return res > 0;

    }

    @Override
    public void deleteAll() throws SQLException {
        var stmt = db.getConnection()
                .prepareStatement("DELETE FROM alumnos");
        stmt.executeUpdate();
        // cerramos todo
        stmt.close();
        db.closeConnection();
    }
}
```

Y luego en tu main o donde quieras

```java
// Ahora te lo voy a repetir con un repositorio, para que veas que todo queda encapsulado y más limpio
        AlumnosRepository alumnosRepository=AlumnosRepositoryImpl.getInstance(DatabaseManager.getInstance());

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
                var alumno=new Alumno(0L,"Ana"); // Le ponemos 0 porque es autonumerico
                System.out.println(alumnosRepository.save(alumno));
                alumno=new Alumno(0L,"Luis");
                System.out.println(alumnosRepository.save(alumno));
                alumno=new Alumno(0L,"Sara");
                System.out.println(alumnosRepository.save(alumno));

                // Obtenemos todos los alumnos
                System.out.println("Todos los alumnos");
                alumnosRepository.findAll().forEach(System.out::println);

                // Actualizamos el alumno con id 1
                System.out.println("Actualizamos el alumno con id 1");
                alumno=new Alumno(1L,"Pedro");
                System.out.println(alumnosRepository.update(alumno));

                // obtenemos todos los alumnos
                System.out.println("Todos los alumnos");
                alumnosRepository.findAll().forEach(System.out::println);

                // Borramos el alumno con id 1
                System.out.println("Borramos el alumno con id 1");
                alumno=new Alumno(1L,"Maria");
                var deleted=alumnosRepository.deleteById(alumno.getId());
                if(deleted){
                System.out.println("Alumno borrado: "+alumno);
                }else{
                System.out.println("Alumno no borrado porque no existe");
                }

                // obtenemos todos los alumnos
                System.out.println("Todos los alumnos");
                alumnosRepository.findAll().forEach(System.out::println);
```