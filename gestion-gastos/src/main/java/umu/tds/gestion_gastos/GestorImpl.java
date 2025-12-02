package umu.tds.gestion_gastos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.notificacion.Notificacion;

public class GestorImpl implements Gestor{

	@JsonProperty("gastos")
	private List<Gasto> gastos;
	@JsonProperty("notificacion")
	public List<Notificacion> notificaciones; 
	
	public GestorImpl() {
		this (new ArrayList<>(), new ArrayList<>());
	}
	
	public GestorImpl(List<? extends Gasto> productos, List<? extends Notificacion> compras) {
		this.gastos = (List<Gasto>) productos;
		this.notificaciones = (List<Notificacion>) compras;
	}

	@Override
	public List<? extends Gasto> getGastos() {
		return gastos;
	}
	
	public void setGastos(List<Gasto> gastos) {
		this.gastos = gastos;
	}

	@Override
	public List<? extends Notificacion> getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(List<Notificacion> notificaciones) {
		this.notificaciones = notificaciones;
	}

	
}
