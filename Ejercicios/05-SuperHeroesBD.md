Crea una Repositorio CRUD en base de Datos H2 o SQLite para Superheroes. Los datos de conexión deben estar encapsulados en un manejador y leídos de un fichero de propiedades o de entorno.

Los datos de cada Superheroe son:
- Nombre
- Nombre de la identidad secreta
- Ciudad donde realiza su actividad

Además debemos tener en cuenta la fecha de creación y la fecha de actualización de cada registro.

Además queremos usar un servicio con CACHE de 10 elementos.

Debes inyectar en el servicio las dependencias vía constructor, y aplicar patrones SOLID u de código limpio, así como otros patrones vistos en clase indentificándolos dentro del código.

Prueba los métodos más importantes en el main.

Finalmente en el main, exporta los datos a un fichero JSON.

Testea el servicio con JUnit y Mockito.
