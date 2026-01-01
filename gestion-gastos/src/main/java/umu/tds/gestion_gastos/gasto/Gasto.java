package umu.tds.gestion_gastos.gasto;

import java.time.LocalDate;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.usuario.Usuario;

public class Gasto {
	
	private int id;
	private LocalDate fecha;
	private double cantidad;
	private String descripcion;
	//private Cuenta cuenta; para cuando se cree la clase cuenta
	private Usuario usuario;
	private Categoria categoria;
	
	// Constructor
	public Gasto(int id, LocalDate fecha, double cantidad, String descripcion, Usuario usuario, Categoria categoria) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.usuario = usuario;
		this.categoria = categoria;
	}
	
	// Constructor vacío para Jackson
	public Gasto() {}

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

	public Usuario getUsuario() {
		return usuario;
	}
	
	public Categoria getCategoria() {
		return categoria;
	}
	
	// Setters
	public void setCategoria(Categoria categoria) {
		 this.categoria = categoria;
	 }

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
