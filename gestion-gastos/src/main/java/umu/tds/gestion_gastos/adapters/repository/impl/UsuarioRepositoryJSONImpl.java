package umu.tds.gestion_gastos.adapters.repository.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import umu.tds.gestion_gastos.usuario.Usuario;
import umu.tds.gestion_gastos.usuario.UsuarioRepository;

public class UsuarioRepositoryJSONImpl implements UsuarioRepository {

    private final ObjectMapper mapper;
    private final File fichero;
    private List<Usuario> usuarios;

    public UsuarioRepositoryJSONImpl(String rutaFichero) {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        String userHome = System.getProperty("user.home");
        File dataDir = new File(userHome, ".gestion_gastos/data");
        if (!dataDir.exists()) dataDir.mkdirs();
        this.fichero = new File(dataDir, "usuarios.json");
        
        cargarDatos();
    }

    private void cargarDatos() {
        if (!fichero.exists() || fichero.length() == 0) {
            usuarios = new ArrayList<>();
            return;
        }
        try {
            usuarios = mapper.readValue(fichero, new TypeReference<List<Usuario>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            usuarios = new ArrayList<>();
        }
    }

    private void guardarCambios() {
        try {
            mapper.writeValue(fichero, usuarios);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void guardar(Usuario u) {
        boolean existe = false;
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId() == (u.getId())) {
                usuarios.set(i, u);
                existe = true;
                break;
            }
        }
        if (!existe) {
            usuarios.add(u);
        }
        guardarCambios();
    }

    @Override
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public Usuario obtenerPorId(int id) {
        return usuarios.stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElse(null);
    }
}