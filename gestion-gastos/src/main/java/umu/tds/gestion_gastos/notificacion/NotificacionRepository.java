package umu.tds.gestion_gastos.notificacion;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum NotificacionRepository implements INotificacionRepository{

	INSTANCE;

	private List<Notificacion> listaNotificaciones;
    private static final String NOMBRE_FICHERO = "notificaciones.json";
	
	public List<Notificacion> getNotificaciones() {
		return Collections.unmodifiableList(this.listaNotificaciones);
	}
    
    
	@Override
	public void add(Notificacion n) {
		if (n == null) return; 
		listaNotificaciones.add(n);
	}

	@Override
	public Optional<Notificacion> getById(String id) {
		return listaNotificaciones.stream().filter(x -> x.getId().equals(id)).findFirst();

	}

	@Override
	public List<Notificacion> getAllOrderedByDateDesc() {
		return listaNotificaciones.stream()
				.sorted(Comparator.comparing(Notificacion::getFecha).reversed())	
				.collect(Collectors.toList());
	}

	@Override
	public List<Notificacion> findByDateRange(LocalDate desde, LocalDate hasta) {
		return findByFilter(new NotificacionFilterFechaRange(desde, hasta));
	}

	@Override
	public List<Notificacion> findByFilter(INotificacionFilter filter) {
		return this.listaNotificaciones.stream()
				.filter(filter)
				.sorted(Comparator.comparing(Notificacion::getFecha).reversed())
				.collect(Collectors.toList());
	}
	
	@Override
	public void marcarLeida(String id) {
		getById(id).ifPresent(Notificacion::marcarLeida);
	}

	@Override
	public void delete(String id) {
		getById(id).ifPresent(listaNotificaciones::remove);
	}
	

	@Override
	public void cargar(Path rutaBase) throws IOException {
		Path fichero = rutaBase.resolve(NOMBRE_FICHERO);
        if (Files.exists(fichero)) {
            ObjectMapper mapper = new ObjectMapper();
            listaNotificaciones = mapper.readValue(fichero.toFile(),
                    new TypeReference<List<Notificacion>>() {});
        }
	}

	@Override
	public void guardar(Path rutaBase) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(rutaBase.resolve(NOMBRE_FICHERO).toFile(), listaNotificaciones);	
	}

	//Marca todas las notificaciones como leidas
	@Override
	public void limpiarHistorial() {
		//marcarla como leida a todas las que no lo esten
		//pero no las quito de la lista porque hay que tenerlas. Historial persistente.
		this.listaNotificaciones.stream()
			.filter(a -> !a.isLeida())
			.forEach(a -> a.marcarLeida());
	}
	
	
}
