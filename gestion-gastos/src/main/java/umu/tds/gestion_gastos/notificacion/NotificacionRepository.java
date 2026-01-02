package umu.tds.gestion_gastos.notificacion;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.gestion_gastos.alerta.Alerta;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;

public enum NotificacionRepository implements INotificacionRepository{

	INSTANCE;

	private List<Notificacion> listaNotificaciones;
	private final ObjectMapper mapper;
	
	
	public List<Notificacion> getNotificaciones() {
		return Collections.unmodifiableList(this.listaNotificaciones);
	}
    
	NotificacionRepository(){
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.listaNotificaciones = new ArrayList<Notificacion>();
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
	public List<Notificacion> findByFilter(Filtro<Notificacion> filter) {
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
	
	//En realidad se guardan en local, en las pruebas usamos otra ruta.
	@Override
	public void cargar(String rutaJson) throws IOException {
	    System.out.println("Cargando notificaciones");

	    Path fichero = Paths.get(rutaJson);
	    Files.createDirectories(fichero.getParent());

	    // Crear el archivo vacío si no existe
	    if (!Files.exists(fichero)) {
	        System.out.println("Archivo no existe, creando uno vacío...");
	        Files.createFile(fichero);
	        listaNotificaciones.clear();
	        return;
	    }

	    System.out.println("Leyendo notificaciones desde JSON...");
	    try (InputStream is = Files.newInputStream(fichero)) {
	        List<Notificacion> cargadas = mapper.readValue(is, new TypeReference<List<Notificacion>>() {});
	        listaNotificaciones.clear();
	        if (cargadas != null) listaNotificaciones.addAll(cargadas);
	    }
	}

 
	
	@Override
	public void guardar(String rutaJson) throws IOException {
	    Path fichero = Paths.get(rutaJson);
	    // Crear directorios si no existen
	    Files.createDirectories(fichero.getParent());

	    // Archivo temporal
	    Path tmp = fichero.resolveSibling(fichero.getFileName() + ".tmp");

	    try (OutputStream os = Files.newOutputStream(tmp,
	            StandardOpenOption.CREATE,
	            StandardOpenOption.TRUNCATE_EXISTING)) {

	        mapper.writerWithDefaultPrettyPrinter()
	              .writeValue(os, listaNotificaciones);
	    }

	    // Mover el temporal al archivo final
	    Files.move(tmp, fichero, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
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


	@Override
	public void crearNotificacion(String msg, double cantidad, String alertId, Categoria categoria, String idCuenta) {
		Notificacion noti = new Notificacion(msg, cantidad, alertId, categoria, idCuenta);
		this.add(noti);
	}

	@Override
	public List<Notificacion> getNotificacionesPorCuenta(String id) {
		return this.listaNotificaciones.stream()
		.filter(a -> a.getIDCuenta().equals(id))
		.collect(Collectors.toList());
	}
	
	
}
