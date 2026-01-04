package umu.tds.gestion_gastos.negocio.controladores;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.adapters.repository.impl.AlertaRepositoryJSONImpl;
import umu.tds.gestion_gastos.alerta.AlertManager;
import umu.tds.gestion_gastos.alerta.Alerta;
import umu.tds.gestion_gastos.alerta.AlertaStrategy;
import umu.tds.gestion_gastos.alerta.IAlertManager;
import umu.tds.gestion_gastos.alerta.IAlertaRepository;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.filtros.Filtro;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.gasto.GestorGastos;
import umu.tds.gestion_gastos.importacion.IImportadorGastos;
import umu.tds.gestion_gastos.importacion.ImportadorCSVAdapter;
import umu.tds.gestion_gastos.notificacion.INotificacionRepository;
import umu.tds.gestion_gastos.notificacion.Notificacion;
import umu.tds.gestion_gastos.scene_manager.SceneManager;
import umu.tds.gestion_gastos.usuario.GestorUsuarios;
import umu.tds.gestion_gastos.usuario.Usuario;
import umu.tds.gestion_gastos.usuario.UsuarioRepository;

public class ControladorApp { //Yo le crearia una interfaz 

    private final GestorGastos gestorGastos;
    private final GestorCategorias gestorCategorias;
    private final INotificacionRepository repoNotificaciones;
    private final IAlertaRepository repoAlertas;
    private final IAlertManager gestorAlertas;
    private final GestorCuenta gestorCuentas;
    private final GestorUsuarios gestorUsuarios;
    private final SceneManager sceneManager;
    
    private final CuentaRepository repoCuentas;
    
    private Usuario usuarioActual;
    
    public ControladorApp(CuentaRepository cuentaRepo, CategoriaRepository categoriaRepo, // GastoRepository YA NO SE USA
    		INotificacionRepository notificacionRepo, IAlertaRepository alertaRepo,
    		IAlertManager gestorAlertas, UsuarioRepository usuarioRepo, SceneManager sceneManager) {
        
        // Guardamos la referencia al repositorio de cuentas
        this.repoCuentas = cuentaRepo;

    	this.gestorGastos = new GestorGastos(cuentaRepo);
        this.gestorCuentas = new GestorCuenta(cuentaRepo);
        
        this.gestorCategorias = new GestorCategorias(categoriaRepo);
        this.repoNotificaciones = notificacionRepo;
        this.repoAlertas = alertaRepo;
        this.gestorAlertas = gestorAlertas;
        this.gestorUsuarios = new GestorUsuarios(usuarioRepo);
        this.sceneManager = sceneManager;
        
        this.inicializarSesion();
    }
    
    //SceneManager
    public SceneManager getSceneManager() {
    	return this.sceneManager;
    }
    
    //importacion de datos
    
    public void importarGastos(File fichero) {
        if (fichero == null || !fichero.exists()) {
            System.err.println("Error: El fichero proporcionado no es válido.");
            return;
        }

        try {
            IImportadorGastos importador = new ImportadorCSVAdapter(
                this.gestorCategorias, 
                this.gestorCuentas, 
                this.gestorUsuarios, 
                this.usuarioActual
            );

            List<Gasto> gastosImportados = importador.importarGastos(fichero);
            int gastosGuardados = 0;

            for (Gasto g : gastosImportados) {
                boolean exito = gestorGastos.crearGasto(
                    g.getFecha(), 
                    g.getCantidad(), 
                    g.getDescripcion(), 
                    g.getCategoria(), 
                    g.getUsuario(),
                    g.getCuenta() 
                );
                
                if (exito) gastosGuardados++;
            }
            
            System.out.println("Importación finalizada. Gastos guardados: " + gastosGuardados);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error crítico en la importación: " + e.getMessage());
        }
    }
    
    
    //Operaciones de persistencia de datos van en la configuracion o en el controlador? 
    public void cargarDatos() throws IOException {
    	String rutaAbsolutaCuentas = Configuracion.getInstancia().getRutaCuentas();
        String rutaAbsolutaCate = Configuracion.getInstancia().getRutaCategorias();      		
        repoCuentas.cargar(rutaAbsolutaCuentas);
        gestorCategorias.cargar(rutaAbsolutaCate);
        
        repoAlertas.cargar(Configuracion.getInstancia().getRutaAlertas());
        repoNotificaciones.cargar(Configuracion.getInstancia().getRutaNotificaciones());
    }

    public void guardarDatos() throws IOException {
        String rutaAbsolutaCuentas = Configuracion.getInstancia().getRutaCuentas();
        String rutaAbsolutaCate = Configuracion.getInstancia().getRutaCategorias();      		
        
        // --- CAMBIO: Guardar Cuentas en lugar de Gastos ---
        repoCuentas.guardar(rutaAbsolutaCuentas);
        gestorCategorias.guardar(rutaAbsolutaCate);
        
        repoAlertas.guardar(Configuracion.getInstancia().getRutaAlertas());
        repoNotificaciones.guardar(Configuracion.getInstancia().getRutaNotificaciones());
    
    }

    public void cargarNotificaciones() throws IOException {
        repoNotificaciones.cargar(Configuracion.getInstancia().getRutaNotificaciones());
    }

    //Lo usamos con activar/desactvar/eliminar porque seria absurdo guardar todo.
    public void cargarAlertas() throws IOException {
        repoAlertas.cargar(Configuracion.getInstancia().getRutaAlertas());
    }
    
    
    //Lo usamos con marcar leida porque seria absurdo guardar todo.
    public void guardarNotificaciones() throws IOException {
        repoNotificaciones.guardar(Configuracion.getInstancia().getRutaNotificaciones());
    }

