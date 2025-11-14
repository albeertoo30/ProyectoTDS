package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;

public class Notificacion {
	
	private int id;
	private String mensaje;
	private LocalDate fecha;
	private boolean isLeida;
	
	
	public void marcarLeida() {
		this.isLeida = true;
	}

}
