package umu.tds.gestion_gastos.usuario;

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
	
}
