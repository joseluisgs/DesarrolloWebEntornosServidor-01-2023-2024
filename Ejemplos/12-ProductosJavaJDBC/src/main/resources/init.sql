-- Fichero para inicializar la base de datos, tablas y valores iniciales
-- Borramos la base de datos si existe
DROP TABLE IF EXISTS productos;

-- Tabla de productos
CREATE TABLE IF NOT EXISTS productos
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    uuid       TEXT    NOT NULL,
    nombre     TEXT    NOT NULL,
    precio     REAL    NOT NULL,
    cantidad   INTEGER NOT NULL,
    created_at TEXT    NOT NULL,
    updated_at TEXT    NOT NULL,
    disponible INTEGER NOT NULL
);

-- insertamos datos iniciales
INSERT INTO productos (id, uuid, nombre, precio, cantidad, created_at, updated_at, disponible)
VALUES (1, '08daf360-15a8-4850-8ac2-19a7f029e36f', 'Producto 1', 10.0, 10, '2023-04-04T10:40:13', '2023-04-03T11:12:14',
        1);
INSERT INTO productos (id, uuid, nombre, precio, cantidad, created_at, updated_at, disponible)
VALUES (2, '5f5e58ec-e099-4ef6-ac49-ebde498c913e', 'Producto 2', 20.0, 20, '2023-04-04T10:41:23', '2023-04-03T11:13:15',
        0);