package umu.tds.gestion_gastos.importacion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.cuenta.CuentaIndividual;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;
import umu.tds.gestion_gastos.usuario.GestorUsuarios;

public class ImportadorCSVAdapter implements IImportadorGastos {

    private final GestorCategorias gestorCategorias;
    private final GestorCuenta gestorCuentas;
    private final GestorUsuarios gestorUsuarios; 
    private final Usuario usuarioActual;
    
    //Memoria para controlar que no creemos los usuarios mas de 1 vez si se importa varias veces
    private final Set<String> cuentasCreadasEnSesion = new HashSet<>();

    public ImportadorCSVAdapter(GestorCategorias gCat, GestorCuenta gCuen, GestorUsuarios gUsu, Usuario uAct) {
        this.gestorCategorias = gCat;
        this.gestorCuentas = gCuen;
        this.gestorUsuarios = gUsu;
        this.usuarioActual = uAct;
    }

    @Override
    public List<Gasto> importarGastos(File fichero) throws Exception {
        List<Gasto> gastosParaProcesar = new ArrayList<>();
        cuentasCreadasEnSesion.clear();
        
        String linea;
        String separador = ";"; 

        try (BufferedReader br = new BufferedReader(new FileReader(fichero))) {
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty() || linea.toUpperCase().startsWith("FECHA")) continue;

                String[] datos = linea.split(separador);
                if (datos.length < 5) continue;

                // parse del csv
                LocalDate fecha = LocalDate.parse(datos[0], DateTimeFormatter.ISO_LOCAL_DATE);
                double cantidad = Double.parseDouble(datos[1]);
                String descripcion = datos[2];
                String nombreCategoria = datos[3];
                String nombreCuenta = datos[4].trim();
                boolean esCompartida = datos.length > 5 && Boolean.parseBoolean(datos[5]);
                String nombrePagador = (datos.length > 6) ? datos[6].trim() : "";

                // resolvemos categoria
                Categoria categoria = resolverCategoria(nombreCategoria);

                // Cuenta
                Cuenta cuenta = resolverCuenta(nombreCuenta, esCompartida);
                
                // Determinamos si es nueva consultando la memoria de sesión
                boolean esCuentaNueva = cuentasCreadasEnSesion.contains(nombreCuenta.toUpperCase());

                // Usuario
                Usuario pagador = resolverUsuario(nombrePagador, cuenta, esCuentaNueva);

                // Crear Gasto
                Gasto nuevoGasto = new Gasto(0, fecha, cantidad, descripcion, categoria);
                nuevoGasto.setUsuario(pagador);
                nuevoGasto.setCuenta(cuenta);

                gastosParaProcesar.add(nuevoGasto);
            }
        } catch (Exception e) {
            throw new Exception("Error procesando CSV: " + e.getMessage());
        }
        
        return gastosParaProcesar;
    }

    //auxiliares que resuelven cada uno de los campos importantes Cuenta--Categoria--Usuario

    private Cuenta resolverCuenta(String nombreCSV, boolean esCompartida) {
        if (esCompartida) {
            if (cuentasCreadasEnSesion.contains(nombreCSV.toUpperCase())) {
                return gestorCuentas.getAll().stream()
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCSV))
                    .findFirst().orElse(null);
            }
            
            Optional<Cuenta> match = gestorCuentas.getAll().stream()
                    .filter(c -> c instanceof CuentaCompartida)
                    .filter(c -> c.getNombre().equalsIgnoreCase(nombreCSV))
                    .findFirst();

            if (match.isPresent()) {
                return match.get(); // EXISTE DE VERDAD
            } else {
                // si no existia la cuenta la creamos
                System.out.println("Creando nueva cuenta compartida importada: " + nombreCSV);
                List<Usuario> miembros = new ArrayList<>();
                miembros.add(usuarioActual); 
                CuentaCompartida nueva = new CuentaCompartida(0, nombreCSV, miembros);
                gestorCuentas.registrarCuenta(nueva);
                
                cuentasCreadasEnSesion.add(nombreCSV.toUpperCase());
                
                return nueva; 
            }
        } else {

            Optional<Cuenta> miCuenta = gestorCuentas.getAll().stream()
                    .filter(c -> c instanceof CuentaIndividual)
                    .findFirst();
            
            if (miCuenta.isPresent()) {
                return miCuenta.get();
            } else {
                CuentaIndividual nueva = new CuentaIndividual(0, nombreCSV, usuarioActual);
                gestorCuentas.registrarCuenta(nueva);
                return nueva;
            }
        }
    }

    private Usuario resolverUsuario(String nombrePagador, Cuenta cuenta, boolean esCuentaNueva) {
        // detectamos usuario
        if (nombrePagador == null || 
            nombrePagador.trim().isEmpty() || 
            nombrePagador.equalsIgnoreCase("Yo") ||
            nombrePagador.equalsIgnoreCase(usuarioActual.getNombre())) {
            
            return usuarioActual;
        }

        if (cuenta instanceof CuentaIndividual) {
            return usuarioActual;
        }

        if (cuenta instanceof CuentaCompartida) {
            CuentaCompartida compartida = (CuentaCompartida) cuenta;

            // Buscamos si ya está en la lista de miembros
            Optional<Usuario> miembro = compartida.getMiembros().stream()
                    .filter(u -> u.getNombre().equalsIgnoreCase(nombrePagador))
                    .findFirst();

            if (miembro.isPresent()) {
                return miembro.get();
            }

            if (esCuentaNueva) {
                Usuario usuarioImportado = obtenerOCrearUsuarioGlobal(nombrePagador);
                
                boolean yaExiste = compartida.getMiembros().stream()
                        .anyMatch(u -> u.getId() == usuarioImportado.getId());
                        
                if (!yaExiste) {
                    compartida.getMiembros().add(usuarioImportado);
                    gestorCuentas.registrarCuenta(compartida);
                    System.out.println("Añadido miembro '" + nombrePagador + "' a la cuenta '" + cuenta.getNombre() + "'");
                }
                return usuarioImportado;
            } else {
                System.err.println("ADVERTENCIA: Usuario '" + nombrePagador + "' ignorado en cuenta existente. Se asigna a: " + usuarioActual.getNombre());
                return usuarioActual;
            }
        }

        return usuarioActual;
    }

    private Usuario obtenerOCrearUsuarioGlobal(String nombre) {
        Optional<Usuario> existente = gestorUsuarios.obtenerOtrosUsuarios(usuarioActual.getId()).stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre))
                .findFirst();
        
        if (existente.isPresent()) {
            return existente.get();
        }

        System.out.println("Registrando nuevo usuario en el sistema: " + nombre);
        Usuario nuevo = new Usuario(0, nombre); 
        gestorUsuarios.crearUsuario(nuevo); 
        
        return nuevo;
    }

    private Categoria resolverCategoria(String nombre) {
        return gestorCategorias.getCategorias().stream()
                .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                .findFirst()
                .orElseGet(() -> {
                    gestorCategorias.crearCategoria(nombre, "Importada");
                    return gestorCategorias.getCategorias().stream()
                            .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                            .findFirst().orElse(null);
                });
    }
}