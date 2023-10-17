package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AlumnadoCacheImpl implements AlumnadoCache {
    private final Logger logger = LoggerFactory.getLogger(AlumnadoCacheImpl.class);
    private final int maxSize;
    private final Map<Long, Alumno> cache;
    private final ScheduledExecutorService cleaner;


    public AlumnadoCacheImpl(int maxSize) {
        this.maxSize = maxSize;
        this.cache = new LinkedHashMap<Long, Alumno>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Long, Alumno> eldest) {
                return size() > maxSize;
            }
        };
        this.cleaner = Executors.newSingleThreadScheduledExecutor();
        this.cleaner.scheduleAtFixedRate(this::clear, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public void put(Long key, Alumno value) {
        logger.debug("Añadiendo alumno a cache con id: " + key + " y valor: " + value);
        cache.put(key, value);
    }

    @Override
    public Alumno get(Long key) {
        logger.debug("Obteniendo alumno de cache con id: " + key);
        return cache.get(key);
    }

    @Override
    public void remove(Long key) {
        logger.debug("Eliminando alumno de cache con id: " + key);
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.entrySet().removeIf(entry -> {
            boolean shouldRemove = entry.getValue().getUpdatedAt().plusMinutes(1).isBefore(LocalDateTime.now());
            if (shouldRemove) {
                logger.debug("Autoeliminando por caducidad alumno de cache con id: " + entry.getKey());
            }
            return shouldRemove;
        });
    }

    @Override
    public void shutdown() {
        cleaner.shutdown();
    }
}
