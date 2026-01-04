package umu.tds.gestion_gastos.categoria;

import java.util.Objects;

public class Categoria {
	
	private String nombre;
	private String descripcion;
	
	// Constructor
	public Categoria(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	
	// Constructor vacío para Jackson
    public Categoria() {}

	// Getters
	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	// Setters
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		return Objects.hash(descripcion, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return this.getNombre();
	}
}
