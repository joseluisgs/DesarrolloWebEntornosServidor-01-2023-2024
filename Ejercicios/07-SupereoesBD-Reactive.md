Lee el fichero JSON de superhéroes de los ejercicios anteriores. Debes leerlo de manera reactiva y almacenar los datos en una base de datos H2 o SQLite de manera reactiva.	

Los datos de conexión deben estar encapsulados en un manejador y leídos de un fichero de propiedades o de entorno. La base de datos debe manejar un pool de conexiones con reactivo.


Crea un repositorio CRUD que trabaje de manera reactiva con los datos de superhéroes en la base de datos. 

Crea un servicio totalmente reactivo que haga uso del repositorio y que use un cache reactiva de 10 elementos que más han sido accedidos y además expiren los elementos si llevan más de 30 segundos en la caché sin haber sido actualizados.

Añade a nuestro servicio una servició de notificaciones para las operaciones de insercción, actualización y de borrado de héroes.

Debes inyectar en el servicio las dependencias vía constructor, y aplicar patrones SOLID u de código limpio, así como otros patrones vistos en clase indentificándolos dentro del código.

Prueba los métodos más importantes en el main.

Testea el servicio con JUnit y Mockito.
