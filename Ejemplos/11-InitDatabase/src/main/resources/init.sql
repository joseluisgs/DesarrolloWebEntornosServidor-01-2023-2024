-- Cuidado que cada SGDB tiene su forma de crear tablas y datos
DROP TABLE IF EXISTS alumnos;
CREATE TABLE IF NOT EXISTS alumnos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT
);

INSERT INTO alumnos (nombre) VALUES ('Juan');