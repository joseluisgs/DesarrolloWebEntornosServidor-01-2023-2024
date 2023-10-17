# Funkos Reactivos

En el directorio data tienes un csv de muestra de Funkos. Con la siguiente estructura:
- COD: En formato UUID v4
- NOMBRE: Cadena de caracteres
- MODELO: Solo tiene estos valores: MARVEL, DISNEY, ANIME u OTROS
- PRECIO: Moneda con dos decimales.
- FECHA_LANZAMIENTO: Fecha en formato YYYY-MM-DD siguiendo ISO-8601

Antes de procesarlos ten en cuenta que puede haber errores en los campos.

***Vamos a trabajar totalmente reactivos***.

Debes cargar estos datos en una base de datos de tipo reactiva (R2DBC) en H2 en fichero, llamada "funkos", teniendo en cuenta que los datos de conexión deben leerse de un fichero de propiedades y que debe estar gestionada por un manejador o servicio de bases de datos. Esta base de datos usará un pool de conexiones reactivas con R2DBC-Pool.

Además vamos a tener un IdGenerator, que se encargará de asignar las clases de manera reactiva. Deberá estar protegido para accesos concurrente en entornos multihilo.

El formato de la tabla FUNKOS es el siguiente:
- ID: autonumérico y clave primaria.
- cod: UUID, no nulo, y se puede generar automáticamente un valor por defecto si no se le pasa.
- MyId: Long que puede será generado por el IdGenerator (sí, sé que no tiene sentido teniendo el otro, pero debemos practicar cosas ;))
- nombre: cadena de caracteres de máximo 255.
- modelo, solo puede ser MARVEL, DISNEY, ANIME u OTROS
- precio: un número real
- fecha_lanzamiento: es un tipo de fecha.
- created_at: marca de tiempo que toma por valor si no se le pasa la fecha completa actual al crearse la entidad
- updated_at: marca de tiempo que toma por valor si no se le pasa la fecha completa al crearse la entidad o actualizarse.

Debes crear un repositorio CRUD totalmente reactivo completo para la gestión de Funkos. Además, de las operaciones CRUD normales, debes incluir una que se llame findByNombre, donde se pueda buscar por nombres que contengan el patrón indicado. Se debe asegurar una instancia única de este repositorio. 

Además, debes usar un servicio totalmente reactivo que haga uso de este repositorio e implemente una caché totalmente reactiva de 15 elementos máximo que más han sido accedidos y además expiren si llevan más de 90 segundos en la caché sin haber sido accedidos. Este servicio hará uso de excepciones personalizadas de no chequeadas si no se puede realizar las operaciones indicadas.
Este servicio tendrá un método backup que exporta los datos en JSON a una ruta pasada de manera reactiva, solo si esta es válida, si no producirá una excepción personalizada y un método import para importarlos de manera reactiva desde el CSV. 

Nuestro servicio hará uso de sistema de notificaciones para las operaciones de insercción, actualización y borrado de funkos.

Para importar y exportar los datos, se recomienda hacer un servicio reactivo de almacenamiento con los métodos de importar y exportar los datos y excepciones personalizadas.

Para las notificaciones es recomendables hacer un Servicio de Notificaciones que las gestione.

Ten en cuenta que si aplicas en patrón Singleton, este tiene que estar protegido en entornos multihilo.

Por lo tanto nuestra estructura es:
- DatabaseManager reactivo con los datos de las conexión leídos desde un fichero properties y un Pool de conexiones en reactivas con instancia única.
- IdGenerator reactivo y protegido en entornos multihilo con instancia única.
- Repositorio de Funkos reactivo al que se le inyecta el IdGenerator y DatabaseManager, tiene una instancia única.
- Servicio de Almacenamiento reactivo para importar de CSV y exportar a JSON, tiene una instancia única.
- Cache reactiva de Funkos.
- Servicio de notificaciones reactivo para Funkos con instancia única.
- Servicio de Funkos al que se le inyecta: Repositorio de Funkos, Caché de Funkos, y Servicio de Almacenamiento y Servicio de Notificaciones. 


Se debe mostrar un ejemplo de cada uno de los métodos del servicio en el main con los casos de ejecución correcta e incorrecta. Además, en el main, las salidas deben estar localizadas tanto en fechas como moneda a ESPAÑA. Ten en cuenta que lo primero que deberás lanzar y capturar son las notificaciones para que se vaya viendo qué funcionan.

Se debe además sacar las consultas en el main de manera reactiva:
- Funko más caro.
- Media de precio de Funkos.
- Funkos agrupados por modelos.
- Número de funkos por modelos.
- Funkos que han sido lanzados en 2023.
- Número de funkos de Stitch.
- Listado de funkos de Stitch.

Finalmente se pide testear todos los caso correctos o incorrectos de los métodos de:
- Caché
- Repositorio
- Servicio de Almacenamiento
- Servicio de Funkos. Ten en cuenta quete va a dar un error los métodos de insercción, actualización y borrado debido al sistema de notificaciones. Puedes usar internet para saber cómo resolverlo, o puedes hacer en tu servicio de funkos un método de insercción, actualización y borrado que no llame a las notificaciones y testearlos, y luego otro que sea insercción, actualización y borrado con actualizaciones y estos no testearlos. Estos métodos llamarán a su método sin notificación y si "sucede" lanzará la notificación.
  
Se recomienda usar un Logger en todo el proceso.

## Entrega

Para entregar se debe crear un repositorio con el código siguiendo GitFlow. En el README.md de tu proyecto debes explicar cómo has realizado el proceso y mostrar capturas del proceso y analizar los distintos elementos y cómo se han desarrollado. Posteriormente se incluirá el enlace del repositorio en el aula virtual.

La práctica puede hacerse en parejas o individualmente. En caso de hacerse en parejas, se debe indicar en el README.md del proyecto el nombre del compañero o compañera.

Se valorará:
- solución aportada
- Uso de Arquitecturas Limpias y código limpio
- Principios SOLID
- Test unitarios y con dobles.


