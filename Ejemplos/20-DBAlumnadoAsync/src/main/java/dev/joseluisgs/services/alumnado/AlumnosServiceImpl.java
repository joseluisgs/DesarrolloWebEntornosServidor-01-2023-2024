package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.exceptions.alumnos.AlumnoNoEncotradoException;
import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.repositories.alumnado.AlumnosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class AlumnosServiceImpl implements AlumnosService {
    // Para mi cache
    private static final int CACHE_SIZE = 10; // Tamaño de la cache
    // Singleton
    private static AlumnosServiceImpl instance;
    private final AlumnadoCache cache;
    private final Logger logger = LoggerFactory.getLogger(AlumnosServiceImpl.class);
    private final AlumnosRepository alumnosRepository;

    private AlumnosServiceImpl(AlumnosRepository alumnosRepository) {
        this.alumnosRepository = alumnosRepository;
        // Inicializamos la cache con el tamaño y la política de borrado de la misma
        // borramos el más antiguo cuando llegamos al tamaño máximo
        this.cache = new AlumnadoCacheImpl(CACHE_SIZE);
    }


    public static AlumnosServiceImpl getInstance(AlumnosRepository alumnosRepository) {
        if (instance == null) {
            instance = new AlumnosServiceImpl(alumnosRepository);
        }
        return instance;
    }

    @Override
    public List<Alumno> findAll() throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Obteniendo todos los alumnos");
        // No vamos a cachear todos los alumnos, pueden ser muchos
        return alumnosRepository.findAll().get();
    }

    @Override
    public List<Alumno> findAllByNombre(String nombre) throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Obteniendo todos los alumnos ordenados por nombre");
        // No vamos a cachear todos los alumnos, pueden ser muchos
        return alumnosRepository.findByNombre(nombre).get();

    }

    @Override
    public Optional<Alumno> findById(long id) throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Obteniendo alumno por id");
        // Buscamos en la cache
        Alumno alumno = cache.get(id);
        if (alumno != null) {
            logger.debug("Alumno encontrado en cache");
            return Optional.of(alumno);
        } else {
            // Buscamos en la base de datos
            logger.debug("Alumno no encontrado en cache, buscando en base de datos");
            return alumnosRepository.findById(id).get();
        }
    }

    @Override
    public Alumno save(Alumno alumno) throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Guardando alumno");
        // Guardamos en la base de datos
        alumno = alumnosRepository.save(alumno).get();
        // Guardamos en la cache
        cache.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public Alumno update(Alumno alumno) throws SQLException, AlumnoNoEncotradoException, ExecutionException, InterruptedException {
        logger.debug("Actualizando alumno");
        // Actualizamos en la base de datos
        alumno = alumnosRepository.update(alumno).get();
        // Actualizamos en la cache
        cache.put(alumno.getId(), alumno);
        return alumno;
    }

    @Override
    public boolean deleteById(long id) throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Borrando alumno");
        // Borramos en la base de datos
        var deleted = alumnosRepository.deleteById(id).get();
        // Borramos en la cache si existe en ella
        if (deleted) {
            cache.remove(id);
        }
        return deleted;
    }

    @Override
    public void deleteAll() throws SQLException, ExecutionException, InterruptedException {
        logger.debug("Borrando todos los alumnos");
        // Borramos en la base de datos
        alumnosRepository.deleteAll().get();
        // Borramos en la cache
        cache.clear();
    }
}
