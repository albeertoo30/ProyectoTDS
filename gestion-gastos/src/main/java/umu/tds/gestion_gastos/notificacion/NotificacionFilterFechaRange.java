package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;
import java.util.Objects;

import umu.tds.gestion_gastos.filtros.Filtro;

public class NotificacionFilterFechaRange implements Filtro<Notificacion>{
	private final LocalDate desde;
	private final LocalDate hasta;
	
	public NotificacionFilterFechaRange(LocalDate desde, LocalDate hasta) {

		this.desde = desde;
		this.hasta = hasta;
	}
	
	@Override
	public boolean test(Notificacion n) {
		Objects.requireNonNull(n, "Notificacion no puede ser null");
		LocalDate f = n.getFecha();
		if (desde != null && f.isBefore(desde)) return false;
		if (hasta != null && f.isAfter(hasta)) return false;
		return true;
	}
}
