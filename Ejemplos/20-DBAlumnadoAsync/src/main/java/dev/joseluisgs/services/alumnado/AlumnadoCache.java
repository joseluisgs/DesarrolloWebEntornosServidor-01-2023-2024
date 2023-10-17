package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.cache.Cache;

interface AlumnadoCache extends Cache<Long, Alumno> {
}