package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AlumnosServiceImpl implements AlumnosService {
    // Para mi cache
    private static final int CACHE_SIZE = 10; // Tamaño de la cache
    // Singleton
    private static AlumnosServiceImpl instance;
    private final Map<Long, Alumno> cache; // Nuestra cache
    private final Logger logger = LoggerFactory.getLogger(AlumnosServiceImpl.class);
    private final AlumnosRepository alumnosRepository;

    private AlumnosServiceImpl(AlumnosRepository alumnosRepository) {
        this.alumnosRepository = alumnosRepository;
        // Inicializamos la cache con el tamaño y la política de borrado de la misma
        // borramos el más antiguo cuando llegamos al tamaño máximo
        this.cache = new LinkedHashMap<>(CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Alumno> eldest) {
                return size() > CACHE_SIZE;
            }
        };
    }


    public static AlumnosServiceImpl getInstance(AlumnosRepository alumnosRepository) {
        if (instance == null) {
            instance = new AlumnosServiceImpl(alumnosRepository);
        }
        return instance;
    }

    @Override
    public List<Alumno> findAll() throws SQLException {
        logger.debug("Obteniendo todos los alumnos");
        // No vamos a cachear todos los alumnos, pueden ser muchos
        return alumnosRepository.findAll();
    }

    @Override
    public List<Alumno> findAllByNombre(String nombre) throws SQLException {
        logger.debug("Obteniendo todos los alumnos ordenados por nombre");
        // No vamos a cachear todos los alumnos, pueden ser muchos
        return alumnosRepository.findByNombre(nombre);
    }

    @Override
    public Optional<Alumno> findById(long id) throws SQLException {
        logger.debug("Obteniendo alumno por id");
        // Buscamos en la cache
        Alumno alumno = cache.get(id);
        if (alumno != null) {
            logger.debug("Alumno encontrado en cache");
            return Optional.of(alumno);
        } else {
            // Buscamos en la base de datos
            logger.debug("Alumno no encontrado en cache, buscando en base de datos");
            var optional = alumnosRepository.findById(id);
            optional.ifPresent(value -> cache.put(id, value));
            return optional;
        }
    }

    @Override
    public Alumno save(Alumno alumno) throws SQLException, AlumnoException {
        logger.debug("Guardando alumno");
        // Guardamos en la base de datos
        alumno = alumnosRepository.save(alumno);
        // Guardamos en la cache
        cache.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) throws SQLException, AlumnoException {
        logger.debug("Actualizando alumno");
        // Actualizamos en la base de datos
        alumno = alumnosRepository.update(alumno);
        // Actualizamos en la cache
        cache.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public boolean deleteById(long id) throws SQLException {
        logger.debug("Borrando alumno");
        // Borramos en la base de datos
        var deleted = alumnosRepository.deleteById(id);
        // Borramos en la cache si existe en ella
        if (deleted) {
            cache.remove(id);
        }
        return deleted;
    }

    @Override
    public void deleteAll() throws SQLException {
        logger.debug("Borrando todos los alumnos");
        // Borramos en la base de datos
        alumnosRepository.deleteAll();
        // Borramos en la cache
        cache.clear();
    }
}
