package umu.tds.gestion_gastos.categoria;

public class Categoria {
	
	private String nombre;
	private String descripcion;
	
	// Constructor
	public Categoria(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	// Getters
	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	// Setter descripción
}
