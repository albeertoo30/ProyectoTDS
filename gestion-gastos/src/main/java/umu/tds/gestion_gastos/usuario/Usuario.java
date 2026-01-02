package umu.tds.gestion_gastos.usuario;

import java.util.Objects;

public class Usuario {

	
	private int id;
	private String nombre;
	

	//jacksooooon
	public Usuario() {

	}
	
	
	public Usuario(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public int getId() {
		return this.id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
	    return this.nombre; 
	}


	@Override
	public int hashCode() {
		return Objects.hash(id, nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return id == other.id && Objects.equals(nombre, other.nombre);
	}
	
}
