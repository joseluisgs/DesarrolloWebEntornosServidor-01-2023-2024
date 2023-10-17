- [Gestores de dependencias y construcción de proyectos](#gestores-de-dependencias-y-construcción-de-proyectos)
  - [Ejemplo Maven](#ejemplo-maven)
  - [Ejemplo Gradle](#ejemplo-gradle)
  - [Ejemplo NPM](#ejemplo-npm)
  - [Generación de código con Lombok](#generación-de-código-con-lombok)


## Gestores de dependencias y construcción de proyectos
Las herramientas de construcción (build tools) son programas que ayudan en la automatización del proceso de construcción y gestión de proyectos de software. Estas herramientas permiten compilar el código fuente, gestionar dependencias, ejecutar pruebas, empaquetar y desplegar la aplicación, entre otras tareas relacionadas con el ciclo de vida del desarrollo de software.

Algunas de las herramientas de construcción más populares en el desarrollo de software son:

1. Maven: Maven es una herramienta de construcción basada en XML que utiliza un archivo de configuración llamado POM (Project Object Model). Maven se enfoca en la gestión de dependencias y promueve una estructura de proyecto estandarizada. Es ampliamente utilizado en proyectos Java y tiene una gran cantidad de complementos disponibles.

2. Gradle: Gradle es una herramienta de construcción basada en Groovy y Kotlin. Utiliza un lenguaje de dominio específico (DSL) para definir la configuración del proyecto. Gradle ofrece una gran flexibilidad y rendimiento, y se utiliza en proyectos de diversos lenguajes de programación, incluyendo Java, Kotlin, Groovy, Android y más.

3. Make: Make es una herramienta de construcción clásica que utiliza archivos de configuración llamados Makefiles. Se basa en reglas y dependencias para determinar qué partes del proyecto deben compilarse y construirse. Make es ampliamente utilizado en proyectos C/C++ y permite la construcción incremental eficiente. 
   
4. Npm (Node Package Manager) es una herramienta de construcción y gestión de paquetes muy popular en el ecosistema de desarrollo de Node.js. Aunque npm es principalmente conocido por su capacidad de gestionar dependencias de Node.js, también se utiliza como una herramienta de construcción para proyectos web. En el contexto de npm, el archivo package.json es el archivo de configuración principal. npm también proporciona herramientas para publicar paquetes en el registro de paquetes de npm, gestionar configuraciones de proyectos y más.

5. Composer utiliza un archivo de configuración llamado composer.json para definir las dependencias del proyecto y otras configuraciones relacionadas. A través de este archivo, puedes especificar las bibliotecas y paquetes de PHP que tu proyecto necesita, junto con sus versiones. Composer se encarga de descargar automáticamente las dependencias desde el repositorio de paquetes de Composer y las almacena en la carpeta "vendor" del proyecto.

Estas son solo algunas de las herramientas de construcción más populares, pero hay muchas otras disponibles en el mundo del desarrollo de software. Cada herramienta tiene sus propias características y enfoques, por lo que es importante evaluar las necesidades del proyecto y elegir la herramienta de construcción más adecuada.

### Ejemplo Maven
Aquí tienes un ejemplo simplificado de un archivo POM que incluye una dependencia de MySQL y la configuración para ejecutar pruebas con JUnit:
```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
         
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>my-project</artifactId>
    <version>1.0.0</version>
    
    <dependencies>
        <!-- Dependencia de MySQL -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.26</version>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Plugin para compilar y ejecutar pruebas con JUnit -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.0.0-M5</version>
                <configuration>
                    <includes>
                        <include>**/*Test.java</include>
                    </includes>
                </configuration>
            </plugin>
        </plugins>
    </build>
    
</project>
```

### Ejemplo Gradle
Aquí tienes un ejemplo simplificado de un archivo de construcción (build.gradle) que incluye una dependencia de MySQL y la configuración para ejecutar pruebas con JUnit en Gradle:

```kotlin
plugins {
    java
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Dependencia de MySQL
    implementation("mysql:mysql-connector-java:8.0.26")
    // Dependencia de JUnit
    testImplementation("junit:junit:4.13.2")
}

tasks.test {
    useJUnit()
}

```

### Ejemplo NPM
Aquí tienes un ejemplo típico de un archivo package.json en un proyecto de Node.js, con scripts de inicio, prueba y construcción, y una librería conocida como Express:

```json
{
  "name": "mi-proyecto",
  "version": "1.0.0",
  "description": "Descripción de mi proyecto",
  "main": "index.js",
  "scripts": {
    "start": "node index.js",
    "test": "mocha",
    "build": "babel src -d dist"
  },
  "dependencies": {
    "express": "^4.17.1"
  },
  "devDependencies": {
    "mocha": "^9.1.1",
    "babel-cli": "^6.26.0",
    "babel-preset-env": "^1.7.0"
  }
}
```

### Generación de código con Lombok
[Lombok](https://projectlombok.org/) es una biblioteca de Java que se utiliza para reducir el código repetitivo en las clases de Java, especialmente para los métodos como getters, setters, equals, hashCode y toString, y para los constructores. Aquí te dejo un ejemplo de cómo se puede usar:

```java
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Persona {
    private String nombre;
    private String apellido;
    private int edad;
}
```

En este ejemplo, las anotaciones `@Getter` y `@Setter` generan automáticamente los métodos getter y setter para cada campo en la clase. La anotación `@EqualsAndHashCode` genera automáticamente los métodos `equals(Object other)` y `hashCode()`. La anotación `@ToString` genera automáticamente un método `toString()`.

También puedes usar `@Data` para combinar todas estas anotaciones en unam, creando una clase POJO:

```java
@Data
public class Persona {
    private String nombre;
    private String apellido;
    private int edad;
}
```

Además de estas anotaciones, Lombok también proporciona anotaciones como `@NoArgsConstructor`, `@AllArgsConstructor` y `@Builder` para generar automáticamente constructores sin argumentos, constructores con argumentos para todos los campos y un método de constructor de estilo builder, respectivamente.

```java
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {
    private String nombre;
    private String apellido;
    private int edad;
}
```

En este ejemplo, `@NoArgsConstructor` genera un constructor sin argumentos, `@AllArgsConstructor` genera un constructor con argumentos para todos los campos y `@Builder` permite que la clase se pueda construir en el estilo builder:

```java
Persona persona = Persona.builder()
                         .nombre("Juan")
                         .apellido("Perez")
                         .edad(25)
                         .build();
```

Es importante mencionar que para que Lombok funcione correctamente, necesitarás instalar el plugin de Lombok en tu IDE y habilitar la opción de procesamiento de anotaciones.

