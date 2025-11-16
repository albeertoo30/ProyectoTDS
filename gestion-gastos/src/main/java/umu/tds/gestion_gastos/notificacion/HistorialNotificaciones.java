package umu.tds.gestion_gastos.notificacion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistorialNotificaciones{
	//atributos
	public List<Notificacion> notificaciones;
	//constructores
	public HistorialNotificaciones() {
		this.notificaciones = new ArrayList<Notificacion>();
	}
	
	
	//metodos y getter
	//Esto añade la notificacion a la lista de historial supongo. NO LA CREA
	public void addNotificacion(Notificacion n) {
		this.notificaciones.add(n);
	}
	
	public List<Notificacion> getNotificaciones(){
		return Collections.unmodifiableList(this.notificaciones);
	}
	
	public void limpiarHistorial() {
		//marcarla como leida a todas las que no lo esten
		//pero no las quito de la lista porque hay que tenerlas. Historial persistente.
		this.notificaciones.stream()
			.filter(a -> !a.isLeida())
			.forEach(a -> a.marcarLeida());
	}
	
	
}
