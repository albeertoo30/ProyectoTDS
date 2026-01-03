package umu.tds.gestion_gastos.adapters.repository.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.jsontype.NamedType;

import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.cuenta.CuentaIndividual;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;

public class CuentaRepositoryJSONImpl implements CuentaRepository {

    private final ObjectMapper mapper;
    private List<Cuenta> cuentas;
    private final File fichero;


    public CuentaRepositoryJSONImpl(String rutaFichero) {
    	this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);

        this.mapper.registerSubtypes(new NamedType(CuentaIndividual.class, "INDIVIDUAL"));
        this.mapper.registerSubtypes(new NamedType(CuentaCompartida.class, "COMPARTIDA"));


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
            

            File archivoLocal = new File(dataDir, "cuentas.json");
            

            if (!archivoLocal.exists() || archivoLocal.length() <= 2) {

                InputStream inputStream = getClass().getResourceAsStream(rutaFichero);
                
                if (inputStream != null) {

                    Files.copy(inputStream, archivoLocal.toPath(), 
                              StandardCopyOption.REPLACE_EXISTING);
                    inputStream.close();
                } else {

                    if (!archivoLocal.exists()) {
                        archivoLocal.createNewFile();
                    }
                    mapper.writeValue(archivoLocal, new ArrayList<>());
                }
            }
            
            return archivoLocal;
            
        } catch (IOException e) {
            throw new RuntimeException("Error inicializando fichero de cuentas", e);
        }
    }
    

    @Override
    public Cuenta add(Cuenta cuenta) {
        if (cuenta.getId() == 0) {
            int maxId = this.cuentas.stream().mapToInt(Cuenta::getId).max().orElse(0);
            cuenta.setId(maxId + 1);
        }
        //borrar antes de guardarla otra vez para evitar duplicados
        cuentas.removeIf(c -> c.getId() == cuenta.getId());
        this.cuentas.add(cuenta);
        guardarDatos();
        return cuenta;
    }
    
    @Override
    public boolean remove(int id) {
        boolean borrado = this.cuentas.removeIf(c -> c.getId() == id);
        if (borrado) guardarDatos();
        return borrado;
    }
    
    @Override
    public boolean update(int id, String nombre) {
       for(Cuenta c : cuentas) {
           if(c.getId() == id) {
               c.setNombre(nombre);
               guardarDatos();
               return true;
           }
       }
       return false;
    }
    
    @Override
    public List<Cuenta> getAll() {
        return new ArrayList<>(this.cuentas);
    }
    
    @Override
    public Cuenta getById(int id) {
        return this.cuentas.stream().filter(c -> c.getId() == id).findFirst().orElse(null);
    }

    private void cargarDatos() {
        try {
            if (fichero.length() <= 2) {
                cuentas = new ArrayList<>();
                return;
            }
            cuentas = mapper.readValue(fichero, new TypeReference<List<Cuenta>>() {});
        } catch (IOException e) {
            e.printStackTrace();
            cuentas = new ArrayList<>();
        }
    }

    private void guardarDatos() {
    	try {
            mapper.writerFor(new TypeReference<List<Cuenta>>() {})
                  .with(SerializationFeature.INDENT_OUTPUT)
                  .writeValue(fichero, cuentas);
                  
        } catch (IOException e) {
            throw new RuntimeException("Error guardando cuentas", e);
        }
    }
}