package umu.tds.gestion_gastos.notificacion;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;

public interface INotificacionRepository {

	void add(Notificacion n);
	List<Notificacion> getNotificaciones();
	Optional<Notificacion> getById(String id);
	List<Notificacion> getAllOrderedByDateDesc();
	List<Notificacion> findByDateRange(LocalDate desde, LocalDate hasta);
	List<Notificacion> findByFilter(Filtro<Notificacion> filter);
	void marcarLeida(String id);
	void delete(String id);
	void cargar(String rutaBase) throws IOException;
	void guardar(String rutaBase) throws IOException;
	void limpiarHistorial();
	void crearNotificacion(String msg, double cantidad, String alertId, Categoria categoria);
	
	
	
}
