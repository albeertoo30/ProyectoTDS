package umu.tds.gestion_gastos.adapters.repository.impl;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
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

    private static final String RUTA_JSON = "umu/tds/gestion_gastos/data/gastos.json";

    private final ObjectMapper mapper;
    private List<Gasto> gastos;
    private File fichero;
    private int nextId;

    public GastoRepositoryJSONImpl() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        cargarDatos();
        inicializarNextId();
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
            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(RUTA_JSON);
            if (is == null) {
                gastos = new ArrayList<>();
                return;
            }
            gastos = mapper.readValue(is, new TypeReference<List<Gasto>>(){});
        } catch (IOException e) {
            throw new RuntimeException("Error cargando gastos", e);
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
                  .writeValue(fichero, gastos);
        } catch (Exception e) {
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
