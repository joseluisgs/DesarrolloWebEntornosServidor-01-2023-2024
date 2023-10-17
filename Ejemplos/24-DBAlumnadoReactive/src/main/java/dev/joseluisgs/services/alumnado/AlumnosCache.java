package dev.joseluisgs.services.alumnado;

import dev.joseluisgs.models.Alumno;
import dev.joseluisgs.services.cache.Cache;

interface AlumnosCache extends Cache<Long, Alumno> {
}