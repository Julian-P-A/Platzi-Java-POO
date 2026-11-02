# Java POO — Programación Orientada a Objetos

Proyecto académico desarrollado en **Java** como parte de mi proceso de aprendizaje de la **Programación Orientada a Objetos (POO)**, aplicando conocimientos adquiridos durante mis estudios en Platzi en 2021.

El ejercicio utiliza un sistema ficticio de gestión de películas y documentales para poner en práctica los principales conceptos de POO mediante clases, objetos, relaciones y comportamientos.

## Conceptos implementados

- **Clases y objetos:** representación de entidades mediante atributos y métodos.
- **Encapsulamiento:** uso de modificadores de acceso, getters y setters.
- **Herencia:** reutilización de atributos y comportamientos mediante clases derivadas.
- **Polimorfismo:** implementación de métodos con comportamientos específicos según el tipo de objeto.
- **Abstracción:** definición de una clase abstracta para representar elementos comunes.
- **Interfaces:** establecimiento de contratos entre clases.
- **Enumeraciones:** clasificación de contenido mediante `enum`.
- **Colecciones:** manipulación de listas y mapas con Java Collections y Streams.
- **Excepciones:** manejo de errores y validación de datos.
- **Persistencia:** lectura y escritura de información mediante archivos de texto.

## Funcionalidades del ejercicio

El proyecto incluye un menú interactivo en consola que permite:

1. Registrar películas y documentales.
2. Consultar el contenido disponible.
3. Realizar búsquedas por título y género.
4. Ordenar elementos según su duración.
5. Simular la reproducción de contenido.
6. Exportar información a archivos de texto.
7. Eliminar registros.

Estas funcionalidades permiten demostrar la interacción entre objetos y aplicar los fundamentos de Java POO en un escenario práctico.

## Estructura principal

- `MainClass.java`: punto de entrada y menú interactivo.
- `Plataforma.java`: administración de contenidos.
- `Contenido.java`: clase abstracta principal.
- `Pelicula.java`: clase derivada de `Contenido`.
- `Documental.java`: clase derivada que implementa una interfaz.
- `Promocionable.java`: interfaz para comportamientos específicos.
- `Genero.java`: enumeración de categorías.
- `Usuario.java`: modelado de usuarios y restricciones de edad.
- `FileUtils.java`: gestión de archivos.
- `ScannerUtils.java`: captura y validación de entradas.

## Tecnologías

- Java
- JDK 21 para la versión actual
- Programación Orientada a Objetos
- Java Collections y Streams
- Eclipse IDE
- Git y GitHub

## Ejecución

Clona el repositorio:

`git clone https://github.com/Julian-P-A/Platzi-Java-POO.git`

Ingresa al directorio:

`cd Platzi-Java-POO`

En macOS o Linux, ejecuta:

`./run.sh`

También puedes importar el proyecto en Eclipse y ejecutar la clase `com.platzi.proyecto.MainClass`, configurando JDK 21.

## Sobre el proyecto

Este repositorio conserva un ejercicio realizado durante mi proceso de formación en desarrollo de software. Su propósito es evidenciar la comprensión y aplicación de los fundamentos de la Programación Orientada a Objetos, más que ofrecer una aplicación destinada a producción.
