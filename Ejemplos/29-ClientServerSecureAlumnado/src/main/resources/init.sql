-- Cuidado que cada SGDB tiene su forma de crear tablas y datos
-- En R2DBC una sentencia por consulta
CREATE TABLE IF NOT EXISTS ALUMNOS (
                                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       nombre VARCHAR(255) NOT NULL,
    calificacion REAL NOT NULL DEFAULT 0,
    uuid UUID NOT NULL DEFAULT RANDOM_UUID(),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    );
