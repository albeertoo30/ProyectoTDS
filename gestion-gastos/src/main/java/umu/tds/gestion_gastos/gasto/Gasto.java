package umu.tds.gestion_gastos.gasto;

import java.time.LocalDate;

import umu.tds.gestion_gastos.categoria.Categoria;

public class Gasto {
	
	private int id;
	private LocalDate fecha;
	private double cantidad;
	private String descripcion;
	//private Cuenta cuenta; para cuando se cree la clase cuenta
	private Categoria categoria;
	
	// Constructor (falta cuenta)
	public Gasto(int id, LocalDate fecha, double cantidad, String descripcion, Categoria categoria) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.categoria = categoria;
	}

	// Getters (falta cuenta)
	public int getId() {
		return id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public double getCantidad() {
		return cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public Categoria getCategoria() {
		return categoria;
	}
	
	// Setter categoría
	public void setCategoria(Categoria categoria) {
		 this.categoria = categoria;
	 }
	
	/*@Override // Comentado para que no salte el error por cuenta
	public String toString() {
		return String.format(getClass().getName() + ": [id=%d, fecha=%s, cantidad=%.2f, "
				+ "descripcion=%s, cuenta=%s, categoria=%s]", 
				id, fecha, cantidad, descripcion, cuenta, categoria != null ? categoria.getNombre() : "N/A");
	}*/

	 

}