    //Lo usamos con activar/desactvar/eliminar porque seria absurdo guardar todo.
    public void guardarAlertas() throws IOException {
        repoAlertas.guardar(Configuracion.getInstancia().getRutaAlertas());
    }
    
    private void inicializarSesion() {
    	this.usuarioActual = this.gestorUsuarios.inicializarSesion();
    }

    // Operaciones con gastos
    public List<Gasto> obtenerGastos() {
        return gestorGastos.obtenerTodos();
    }

    public void crearGasto(LocalDate fecha, double cantidad, String descripcion, Categoria categoria, Usuario pagador, Cuenta cuenta) {
    	gestorGastos.crearGasto(fecha, cantidad, descripcion, categoria, pagador, cuenta);
    }

    public void actualizarGasto(Gasto gastoExistente, LocalDate fecha, double cantidad, String descripcion, Categoria categoria, Usuario pagador, Cuenta cuenta) {
    	gestorGastos.modificarGasto(gastoExistente, fecha, cantidad, descripcion, categoria, pagador, cuenta);
    }
    
    public void eliminarGasto(int id) {
        gestorGastos.eliminarGasto(id);
    }

    public Gasto obtenerGastoPorId(int id) {
        return gestorGastos.obtenerPorId(id);
    }
    
    // Operaciones con categorias
    public List<Categoria> obtenerCategorias() {
        return gestorCategorias.getCategorias();
    }
    
    public void crearCategoria(String nombre, String descripcion) {
        gestorCategorias.crearCategoria(nombre, descripcion);
    }

    public void editarCategoria(Categoria categoria) {
        gestorCategorias.editarCategoria(categoria);
    }
    
    public boolean categoriaTieneGastos(String nombreCategoria) {
        return gestorGastos.obtenerTodos().stream()
                .anyMatch(g ->
                    g.getCategoria() != null &&
                    g.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria)
                );
    }
    
    public void eliminarCategoria(String nombre) {
        if (categoriaTieneGastos(nombre)) {
            throw new IllegalStateException(
                "No se puede eliminar la categoría porque tiene gastos asociados."
            );
        }
        gestorCategorias.eliminarCategoria(nombre);
    }
       
    public List<Gasto> obtenerGastosFiltrados(LocalDate fecha, Categoria categoria, Double min, Double max) {
        return gestorGastos.obtenerGastosFiltrados(fecha, categoria, min, max);
    }
    
    //OPERACIONES CON ALERTAS: 
    
    public List<Alerta> getAlertas() {
    	return repoAlertas.getAlertas();
    }
    
    public List<Alerta> getAlertasPorCuenta() {
    	return repoAlertas.getAlertasPorCuenta(Configuracion.getInstancia().getCuentaActual());
    }
    
    public List<Alerta> getAlertasActivas() {
    	return repoAlertas.getAlertasActivas();
    }
 
	public void crearAlerta(String descripcion, Categoria categoria, AlertaStrategy strategy, double limite, String idCuenta) {
		repoAlertas.crearAlerta(descripcion, categoria, strategy, limite, idCuenta);
	}
 
    public List<Alerta> filtrarAlertas(Filtro<Alerta> filter){
    	return repoAlertas.findByFilter(filter);
    }
    
    public void activarAlerta(String id) {
    	repoAlertas.activarAlerta(id);
    }
    
    public void desactivarAlerta(String id) {
    	repoAlertas.desactivarAlerta(id);
    }
	
    public void eliminarAlerta(Alerta a) {
    	repoAlertas.remove(a);
    }
	
    
	
	//OPERACIONES CON NOTIFICACIONES
       
    public List<Notificacion> getNotificaciones() {
    	return repoNotificaciones.getNotificaciones();
    }
    
    public List<Notificacion> getNotificacionesPorCuenta(){
    	return repoNotificaciones.getNotificacionesPorCuenta(Configuracion.getInstancia().getCuentaActual());
    }
    
    public List<Notificacion> getNotificacionesOrdenadasDesFecha(){
    	return repoNotificaciones.getAllOrderedByDateDesc();
    }
    
    public List<Notificacion> filtrarNotificaciones(Filtro<Notificacion> filter){
    	return repoNotificaciones.findByFilter(filter);
    }

    public void marcarNotificacionLeida(String id) {
    	repoNotificaciones.marcarLeida(id);
    }

    public void eliminarNotificacion(String id) {
    	repoNotificaciones.delete(id);
    }

    public void limpiarHistorialNotificaciones() {
    	repoNotificaciones.limpiarHistorial();
    }
    
    //Cuentas
    
    public void registrarCuenta(Cuenta c) {
    	gestorCuentas.registrarCuenta(c);
    }
    
    public List<Cuenta> obtenerTodasLasCuentas() {
    	return this.gestorCuentas.getAll();
    }
    
    // Auxiliar para FormularioGasto
    public List<Cuenta> obtenerCuentas() {
        return obtenerTodasLasCuentas(); // Alias para consistencia
    }
    
    public List<Cuenta> obtenerCuentasCompartidas() {
    	return this.gestorCuentas.getAll().stream()
                .filter(c -> c instanceof CuentaCompartida)
                .collect(Collectors.toList());
    }
    
    public void eliminarCuenta(int id) {
    	this.gestorCuentas.eliminarCuenta(id);
    }
    
    public Map<Usuario, Double> calcularSaldos(int idCuenta) {
        return this.gestorCuentas.calcularSaldos(idCuenta);
    }
    
    //Usuarios
    
    public Usuario getUsuarioActual() {
    	return this.usuarioActual;
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        return gestorUsuarios.obtenerOtrosUsuarios(usuarioActual.getId());
    }
    
}
