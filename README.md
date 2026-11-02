# VerPelisPlus — Java y programación orientada a objetos

Catálogo educativo de películas y documentales por consola. Permite agregar, listar,
buscar por título o género, ordenar por duración, eliminar y exportar el catálogo.
La reproducción es una simulación por consola.

## Ejecutar

Necesitas un JDK 21. Desde esta carpeta, en macOS o Linux:

```sh
./run.sh
```

El script compila el código y abre el menú. En Eclipse, importa el proyecto como
proyecto Java existente y selecciona un JDK 21. Ejecuta `com.platzi.proyecto.MainClass`
con el directorio de trabajo en la raíz del proyecto.

Para compilar y ejecutar manualmente (también en Windows, usando los comandos
correspondientes para listar los archivos Java):

```sh
mkdir -p bin
find src -name '*.java' > bin/sources.txt
javac --release 21 -encoding UTF-8 -d bin @bin/sources.txt
java -cp bin com.platzi.proyecto.MainClass
```

## Datos

- `src/contenido.txt` contiene el catálogo inicial y guarda los cambios de altas y bajas.
- Las escrituras reemplazan el archivo completo mediante un archivo temporal.
- Los títulos repetidos se rechazan sin modificar el archivo.
- Las exportaciones se crean en `exports/`, que se excluye de Git.
- Si hay una línea inválida en el catálogo, la aplicación indica cuál es y se detiene
  sin sobrescribir el archivo. Puedes corregir esa línea y volver a ejecutar.
- Puedes elegir otro archivo con `java -Dverpelis.data=/ruta/catalogo.txt -cp bin com.platzi.proyecto.MainClass`.

Formato de los registros:

```text
PELICULA|Titulo|90|ACCION|12|2026-01-01
DOCUMENTAL|Titulo|42|CIENCIA_FICCION|0|2026-01-01|Narrador
```

La edad recomendada se introduce entre 0 y 18 años; las duraciones deben ser positivas.
Los géneros aceptan espacios o guion bajo: `ciencia ficcion` y `CIENCIA_FICCION`.

## Pruebas

Con Python 3 y JDK 21:

```sh
python3 -m unittest discover -s tests -v
```

Las pruebas compilan desde cero y usan archivos temporales: no cambian tu catálogo.
Comprueban persistencia después de reiniciar, búsquedas, exportación, entradas
inválidas y protección de archivos corruptos. GitHub Actions ejecuta estas pruebas
al subir el repositorio.

## Subir a GitHub

Sube esta carpeta como un repositorio independiente. Incluye `src/`, `tests/`,
`run.sh`, este README y `.github/`. El archivo `.gitignore` excluye clases compiladas,
exportaciones, archivos del IDE y configuración local.
