package com.platzi.proyecto.excepcion;

public class PeliculaExistenteException extends RuntimeException {
	
	public PeliculaExistenteException(String titulo) {
		super("Error: La pelicula con el titulo: " + titulo + ", ya existe");
	}

}
