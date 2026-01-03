package umu.tds.gestion_gastos.gasto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.usuario.Usuario;

public class Gasto {
	
	private int id;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;
	private double cantidad;
	private String descripcion;
	private Categoria categoria;
	private Cuenta cuenta;
	private Usuario usuario;
	
	
	// Constructor 1 (antes de usuario y cuenta para no tener que modificar codigo)
	public Gasto(int id, LocalDate fecha, double cantidad, String descripcion, Categoria categoria) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.categoria = categoria;
	}
	
	public Gasto(int id, LocalDate fecha, double cantidad, String descripcion, Categoria categoria, Cuenta cuenta, Usuario usuario) {
		super();
		this.id = id;
		this.fecha = fecha;
		this.cantidad = cantidad;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.cuenta = cuenta;
		this.usuario = usuario;
	}
	
	// Constructor vacío para Jackson
	public Gasto() {}

	// Getters
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
	
	@JsonIgnore
	public Cuenta getCuenta() {
		return cuenta;
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
	
	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public void setUsuario(Usuario pagador) {
		this.usuario = pagador;
	}

}
