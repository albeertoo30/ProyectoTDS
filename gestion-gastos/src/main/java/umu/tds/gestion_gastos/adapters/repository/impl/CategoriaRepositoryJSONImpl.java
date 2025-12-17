package umu.tds.gestion_gastos.adapters.repository.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;

public class CategoriaRepositoryJSONImpl implements CategoriaRepository {

    private static final String RUTA_JSON = "umu/tds/gestion_gastos/data/categorias.json";

    private final ObjectMapper mapper;
    private List<Categoria> categorias;
    private File fichero;

    public CategoriaRepositoryJSONImpl() {
        this.mapper = new ObjectMapper();
        cargarDatos();
    }

    // Operaciones del repositorio

    @Override
    public void add(Categoria categoria) {
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

    // Persistencia JSON

    private void cargarDatos() {
        try {
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(RUTA_JSON);
            if (is == null) {
                categorias = new ArrayList<>();
                return;
            }
            categorias = mapper.readValue(is, new TypeReference<List<Categoria>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Error cargando categorías", e);
        }
    }

    private void guardarDatos() {
        try {
            if (fichero == null) {
                fichero = new File(getClass()
                            .getClassLoader()
                            .getResource(RUTA_JSON)
                            .getFile()
                );
            }
            mapper.writerWithDefaultPrettyPrinter()
                  .writeValue(fichero, categorias);
        } catch (Exception e) {
            throw new RuntimeException("Error guardando categorías", e);
        }
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
}
