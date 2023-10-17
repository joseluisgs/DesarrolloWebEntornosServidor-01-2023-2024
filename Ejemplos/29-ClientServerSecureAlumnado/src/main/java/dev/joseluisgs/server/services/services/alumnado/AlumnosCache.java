package dev.joseluisgs.server.services.services.alumnado;


import dev.joseluisgs.common.models.Alumno;
import dev.joseluisgs.server.services.services.cache.Cache;

interface AlumnosCache extends Cache<Long, Alumno> {
}