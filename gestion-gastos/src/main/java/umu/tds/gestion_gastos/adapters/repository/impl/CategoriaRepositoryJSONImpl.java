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

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;

public class CategoriaRepositoryJSONImpl implements CategoriaRepository {

    private final ObjectMapper mapper;
    private List<Categoria> categorias;
    private final File fichero;

    public CategoriaRepositoryJSONImpl(String rutaFichero) {
        this.mapper = new ObjectMapper();
        
        // Inicializar el fichero desde resources o crear uno nuevo
        this.fichero = inicializarFichero(rutaFichero);
        
        cargarDatos();
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
            
            File archivoLocal = new File(dataDir, "categorias.json");
            
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
        categorias.remove(categoria);
        guardarDatos();
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

    // Persistencia JSON

    private void cargarDatos() {
        try {
            if (fichero.length() == 0) {
                categorias = new ArrayList<>();
                return;
            }
            categorias = mapper.readValue(
                fichero,
                new TypeReference<List<Categoria>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Error cargando categorías", e);
        }
    }

    private void guardarDatos() {
        try {
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(fichero, categorias);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando categorías", e);
        }
    }
}