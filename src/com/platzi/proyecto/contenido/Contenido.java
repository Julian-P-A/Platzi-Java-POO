package com.platzi.proyecto.contenido;

import java.security.PublicKey;
import java.time.LocalDate;

public abstract class Contenido {
	
	private String nombre;
	private String descripcion;
	private int duracion;
	private Genero genero;
	private int clasificacion;
	private boolean disponible;
	private LocalDate fecha;
	
	public Contenido(String nombre, int duracion, Genero genero, int clasificacion, LocalDate fecha) {
		if (nombre == null || nombre.isBlank() || nombre.matches("(?s).*[|\r\n].*")) {
			throw new IllegalArgumentException("El nombre es obligatorio y no puede contener | ni saltos de linea.");
		}
		this.nombre = nombre.trim();
		this.setDuracion(duracion);
		this.setGenero(genero);
		this.clasificar(clasificacion);
		this.setFecha(fecha);
		this.disponible = true;
		
	}
	
	public  abstract void reproducir();
	
	public abstract String obtenerFichaTecnica();
	
	public void clasificar( int clasificacion) {
		if (clasificacion < 0) {
			throw new IllegalArgumentException("La clasificacion no puede ser negativa.");
		}
		if (clasificacion >= 18) {
			this.clasificacion = 18;
		} else {
			this.clasificacion = clasificacion;
		} 
	}
	
	public boolean aptoSoloMayores () {
		return clasificacion == 18;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public int getDuracion() {
		return duracion;
	}
	
	public Genero getGenero() {
		return genero;
	}
	
	public int getClasificacion() {
		return clasificacion;
	}
	
	public boolean isDisponible() {
		return disponible;
	}
	
	public LocalDate getFecha() {
		return fecha;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public void setDuracion(int duracion) {
		if (duracion < 1) {
			throw new IllegalArgumentException("La duracion debe ser mayor que cero.");
		}
		this.duracion = duracion;
	}
	
	public void setGenero(Genero genero) {
		java.util.Objects.requireNonNull(genero, "El genero es obligatorio.");
		this.genero = genero;
	}
	
	public void setClasificacion(int clasificacion) {
		this.clasificar(clasificacion);
	}
	
	public void setFecha(LocalDate fecha) {
		java.util.Objects.requireNonNull(fecha, "La fecha es obligatoria.");
		this.fecha = fecha;
	}
	
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
}
