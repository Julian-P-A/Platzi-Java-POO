package com.platzi.proyecto.contenido;

import java.time.LocalDate;

public class Documental extends Contenido implements Promocionable {
	private String narrador;
	
	public Documental(String nombre, int duracion, Genero genero, int clasificacion, LocalDate fecha, String narrador) {
		super(nombre, duracion, genero, clasificacion, fecha);
		if (narrador == null || narrador.isBlank() || narrador.matches("(?s).*[|\r\n].*")) {
			throw new IllegalArgumentException("El narrador es obligatorio y no puede contener | ni saltos de linea.");
		}
		this.narrador = narrador.trim();
	}
	
	@Override
	public void reproducir() {
		System.out.println("Reproduciendo " + this.getNombre() + " narrado por " + this.getNarrador());
	}
	
	@Override
	public String obtenerFichaTecnica() {
		return getNombre() + " (" + getFecha() + ")" + " (Duracion: " + getDuracion() + ")\n" +
				"Genero: " + getGenero() + ".\n" +
				"Clasificacion: +" + getClasificacion() + ".\n" +
				"Narrado por: " + narrador + ".\n" +
				"Disponibilidad: " + isDisponible() ;
	}
	
	@Override
	public String promocionar() {
		return "Descubre el documental " + this.getNombre() + " narrado por " + this.getNarrador() + " ahora en VerPelisPlus ";
	}
	
	public String getNarrador() {
		return narrador;
	}


}
