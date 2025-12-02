package umu.tds.gestion_gastos;

import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.notificacion.Notificacion;

public interface Gestor {
	List<? extends Gasto> getGastos();
	
	List<? extends Notificacion> getNotificaciones();


}
