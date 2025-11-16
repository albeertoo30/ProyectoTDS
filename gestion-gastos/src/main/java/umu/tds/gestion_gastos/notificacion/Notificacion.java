package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;

public class Notificacion {
	
	private int id;
	private String mensaje;
	private LocalDate fecha;
	private boolean leida;
	
	public Notificacion(int id, String mensaje) {
		//Por defecto la fecha actual y no leida
		this.id = id;
		this.mensaje = mensaje;
		this.fecha = LocalDate.now();
		this.leida = false;
	}	
	
	
	public void marcarLeida() {
		this.leida = true;
	}
	
	//Nada de setters, para que quiero modificar una notificacion.
	//getters todos
	public int getId() {
		return this.id;
	}
	
	public String getMensaje() {
		return this.mensaje;
	}
	
	public LocalDate getFecha() {
		return this.fecha;
	}
	
	public boolean isLeida() {
		return this.leida;
	}
	

}
