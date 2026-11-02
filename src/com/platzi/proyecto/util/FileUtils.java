package com.platzi.proyecto.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.platzi.proyecto.contenido.Contenido;
import com.platzi.proyecto.contenido.Documental;
import com.platzi.proyecto.contenido.Genero;
import com.platzi.proyecto.contenido.Pelicula;

public class FileUtils {
    public static final String URL = "src/contenido.txt";
    public static final String SEPARADOR = "|";
    public static final String NOMBRE_ARCHIVO = "contenido.txt";

    private static Path archivoContenido() {
        return Path.of(System.getProperty("verpelis.data", URL));
    }

    public static void guardarContenido(List<Contenido> contenidos) {
        escribirArchivo(archivoContenido(), contenidos);
    }

    public static void exportarContenido(List<Contenido> contenidos, String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.matches("[\\p{L}\\p{N}_ -]+")) {
            throw new IllegalArgumentException("Usa solo letras, numeros, espacios, guiones o guion bajo para el archivo.");
        }
        escribirArchivo(Path.of("exports", nombreArchivo + ".txt"), contenidos);
    }

    public static void escribirContenido(Contenido contenido) {
        List<Contenido> contenidos = leerContenido();
        contenidos.add(contenido);
        guardarContenido(contenidos);
    }

    private static void escribirArchivo(Path destino, List<Contenido> contenidos) {
        Path temporal = null;
        try {
            Path archivo = destino.toAbsolutePath();
            Files.createDirectories(archivo.getParent());
            temporal = Files.createTempFile(archivo.getParent(), ".verpelis-", ".tmp");
            Files.write(temporal, contenidos.stream().map(FileUtils::serializar).toList(), StandardCharsets.UTF_8);
            try {
                Files.move(temporal, archivo, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, archivo, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el archivo " + destino + ".", e);
        } finally {
            if (temporal != null) {
                try {
                    Files.deleteIfExists(temporal);
                } catch (IOException e) {
                    System.err.println("No se pudo limpiar el archivo temporal: " + temporal);
                }
            }
        }
    }

    private static String serializar(Contenido contenido) {
        String linea = String.join(SEPARADOR, contenido.getNombre(), String.valueOf(contenido.getDuracion()),
                contenido.getGenero().name(), String.valueOf(contenido.getClasificacion()), contenido.getFecha().toString());
        if (contenido instanceof Documental documental) {
            return "DOCUMENTAL|" + linea + SEPARADOR + documental.getNarrador();
        }
        return "PELICULA|" + linea;
    }

    public static List<Contenido> leerContenido() {
        Path archivo = archivoContenido();
        List<Contenido> contenidos = new ArrayList<>();
        if (Files.notExists(archivo)) {
            return contenidos;
        }
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            for (int i = 0; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea.isBlank()) {
                    continue;
                }
                try {
                    String[] datos = linea.split("\\|", -1);
                    boolean documental = datos[0].equals("DOCUMENTAL") && datos.length == 7;
                    if (!documental && !(datos[0].equals("PELICULA") && datos.length == 6)) {
                        throw new IllegalArgumentException("Formato no reconocido.");
                    }
                    int duracion = Integer.parseInt(datos[2]);
                    Genero genero = Genero.valueOf(datos[3].toUpperCase(Locale.ROOT));
                    int clasificacion = Integer.parseInt(datos[4]);
                    LocalDate fecha = LocalDate.parse(datos[5]);
                    Contenido contenido = documental
                            ? new Documental(datos[1], duracion, genero, clasificacion, fecha, datos[6])
                            : new Pelicula(datos[1], duracion, genero, clasificacion, fecha);
                    if (contenidos.stream().noneMatch(c -> c.getNombre().equalsIgnoreCase(contenido.getNombre()))) {
                        contenidos.add(contenido);
                    }
                } catch (IllegalArgumentException | DateTimeException e) {
                    throw new IllegalArgumentException("Contenido invalido en " + archivo + ", linea " + (i + 1)
                            + ". Corrige esa linea antes de continuar.", e);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el archivo " + archivo + ".", e);
        }
        return contenidos;
    }
}
