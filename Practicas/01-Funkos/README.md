# Funkos

En el directorio data tienes un csv de muestra de Funkos. Con la siguiente estructura:
- COD: En formato UUID v4
- NOMBRE: Cadena de caracteres
- MODELO: Solo tiene estos valores: MARVEL, DISNEY, ANIME u OTROS
- PRECIO: Moneda con dos decimales.
- FECHA_LANZAMIENTO: Fecha en formato YYYY-MM-DD siguiendo ISO-8601

Antes de procesarlos ten en cuenta que puede haber errores en los campos.

Debes cargar estos datos en una base de datos H2 en fichero, llamada "funkos", teniendo en cuenta que los datos de conexión deben leerse de un fichero de propiedades y que debe estar gestionada por un manejador o servicio de bases de datos. El formato de la tabla FUNKOS es el siguiente:
- ID: autonumérico y clave primaria
- cod: UUID, no nulo, y se puede generar automáticamente un valor por defecto si no se le pasa.
- nombre: cadena de caracteres de máximo 255.
- modelo, solo puede ser MARVEL, DISNEY, ANIME u OTROS
- precio: un número real
- fecha_lanzamiento: es un tipo de fecha.
- created_at: marca de tiempo que toma por valor si no se le pasa la fecha completa actual al crearse la entidad
- updated_at: marca de tiempo que toma por valor si no se le pasa la fecha completa al crearse la entidad o actualizarse.


Debes crear un repositorio CRUD completo para la gestión de Funkos. Además, de las operaciones CRUD normales, debes incluir una que se llame findByNombre, donde se pueda buscar por nombres que contengan el patrón indicado. Se debe asegurar una instancia única de este repositorio. Este repositorio debe tener en cuenta la gestión de nulos.

Además, debes usar un servicio que haga uso de este repositorio e implemente una caché de 25 elementos más usuales. Este repositorio hará uso de excepciones personalizadas de no chequeadas si no se puede realizar las operaciones indicadas.
Este servicio tendrá un método backup que exporta los datos en JSON a una ruta pasada de manera asíncrona, solo si esta es válida, si no producirá una excepción personalizada y un método import para importarlos de manera asíncrona desde el CSV.

Para importar y exportar los datos, se recomienda hacer un servicio asíncrono de almacenamiento con los métodos de importar y exportar los datos y excepciones personalizadas.

Además, en el main, las salidas deben estar localizadas tanto en fechas como moneda a ESPAÑA.

Se debe mostrar un ejemplo de cada uno de los métodos del servicio en el main y de sus casos incorrectos.

Se debe ademas sacar las consultas en el main:
- Funko más caro.
- Media de precio de Funkos.
- Funkos agrupados por modelos.
- Número de funkos por modelos.
- Funkos que han sido lanzados en 2023.
- Número de funkos de Stitch.
- Listado de funkos de Stitch.

Finalmente se pide testear todos los caso correctos o incorrectos de los métodos del servicio y repositorio. Se debe usar JUnit 5 y Mockito donde corresponda.

Se recomienda usar un Logger en todo el proceso.


## Entrega

Para entregar se debe crear un repositorio con el código siguiendo GitFlow. En el README.md de tu proyecto debes explicar cómo has realizado el proceso y mostrar capturas del proceso y analizar los distintos elementos y cómo se han desarrollado. Posteriormente se incluirá el enlace del repositorio en el aula virtual.

La práctica puede hacerse en parejas o individualmente. En caso de hacerse en parejas, se debe indicar en el README.md del proyecto el nombre del compañero o compañera.

Se valorará:
- solución aportada
- Uso de Arquitecturas Limpias y código limpio
- Principios SOLID
- Test unitarios y con dobles.

