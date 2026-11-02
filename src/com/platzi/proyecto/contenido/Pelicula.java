package com.platzi.proyecto.contenido;

import java.time.LocalDate;

public class Pelicula extends Contenido {
	public Pelicula(String nombre, int duracion, Genero genero, int clasificacion, LocalDate fecha) {
		super(nombre, duracion, genero, clasificacion, fecha);
	}
	
	@Override
	public void reproducir() {
		System.out.println("Reproduciendo " + this.getNombre());
	}
	
	@Override
	public String obtenerFichaTecnica() {
		return getNombre() + " (" + getFecha() + ")" + " (Duracion: " + getDuracion() + ")\n" +
				"Genero: " + getGenero() + ".\n" +
				"Clasificacion: +" + getClasificacion() + ".\n" +
				"Disponibilidad: " + isDisponible() ;
	}

}
