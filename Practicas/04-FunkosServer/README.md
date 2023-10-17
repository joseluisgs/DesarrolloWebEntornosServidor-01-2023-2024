# Funkos Server

En el directorio data tienes un csv de muestra de Funkos. Con la siguiente estructura:
- COD: En formato UUID v4
- NOMBRE: Cadena de caracteres
- MODELO: Solo tiene estos valores: MARVEL, DISNEY, ANIME u OTROS
- PRECIO: Moneda con dos decimales.
- FECHA_LANZAMIENTO: Fecha en formato YYYY-MM-DD siguiendo ISO-8601

Antes de procesarlos ten en cuenta que puede haber errores en los campos.

***Vamos a trabajar con un cliente servidor***.

El servidor, que escucha en el puerto 3000, nada mas arrancar lee el fichero de funkos.csv del directorio data y lo carga en un abase de datos.

El Servidor debe procesar las siguientes llamadas:
- Obtener todos los funkos.
- Obtener funkos por código.
- Obtener funkos por modelo.
- Obtener funkos por año de lanzamiento.
- Insertar un funko.
- Actualizar un funko.
- Eliminar un funko.

El servidor debe ser capaz de procesar las peticiones y saber responder ante errores inesperados.

Además, tendrá un sistema de autenticación y autorización basado en JWT, sabiendo que solo los administradores pueden borrar funkos.

Las conexiones deben ser seguras.

Se debe desplegar el servidor en Docker y mostrar como el cliente interactúa con él.
  
Se recomienda usar un Logger en todo el proceso y testear los elementos más relevantes.

Se debe asegurar que repositorios y servicios estén testeados al 85% de cobertura. Se debe mostrar el informe con Jacoco o IntellIj

Se debe desplegar el servidor usando docker, para ello se recomienda usar una etapa de compilación y otra de ejecución.

## Entrega

Video de Youtube, no necesitas que sea público, si no que se vea a quien le pases el enlace. Este vídeo debe ser de 25 minutos, mostrar el código, explicar el proceso de desarrollo, test, despliegue y mostrar el funcionamiento usando el docker del servidor.

En el aula virtual debes entregar el enlace a tu repositorio y el enlace al vídeo de Youtube.

Para entregar se debe crear un repositorio con el código siguiendo GitFlow. En el README.md de tu proyecto debes explicar cómo has realizado el proceso y mostrar capturas del proceso y analizar los distintos elementos y cómo se han desarrollado. Posteriormente se incluirá el enlace del repositorio en el aula virtual.

La práctica puede hacerse en parejas o individualmente. En caso de hacerse en parejas, se debe indicar en el README.md del proyecto el nombre del compañero o compañera.

Se valorará:
- Solución aportada
- Uso de Arquitecturas Limpias y código limpio
- Principios SOLID
- Test unitarios y con dobles.
- Despliegue en Docker


