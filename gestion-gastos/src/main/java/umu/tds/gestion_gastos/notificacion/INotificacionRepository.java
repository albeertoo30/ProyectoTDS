package umu.tds.gestion_gastos.notificacion;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface INotificacionRepository {

	void add(Notificacion n);
	Optional<Notificacion> getById(String id);
	List<Notificacion> getAllOrderedByDateDesc();
	List<Notificacion> findByDateRange(LocalDate desde, LocalDate hasta);
	List<Notificacion> findByFilter(INotificacionFilter filter);
	void marcarLeida(String id);
	void delete(String id);
	void cargar(Path rutaBase) throws IOException;
	void guardar(Path rutaBase) throws IOException;
	void limpiarHistorial();

}
