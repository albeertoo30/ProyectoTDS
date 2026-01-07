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

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;

public class CategoriaRepositoryJSONImpl implements CategoriaRepository {

    private final ObjectMapper mapper;
    private List<Categoria> categorias;
    private final File fichero;

    public CategoriaRepositoryJSONImpl(String rutaFichero) {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        // Inicializar el fichero desde resources o crear uno nuevo
        this.fichero = inicializarFichero(rutaFichero);
        
        cargarDatos();
    }

    private File inicializarFichero(String rutaFichero) {
        try {
            String userHome = System.getProperty("user.home");
            File dataDir = new File(userHome, ".gestion_gastos/data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            
            File archivoLocal = new File(dataDir, "categorias.json");
            
            // Si no existe o está vacío (≤2 bytes = "[]"), copiar desde resources
            if (!archivoLocal.exists() || archivoLocal.length() <= 2) {
                InputStream inputStream = getClass().getResourceAsStream(rutaFichero);
                
                if (inputStream != null) {
                    Files.copy(inputStream, archivoLocal.toPath(), 
                              StandardCopyOption.REPLACE_EXISTING);
                    inputStream.close();
                } else {
                	if(!archivoLocal.exists()) {
                		archivoLocal.createNewFile();
                	}
                    mapper.writeValue(archivoLocal, new ArrayList<>());
                }
            }
            
            return archivoLocal;
            
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando fichero de categorías", e);
        }
    }
    
    // Operaciones del repositorio

    @Override
    public void add(Categoria categoria) {
        if (findByName(categoria.getNombre()).isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe una categoría con nombre " + categoria.getNombre()
            );
        }
        categorias.add(categoria);
        guardarDatos();
    }

    @Override
    public void remove(Categoria categoria) {
        boolean borrado = this.categorias.removeIf(c -> c.getNombre().equalsIgnoreCase(categoria.getNombre()));
        if (borrado) guardarDatos();
    }
    
    @Override
    public void update(Categoria categoria) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getNombre().equalsIgnoreCase(categoria.getNombre())) {
                categorias.set(i, categoria);
                guardarDatos();
                return;
            }
        }
        throw new IllegalArgumentException(
            "No existe una categoría con nombre " + categoria.getNombre()
        );
    }

    @Override
    public List<Categoria> getAll() {
        return new ArrayList<>(categorias);
    }

    @Override
    public Optional<Categoria> findByName(String nombre) {
        return categorias.stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
    }

   
    // Persistencia JSON

    private void cargarDatos() {
        try {
            if (fichero.length() <= 2) {
                categorias = new ArrayList<>();
                return;
            }
            categorias = mapper.readValue(fichero, new TypeReference<List<Categoria>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            categorias = new ArrayList<Categoria>();
        }
    }

    private void guardarDatos() {
        try {
            mapper.writerFor(new TypeReference<List<Categoria>>() {})
                  .with(SerializationFeature.INDENT_OUTPUT)
                  .writeValue(fichero, categorias);
                  
        } catch (IOException e) {
            throw new RuntimeException("Error guardando categorías", e);
        }
    }

	@Override
	public void cargar(String ruta) throws IOException {
		cargarDatos();
	}

	@Override
	public void guardar(String ruta) throws IOException {
		guardarDatos();
	}
}