package umu.tds.gestion_gastos.adapters.repository.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;

public class GastoRepositoryJSONImpl implements GastoRepository {

    private final ObjectMapper mapper;
    private List<Gasto> gastos;
    private final File fichero;
    private int nextId;

    public GastoRepositoryJSONImpl(String rutaFichero) {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Inicializar el fichero desde resources o crear uno nuevo
        this.fichero = inicializarFichero(rutaFichero);
        
        cargarDatos();
        inicializarNextId();
    }

    private File inicializarFichero(String rutaFichero) {
        try {
            // Intentar cargar desde resources (lectura)
            InputStream inputStream = getClass().getResourceAsStream(rutaFichero);
            
            // Crear directorio de datos en la ubicación del usuario
            String userHome = System.getProperty("user.home");
            File dataDir = new File(userHome, ".gestion_gastos/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            File archivoLocal = new File(dataDir, "gastos.json");
            
            // Si existe en resources y no existe localmente, copiar
            if (inputStream != null && !archivoLocal.exists()) {
                Files.copy(inputStream, archivoLocal.toPath());
                inputStream.close();
            }
            
            // Si no existe ni en resources ni localmente, crear vacío
            if (!archivoLocal.exists()) {
                archivoLocal.createNewFile();
                // Escribir array vacío
                mapper.writeValue(archivoLocal, new ArrayList<>());
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
    }

    @Override
    public void update(Gasto gasto) {
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == gasto.getId()) {
                gastos.set(i, gasto);
                guardarDatos();
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
}
