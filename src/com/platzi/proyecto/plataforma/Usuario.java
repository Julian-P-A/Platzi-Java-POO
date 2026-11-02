package com.platzi.proyecto.plataforma;

import java.time.LocalDateTime;

import com.platzi.proyecto.contenido.Contenido;
import com.platzi.proyecto.contenido.Pelicula;

public class Usuario {
	private String userNombre;
	private String correo;
	private LocalDateTime fechaRegistro;
	private int edad;
	
	public Usuario(String userNombre, int edad, String correo) {
		this.userNombre = userNombre;
		this.edad = edad;
		this.correo = correo;
		this.fechaRegistro = LocalDateTime.now();
	}
	
	public void ver(Contenido pelicula) {
		boolean apto = pelicula.aptoSoloMayores();	
		
		if ((apto==true && edad >= 18 ) || apto==false) {
			System.out.println(userNombre);
			pelicula.reproducir();
	    } else {
			System.out.println("Lo siento " + userNombre + ", no puedes ver esta pelicula");
	    }	
	}
	
	public String getUserNombre() {
		return userNombre;
	}
	
	public int getEdad() {
		return edad;
	}
	
	public String getCorreo() {
		return correo;
	}
	
	public LocalDateTime getFechaRegistro() {
		return fechaRegistro;
	}
	
	public void setEdad(int edad) {
		this.edad = edad;
	}
	
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public void setUserNombre(String userNombre) {
		this.userNombre = userNombre;
	}	
	
}
