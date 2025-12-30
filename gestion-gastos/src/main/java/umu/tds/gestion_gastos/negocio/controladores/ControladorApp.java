package umu.tds.gestion_gastos.negocio.controladores;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.alerta.AlertManager;
import umu.tds.gestion_gastos.alerta.Alerta;
import umu.tds.gestion_gastos.alerta.AlertaRepository;
import umu.tds.gestion_gastos.alerta.AlertaStrategy;
import umu.tds.gestion_gastos.alerta.IAlertManager;
import umu.tds.gestion_gastos.alerta.IAlertaRepository;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.gasto.GestorGastos;
import umu.tds.gestion_gastos.notificacion.INotificacionFilter;
import umu.tds.gestion_gastos.notificacion.INotificacionRepository;
import umu.tds.gestion_gastos.notificacion.Notificacion;
import umu.tds.gestion_gastos.notificacion.NotificacionRepository;
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
    
    private Usuario usuarioActual;
    
    public ControladorApp(GastoRepository gastoRepo, CategoriaRepository categoriaRepo,
    		INotificacionRepository notificacionRepo, IAlertaRepository alertaRepo,
    		IAlertManager gestorAlertas, CuentaRepository cuentaRepo, UsuarioRepository usuarioRepo) {
        
    	this.gestorGastos = new GestorGastos(gastoRepo);
        this.gestorCategorias = new GestorCategorias(categoriaRepo);
        this.repoNotificaciones = notificacionRepo;
        this.repoAlertas = alertaRepo;
        this.gestorAlertas = gestorAlertas;
        this.gestorCuentas = new GestorCuenta(cuentaRepo);
        this.gestorUsuarios = new GestorUsuarios(usuarioRepo);
        
        this.inicializarSesion();
    }
    
    //Operaciones de persistencia de datos van en la configuracion o en el controlador? 
    public void cargarDatos() throws IOException {
        String rutaAbsolutaGastos = Configuracion.getInstancia().getRutaGastos();
        String rutaAbsolutaCate = Configuracion.getInstancia().getRutaCategorias();      		
        //gestorGastos.cargar(rutaAbsolutaGastos);
        //gestorCategorias.cargar(rutaAbsolutaCate);
        
        repoAlertas.cargar(Configuracion.getInstancia().getRutaAlertas());
        repoNotificaciones.cargar(Configuracion.getInstancia().getRutaNotificaciones());
    }

    public void guardarDatos() throws IOException {
        String rutaAbsolutaGastos = Configuracion.getInstancia().getRutaGastos();
        String rutaAbsolutaCate = Configuracion.getInstancia().getRutaCategorias();      		
        gestorGastos.guardar(rutaAbsolutaGastos);
        gestorCategorias.guardar(rutaAbsolutaCate);
        
        repoAlertas.guardar(Configuracion.getInstancia().getRutaAlertas());
        repoNotificaciones.guardar(Configuracion.getInstancia().getRutaNotificaciones());
    
    }


    //Lo usamos con marcar leida porque seria absurdo guardar todo.
    public void guardarNotificaciones() throws IOException {
        repoNotificaciones.guardar(Configuracion.getInstancia().getRutaNotificaciones());
    }

    
    
    private void inicializarSesion() {
    	this.usuarioActual = this.gestorUsuarios.inicializarSesion();
    }

    // Operaciones con gastos
    public List<Gasto> obtenerGastos() {
        return gestorGastos.obtenerTodos();
    }

    public void registrarGasto(Gasto gasto) {
        gestorGastos.registrarGasto(gasto);
    }

    public void editarGasto(Gasto gasto) {
        gestorGastos.editarGasto(gasto);
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
    
    
    
    
    
    
    
    
    //OPERACIONES CON ALERTAS: 
    
    public List<Alerta> getAlertas() {
    	return repoAlertas.getAlertas();
    }
    public List<Alerta> getAlertasActivas() {
    	return repoAlertas.getAlertasActivas();
    }
 
	public void crearAlerta(String descripcion, Categoria categoria, AlertaStrategy strategy, double limite) {
		repoAlertas.crearAlerta(descripcion, categoria, strategy, limite);
	}
 
	
	//OPERACIONES CON NOTIFICACIONES
       
    public List<Notificacion> getNotificaciones() {
    	return repoNotificaciones.getNotificaciones();
    }
    
    public List<Notificacion> getNotificacionesOrdenadasDesFecha(){
    	return repoNotificaciones.getAllOrderedByDateDesc();
    }
    
    public List<Notificacion> filtrarNotificaciones(INotificacionFilter filter){
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
    
    //Usuarios
    
    public Usuario getUsuarioActual() {
    	return this.usuarioActual;
    }
    
    public List<Usuario> obtenerTodosLosUsuarios() {
        return gestorUsuarios.obtenerOtrosUsuarios(usuarioActual.getId());
    }
    

    
    
}
