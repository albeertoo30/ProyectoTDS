package umu.tds.gestion_gastos.alerta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.gestion_gastos.gasto.RepoGastos;
import umu.tds.gestion_gastos.notificacion.HistorialNotificaciones;

public class GestorAlertas{

	//Atributos
	private List<Alerta> alertas;
	private RepoGastos repoGastos;
	private HistorialNotificaciones historial;
	
	//Constructores
	public GestorAlertas(RepoGastos repoGastos, HistorialNotificaciones historial){
		this.repoGastos = repoGastos;
		this.historial = historial;
		this.alertas = new ArrayList<>();
	}
	
	public void addAlerta(Alerta a) {
		this.alertas.add(a);
	}
	
	public void eliminarAlerta(int id) {
		//Esto no se si hay que hacerlo asi o no, depende de la implementacion que queramos.
		//Solo la desactivamos.
		this.alertas.stream()
				.filter(a -> a.getId() == id)
				.forEach(a -> a.desactivarAlarma());
	}
	
	public void comprobarAlertas() {
		//de momento nada, que tenemos que comprobar?? 
	}
	
	public List<Alerta> getAlertasActivas(){
		return alertas.stream()
				.filter(Alerta::isActiva)
				.collect(Collectors.toList());
	}
	
	public HistorialNotificaciones getHistorial() {
		return this.historial;   // En historial nos encargamos de no pasarle cosas que no deberiamos.
	}
	
	public RepoGastos getRepoGastos() {
		return this.repoGastos;
	}
	
	
}

