package umu.tds.gestion_gastos.repository.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.gestion_gastos.alerta.GastoListener;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;

public class GastoRepositoryJSONImpl implements GastoRepository {

    private final ObjectMapper mapper;
    private List<Gasto> gastos;
    private final File fichero;
    private int nextId;
    
    //Agregado para el patron observer
    private List<GastoListener> listeners;

    

    public GastoRepositoryJSONImpl(String rutaFichero) {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Inicializar el fichero desde resources o crear uno nuevo
        this.fichero = inicializarFichero(rutaFichero);
        
        this.listeners = new ArrayList<>();
        
        cargarDatos();
        inicializarNextId();
    }

    private File inicializarFichero(String rutaFichero) {
        try {
            String userHome = System.getProperty("user.home");
            File dataDir = new File(userHome, ".gestion_gastos/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            File archivoLocal = new File(dataDir, "gastos.json");
            
            // Si no existe o está vacío, copiar desde resources
            if (!archivoLocal.exists() || archivoLocal.length() <= 2) {
                InputStream inputStream = getClass().getResourceAsStream(rutaFichero);
                
                if (inputStream != null) {
                    Files.copy(inputStream, archivoLocal.toPath(), 
                              StandardCopyOption.REPLACE_EXISTING);
                    inputStream.close();
                } else {
                    archivoLocal.createNewFile();
                    mapper.writeValue(archivoLocal, new ArrayList<>());
                }
            }
            
            return archivoLocal;
            
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando fichero de gastos", e);
        }
    }

    // Operaciones del repositorio

    @Override
    public void add(Gasto gasto) {
        gasto.setId(nextId++);
        gastos.add(gasto);
        guardarDatos();
        notifyGastoCreado(gasto);
    }

    @Override
    public void update(Gasto gasto) {
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == gasto.getId()) {
                gastos.set(i, gasto);
                guardarDatos();
                notifyGastoActualizado(gasto);
                return;
            }
        }
        throw new IllegalArgumentException(
            "No existe un gasto con id " + gasto.getId()
        );
    }

    @Override
    public void remove(Gasto gasto) {
        gastos.removeIf(g -> g.getId() == gasto.getId());
        guardarDatos();
        notifyGastoEliminado(gasto);
    }

    @Override
    public Optional<Gasto> findById(int id) {
        return gastos.stream()
                .filter(g -> g.getId() == id)
                .findFirst();
    }

    @Override
    public List<Gasto> getAll() {
        return new ArrayList<>(gastos);
    }

    // Persistencia JSON

    private void cargarDatos() {
        try {
            if (fichero.length() == 0) {
                gastos = new ArrayList<>();
                return;
            }
            gastos = mapper.readValue(
                fichero,
                new TypeReference<List<Gasto>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Error cargando gastos", e);
        }
    }

    private void guardarDatos() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(fichero, gastos);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando gastos", e);
        }
    }

    private void inicializarNextId() {
        nextId = gastos.stream()
                .mapToInt(Gasto::getId)
                .max()
                .orElse(0) + 1;
    }

    //OBSERVER MANUAL, nos saltamos lo del changed. Estos metodos no estan en la interfaz. 
	@Override
	public void addListener(GastoListener gastoListener) {
	    if (!listeners.contains(gastoListener)) listeners.add(gastoListener);		
	}

	private void notifyGastoCreado(Gasto g) {
	    for (GastoListener l : listeners) l.onGastoNuevo(g);
	}

	private void notifyGastoActualizado(Gasto g) {
	    for (GastoListener l : listeners) l.onGastoModificado(g);
	}

	private void notifyGastoEliminado(Gasto g) {
	    for (GastoListener l : listeners) l.onGastoEliminado(g);
	}
	
	
	//JSON
	

	@Override
	public void cargar(String ruta) throws IOException {
		cargarDatos();
	}

	@Override
	public void guardar(String ruta) throws IOException {
		guardarDatos();
	}

}
