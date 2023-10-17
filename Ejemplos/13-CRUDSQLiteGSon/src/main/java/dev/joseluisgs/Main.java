package dev.joseluisgs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.joseluisgs.controllers.PersonasController;
import dev.joseluisgs.managers.DataBaseManager;
import dev.joseluisgs.models.Persona;
import dev.joseluisgs.repositories.PersonasRepositoryImpl;
import dev.joseluisgs.services.BackupJsonImpl;
import dev.joseluisgs.utils.LocalDateAdapter;
import dev.joseluisgs.utils.LocalDateTimeAdapter;
import dev.joseluisgs.utils.UuidAdapter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hola SQLite :)");
        checkDataBase();

        // Comenzamos con inyección de dependencias... Se puede usar con singleton, pero ojo con Daggger!!!
        PersonasController personasController = new PersonasController(
                new PersonasRepositoryImpl(
                        DataBaseManager.getInstance(),
                        BackupJsonImpl.getInstance()
                )
        );
        Gson gson = new GsonBuilder()
                // Como hay problemas debo crearle un adaptador para la fecha
                // Carga solo los adaptadores que necesites
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(UUID.class, new UuidAdapter())
                .setPrettyPrinting() // Para que imprima bien tabulado
                .create();

        // Primero borramos todas las personas
        System.out.println("Borramos todas las personas");
        personasController.deleteAll();

        // Vamos a insertar unos pocos datos...
        Persona p1 = Persona.builder()
                .id(0) // No lo vamos a usar
                .uuid(UUID.randomUUID())
                .nombre("Pepito")
                .edad(31)
                .createdAt(LocalDate.now())
                .build();
        Persona p2 = Persona.builder()
                .id(0) // No lo vamos a usar
                .uuid(UUID.randomUUID())
                .nombre("Juanito")
                .edad(20)
                .createdAt(LocalDate.now())
                .build();

        Persona p3 = Persona.builder()
                .id(0) // No lo vamos a usar
                .uuid(UUID.randomUUID())
                .nombre("Anita")
                .edad(25)
                .createdAt(LocalDate.now())
                .build();

        // Insertamos los datos
        System.out.println("Insertamos los datos");
        var res1 = personasController.savePersona(p1);
        System.out.println(gson.toJson(res1));
        var res2 = personasController.savePersona(p2);
        System.out.println(gson.toJson(res2));
        var res3 = personasController.savePersona(p3);
        System.out.println(gson.toJson(res3));

        // Buscamos todas las personas
        System.out.println("Buscamos todas las personas");
        var personas = personasController.findAll();
        System.out.println(gson.toJson(personas));

        // Trabajamos con la API Streams
        System.out.println("Trabajamos con la API Streams");
        System.out.println("Personas menores de 30 años");
        var personasMenores30 = personas.stream().filter(p -> p.getEdad() < 30).collect(java.util.stream.Collectors.toList());
        // podríamos usar un foreach
        System.out.println(gson.toJson(personasMenores30));
        System.out.println("Nombre de las personas mayores de 30 años");
        var nombresPersonasMayores30 = personas.stream().filter(p -> p.getEdad() > 30).map(Persona::getNombre).collect(java.util.stream.Collectors.toList());
        System.out.println(gson.toJson(nombresPersonasMayores30));
        // Cuantas personas menores de 30 años hay??
        System.out.println("Cuantas personas menores de 30 años hay?");
        var cuantasPersonasMenores30 = personas.stream().filter(p -> p.getEdad() < 30).count();
        System.out.println(cuantasPersonasMenores30);
        // Hay alguna persona mayor de 30 años??
        System.out.println("Hay alguna persona mayor de 30 años?");
        var hayPersonaMayor30 = personas.stream().anyMatch(p -> p.getEdad() > 30);
        System.out.println(hayPersonaMayor30);
        // Hay alguna persona de nombre Funalito??
        System.out.println("Hay alguna persona de nombre Funalito?");
        var hayPersonaFunalito = personas.stream().anyMatch(p -> p.getNombre().equals("Funalito"));
        System.out.println(hayPersonaFunalito);
        // Estadisticas de edad
        System.out.println("Agrupación por edad");
        var estadisticasEdad = personas.stream().collect(Collectors.groupingBy(Persona::getEdad));
        System.out.println(gson.toJson(estadisticasEdad));
        // Obtener la edad media
        System.out.println("Obtener la edad media");
        var edadMedia = personas.stream().collect(Collectors.averagingInt(Persona::getEdad));
        System.out.println(edadMedia);
        // Obtener la edad máxima
        System.out.println("Obtener la persona con la edad máxima");
        var personaMayor = personas.stream().max(Comparator.comparingInt(Persona::getEdad)).get();
        System.out.println(gson.toJson(personaMayor));
        // Obtener la persona con la edad mínima
        System.out.println("Obtener la persona con la edad mínima");
        var personaMenor = personas.stream().min(Comparator.comparingInt(Persona::getEdad)).get();
        System.out.println(gson.toJson(personaMenor));
        // Estadisticas de edad
        System.out.println("Estasisticas por Edad");
        var estadisticasEdad2 = personas.stream().mapToInt(Persona::getEdad).summaryStatistics();
        System.out.println(estadisticasEdad2);

        // Cambiamos el nombre a p1
        System.out.println("Cambiamos el nombre a: " + p1.getNombre());
        p1.setNombre("Pepito Update");
        p1 = personasController.update(p1);
        System.out.println(gson.toJson(p1));

        // Vamos a buscar a p2
        System.out.println("Vamos a buscar a: " + p2.getNombre());
        p2 = personasController.findById(p2.getId());
        System.out.println(gson.toJson(p2));

        // Vamos a borrar a p2
        System.out.println("Vamos a borrar a: " + p2.getNombre());
        p2 = personasController.delete(p2);
        System.out.println(gson.toJson(p2));

        // Finalmente obtenemos todos los datos
        System.out.println("Obtenemos todos los datos");
        var personas2 = personasController.findAll();
        System.out.println(gson.toJson(personas2));

        // Salvamos los datos backup json
        System.out.println("Salvamos los datos backup json");
        personasController.backup();

        // Recuperamos los datos backup json
        System.out.println("Recuperamos los datos backup json");
        personasController.restore();
        var personas3 = personasController.findAll();
        System.out.println(gson.toJson(personas3));

    }

    private static void checkDataBase() {
        System.out.println("Comprobamos la conexión al Servidor BD");
        DataBaseManager db = DataBaseManager.getInstance();
        try {
            db.open();
            Optional<ResultSet> rs = db.select("SELECT 'Hello world'");
            if (rs.isPresent()) {
                rs.get().next();
                db.close();
                System.out.println("Conexión correcta a la Base de Datos");
            }
        } catch (SQLException e) {
            System.err.println("Error al conectar al servidor de Base de Datos: " + e.getMessage());
            System.exit(1);
        }
    }
}