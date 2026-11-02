package com.platzi.proyecto;

import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import com.platzi.proyecto.contenido.Genero;
import com.platzi.proyecto.contenido.Pelicula;
import com.platzi.proyecto.contenido.Promocionable;
import com.platzi.proyecto.contenido.Contenido;
import com.platzi.proyecto.contenido.Documental;
import com.platzi.proyecto.excepcion.PeliculaExistenteException;
import com.platzi.proyecto.excepcion.PeliculaNoExistenteException;


public class Plataforma {
	private String plataforma;
	private List<Contenido> contenido;
	private Map<Contenido, Integer> visualizaciones;
	
	public Plataforma(String plataforma) {
		this.plataforma = plataforma;
		this.contenido = new ArrayList<>();
		this.visualizaciones = new HashMap<>();
	}
	
	public void agregar(Contenido pelicula) {
		Contenido contenido = this.buscarPorTitulo(pelicula.getNombre());
		
		if (contenido != null) {
			throw new PeliculaExistenteException(pelicula.getNombre());		
		}
		
		this.contenido.add(pelicula);
		
	}
	
	public void reproducir(Contenido pelicula) {
		
		int conteoActual = visualizaciones.getOrDefault(pelicula, 0);
		System.out.println("El contenido " + pelicula.getNombre() + " ha sido reproducido " + conteoActual + " veces.");
		
		this.llevarConteo(pelicula);
		pelicula.reproducir();
	}
	
	public void llevarConteo(Contenido pelicula) {
		int conteoActual = visualizaciones.getOrDefault(pelicula, 0);
		visualizaciones.put(pelicula, conteoActual + 1);
	}
	
	public Contenido buscarPorTitulo(String nombre) {
		return contenido.stream().filter(pelicula -> pelicula.getNombre().equalsIgnoreCase(nombre)).findFirst().orElse(null);
	}
	
	public List<Contenido> buscarPorGenero(Genero genero) {
		return contenido.stream().filter(pelicula -> pelicula.getGenero().equals(genero)).toList();
	}
	
	public List<Contenido> ordenarPorDuracion(int cantidad, boolean deMayorAMenor) {
		if (cantidad < 1) {
			throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
		}
		if (deMayorAMenor) {
			return contenido.stream().sorted(Comparator.comparing(Contenido::getDuracion).reversed()).limit(cantidad).toList();
		} else {
			return contenido.stream().sorted(Comparator.comparing(Contenido::getDuracion)).limit(cantidad).toList();
		}
	}
	
	public void eliminarContenido(Contenido pelicula) {
		this.contenido.remove(pelicula);
	}
	
	public List<Promocionable> getContenidosPromocionables() {
		return contenido.stream().filter(Contenido -> Contenido instanceof Promocionable).map(Contenido -> (Promocionable) Contenido).toList();} // <Contenido>
	
	public int getDuracionTotal() {
		return contenido.stream().mapToInt(Contenido::getDuracion).sum();
	}
	
	public Contenido getMasLarga() {
		return contenido.stream().max(Comparator.comparing(Contenido::getDuracion)).orElse(null);
	}
	
	public Contenido getMasCorta() {
		return contenido.stream().min(Comparator.comparing(Contenido::getDuracion)).orElse(null);
	}
	
	public List<String> getTitulos() {
		return contenido.stream().map(Contenido::getNombre).toList();
	} 
	
	public List<Contenido> getContenidos() {
		return contenido;
	}
	
	public List<Pelicula> getPeliculas() {
		return contenido.stream().filter(pelicula -> pelicula instanceof Pelicula).map(pelicula -> (Pelicula) pelicula).toList();
	}
	
	public List<Documental> getDocumentales() {
		return contenido.stream().filter(documental -> documental instanceof Documental).map(documental -> (Documental) documental).toList();
	}
	
}
