package com.platzi.proyecto;

import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.platzi.proyecto.contenido.Contenido;
import com.platzi.proyecto.contenido.Documental;
import com.platzi.proyecto.contenido.Genero;
import com.platzi.proyecto.contenido.Pelicula;
import com.platzi.proyecto.util.FileUtils;
import com.platzi.proyecto.util.ScannerUtils;

public class MainClass {
    public static final String VERSION = "1.0.2";
    public static final String NOMBRE_PLATAFORMA = "VerPelisPlus";
    public static final int AGREGAR_CONTENIDO = 1;
    public static final int MOSTRAR_CONTENIDO = 2;
    public static final int BUSCAR_POR_GENERO = 3;
    public static final int BUSCAR_POR_TITULO = 4;
    public static final int ORDENAR_POR_DURACION = 5;
    public static final int REPRODUCIR = 6;
    public static final int EXPORTAR_CONTENIDO = 7;
    public static final int ELIMINAR_CONTENIDO = 9;
    public static final int SALIR = 10;

    public static void main(String[] args) {
        Plataforma plataforma = new Plataforma(NOMBRE_PLATAFORMA);
        try {
            plataforma.getContenidos().addAll(FileUtils.leerContenido());
        } catch (IllegalArgumentException | UncheckedIOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
            return;
        }
        System.out.println("Bienvenido a " + NOMBRE_PLATAFORMA + " v" + VERSION);
        System.out.println("Mas de " + plataforma.getDuracionTotal() + " minutos de contenido!");
        plataforma.getContenidosPromocionables().forEach(c -> System.out.println(c.promocionar()));

        try {
            boolean continuar = true;
            while (continuar) {
                int opcion = ScannerUtils.capturarNumero("""
                        -----------------------------------
                        1. Agregar contenido
                        2. Mostrar todo
                        3. Buscar por genero
                        4. Buscar por titulo
                        5. Ordenar por duracion
                        6. Reproducir
                        7. Exportar
                        9. Eliminar
                        10. Salir
                        -----------------------------------
                        Ingresa la opcion que quieres hacer""");
                try {
                    switch (opcion) {
                        case AGREGAR_CONTENIDO -> agregar(plataforma);
                        case MOSTRAR_CONTENIDO -> {
                            System.out.println("Numero de elementos en la plataforma: " + plataforma.getContenidos().size());
                            plataforma.getTitulos().forEach(System.out::println);
                        }
                        case BUSCAR_POR_GENERO -> {
                            Genero genero = ScannerUtils.capturarGenero("Ingresa el genero de la pelicula");
                            List<Contenido> encontrados = plataforma.buscarPorGenero(genero);
                            System.out.println(encontrados.size() + " peliculas encontradas del genero " + genero);
                            mostrarFichas(encontrados);
                        }
                        case BUSCAR_POR_TITULO -> {
                            Contenido contenido = buscar(plataforma);
                            if (contenido != null) {
                                System.out.println(contenido.obtenerFichaTecnica());
                                System.out.println("Pelicula encontrada con exito");
                            }
                        }
                        case ORDENAR_POR_DURACION -> {
                            int cantidad = ScannerUtils.capturarNumero("Ingresa la cantidad de peliculas que quieres ver", 1, Integer.MAX_VALUE);
                            boolean descendente = ScannerUtils.capturarBoolean("Ordenar de mayor a menor (si/no)");
                            mostrarFichas(plataforma.ordenarPorDuracion(cantidad, descendente));
                            System.out.println("Peliculas ordenadas con exito");
                        }
                        case REPRODUCIR -> {
                            Contenido contenido = buscar(plataforma);
                            if (contenido != null) {
                                plataforma.reproducir(contenido);
                            }
                        }
                        case EXPORTAR_CONTENIDO -> {
                            String nombre = ScannerUtils.capturarTexto("Nombre del archivo sin extension");
                            FileUtils.exportarContenido(plataforma.getContenidos(), nombre);
                            System.out.println("Contenido exportado a exports/" + nombre + ".txt");
                        }
                        case ELIMINAR_CONTENIDO -> eliminar(plataforma);
                        case SALIR -> continuar = false;
                        default -> System.out.println("Opcion inexistente: " + opcion);
                    }
                } catch (IllegalArgumentException | UncheckedIOException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Fin de la entrada.");
        }
        System.out.println("Gracias por usar " + NOMBRE_PLATAFORMA);
    }

    private static void agregar(Plataforma plataforma) {
        int tipo = ScannerUtils.capturarNumero("Tipo de contenido: 1. Pelicula, 2. Documental", 1, 2);
        String nombre = ScannerUtils.capturarTexto("Nombre del contenido");
        int duracion = ScannerUtils.capturarNumero("Duracion en minutos", 1, Integer.MAX_VALUE);
        Genero genero = ScannerUtils.capturarGenero("Genero");
        int clasificacion = ScannerUtils.capturarNumero("Edad recomendada (0 a 18)", 0, 18);
        LocalDate fecha = ScannerUtils.capturarFecha("Fecha de estreno");
        Contenido contenido = tipo == 1
                ? new Pelicula(nombre, duracion, genero, clasificacion, fecha)
                : new Documental(nombre, duracion, genero, clasificacion, fecha,
                        ScannerUtils.capturarTexto("Nombre del narrador"));
        if (plataforma.buscarPorTitulo(nombre) != null) {
            System.out.println("Error: la pelicula con el titulo " + nombre + " ya existe.");
            return;
        }
        List<Contenido> actualizados = new ArrayList<>(plataforma.getContenidos());
        actualizados.add(contenido);
        FileUtils.guardarContenido(actualizados);
        plataforma.agregar(contenido);
        System.out.println("Felicidades, se ha cargado con exito");
    }

    private static void eliminar(Plataforma plataforma) {
        Contenido contenido = buscar(plataforma);
        if (contenido != null) {
            List<Contenido> actualizados = new ArrayList<>(plataforma.getContenidos());
            actualizados.remove(contenido);
            FileUtils.guardarContenido(actualizados);
            plataforma.eliminarContenido(contenido);
            System.out.println("Contenido eliminado y cambios guardados.");
        }
    }

    private static Contenido buscar(Plataforma plataforma) {
        Contenido contenido = plataforma.buscarPorTitulo(ScannerUtils.capturarTexto("Ingresa el titulo de la pelicula"));
        if (contenido == null) {
            System.out.println("Pelicula no encontrada");
        }
        return contenido;
    }

    private static void mostrarFichas(List<Contenido> contenidos) {
        contenidos.forEach(c -> {
            System.out.println("-----------------------------------");
            System.out.println(c.obtenerFichaTecnica());
        });
    }
}
