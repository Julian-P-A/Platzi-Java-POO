package com.platzi.proyecto.excepcion;

public class PeliculaNoExistenteException extends RuntimeException {
	
	public PeliculaNoExistenteException(String titulo) {
		super("Error: La pelicula con el titulo: " + titulo + ", no existe");
	}

}
