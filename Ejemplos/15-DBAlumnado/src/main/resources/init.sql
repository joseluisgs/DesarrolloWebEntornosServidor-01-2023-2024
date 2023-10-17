-- Cuidado que cada SGDB tiene su forma de crear tablas y datos
DROP TABLE IF EXISTS ALUMNOS;
CREATE TABLE IF NOT EXISTS ALUMNOS (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(255) NOT NULL,
    calificacion REAL NOT NULL DEFAULT 0,
    uuid UUID NOT NULL DEFAULT RANDOM_UUID(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO ALUMNOS (nombre, calificacion) VALUES
                                                   ('Juan Pérez', 8.5),
                                                   ('María López', 9.2),
                                                   ('Carlos Rodríguez', 7.8),
                                                   ('Ana Martínez', 6.9);