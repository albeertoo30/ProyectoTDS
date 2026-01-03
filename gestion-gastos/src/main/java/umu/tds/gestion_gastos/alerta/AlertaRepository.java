package umu.tds.gestion_gastos.alerta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
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

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;
import umu.tds.gestion_gastos.notificacion.Notificacion;

public enum AlertaRepository implements IAlertaRepository{ // Es observer de Gasto, YA no, ahora es el gestor.

	INSTANCE;
	
	private final ObjectMapper mapper; 
	private final List<Alerta> listaAlertas;
	
	
	AlertaRepository(){
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
		this.listaAlertas = new ArrayList<Alerta>();
	}
	
	//En realidad se guardan en local, en las pruebas usamos otra ruta.
	@Override
	public void cargar(String rutaJson) throws IOException {
	    System.out.println("Cargando alertas");

	    Path fichero = Paths.get(rutaJson);
	    Files.createDirectories(fichero.getParent());

	    // Crear el archivo vacío si no existe
	    if (!Files.exists(fichero)) {
	        System.out.println("Archivo no existe, creando uno vacío...");
	        Files.createFile(fichero);
	        listaAlertas.clear();
	        return;
	    }

	    System.out.println("Leyendo notificaciones desde JSON...");
	    try (InputStream is = Files.newInputStream(fichero)) {
	        List<Alerta> cargadas = mapper.readValue(is, new TypeReference<List<Alerta>>() {});
	        listaAlertas.clear();
	        if (cargadas != null) listaAlertas.addAll(cargadas);
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
	              .writeValue(os, listaAlertas);
	    }

	    // Mover el temporal al archivo final
	    Files.move(tmp, fichero, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
	}

	
	
	@Override
	public void crearAlerta(String descripcion, Categoria categoria, AlertaStrategy strategy, double limite, String idCUenta) {
		Alerta alerta = new Alerta(descripcion, categoria, strategy, limite, idCUenta);
		add(alerta);
	}
	  
	
    @Override
    public void add(Alerta alerta) {
        if (!listaAlertas.contains(alerta)) {
            listaAlertas.add(alerta);
        }
    }
    
    
    @Override
    public void remove(Alerta alerta) {
        listaAlertas.remove(alerta);
    }
    
    @Override
    public List<Alerta> getAlertas() {
        return Collections.unmodifiableList(listaAlertas);
    }
    
    @Override
    public List<Alerta> getAlertasActivas() {
        return listaAlertas.stream()
            .filter(Alerta::isActiva)
            .collect(Collectors.toList());
    }
    
    @Override
    public void setAlertas(List<Alerta> nuevasAlertas) {
        listaAlertas.clear();
        if (nuevasAlertas != null) {
            listaAlertas.addAll(nuevasAlertas);
        }
    }
    
    
    @Override
	public List<Alerta> findByFilter(Filtro<Alerta> filter) {
		return this.listaAlertas.stream()
				.filter(filter)
				.sorted(Comparator.comparing(Alerta::getLimite).reversed())
				.collect(Collectors.toList());
	}

    @Override
    public void activarAlerta(String id) {
        getById(id).ifPresent(Alerta::activarAlarma);
    }

    @Override
    public void desactivarAlerta(String id) {
        getById(id).ifPresent(Alerta::desactivarAlarma);
    }


	@Override
	public Optional<Alerta> getById(String id) {
		return listaAlertas.stream().filter(x -> x.getId().equals(id)).findFirst();
	}

	@Override
	public List<Alerta> getAlertasPorCuenta(String cuentaActual) {
		return this.listaAlertas.stream()
				.filter(a -> a.getIdCuenta().equals(cuentaActual))
				.collect(Collectors.toList());
	}

	//@Override
	//	public void update(Alerta alertaActualizada) {
	//	getById(alertaActualizada.getId()).ifPresent(n -> n.se)
	//}
	
    
    
    
}
