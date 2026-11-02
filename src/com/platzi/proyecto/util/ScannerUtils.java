package com.platzi.proyecto.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Scanner;

import com.platzi.proyecto.contenido.Genero;

public class ScannerUtils {
    public static Scanner scanner = new Scanner(System.in);

    public static String capturarTexto(String mensaje) {
        while (true) {
            System.out.println(mensaje + ":");
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty() && !texto.contains("|")) {
                return texto;
            }
            System.out.println("Error: escribe un texto no vacio y sin el caracter |.");
        }
    }

    public static int capturarNumero(String mensaje) {
        return capturarNumero(mensaje, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int capturarNumero(String mensaje, int minimo, int maximo) {
        while (true) {
            String texto = capturarTexto(mensaje);
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= minimo && numero <= maximo) {
                    return numero;
                }
                System.out.println("Error: el numero debe estar entre " + minimo + " y " + maximo + ".");
            } catch (NumberFormatException e) {
                System.out.println("Error: escribe un numero entero.");
            }
        }
    }

    public static LocalDate capturarFecha(String mensaje) {
        while (true) {
            String texto = capturarTexto(mensaje + " (yyyy-mm-dd)");
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Error: escribe una fecha valida en formato yyyy-mm-dd.");
            }
        }
    }

    public static double capturarDecimal(String mensaje) {
        while (true) {
            String texto = capturarTexto(mensaje);
            try {
                double valor = Double.parseDouble(texto.replace(',', '.'));
                if (Double.isFinite(valor)) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // Solicitar de nuevo sin consumir otra linea.
            }
            System.out.println("Error: escribe un numero decimal valido.");
        }
    }

    public static boolean capturarBoolean(String mensaje) {
        while (true) {
            String opcion = capturarTexto(mensaje);
            if (opcion.equalsIgnoreCase("si") || opcion.equalsIgnoreCase("sí")) {
                return true;
            }
            if (opcion.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Error: escribe si o no.");
        }
    }

    public static Genero capturarGenero(String mensaje) {
        System.out.println("Generos disponibles:");
        for (Genero genero : Genero.values()) {
            System.out.println(genero);
        }
        while (true) {
            String opcion = capturarTexto(mensaje).toUpperCase(Locale.ROOT).replaceAll("\\s+", "_");
            try {
                return Genero.valueOf(opcion);
            } catch (IllegalArgumentException e) {
                System.out.println("Error: genero no encontrado. Elige uno de la lista.");
            }
        }
    }
}
