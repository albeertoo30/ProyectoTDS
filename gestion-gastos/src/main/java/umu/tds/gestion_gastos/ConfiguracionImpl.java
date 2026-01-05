package umu.tds.gestion_gastos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import umu.tds.gestion_gastos.adapters.repository.impl.AlertaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.CategoriaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.CuentaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.GastoRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.NotificacionRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.UsuarioRepositoryJSONImpl;
import umu.tds.gestion_gastos.alerta.AlertManager;
import umu.tds.gestion_gastos.alerta.GastoListener;
import umu.tds.gestion_gastos.alerta.IAlertManager;
import umu.tds.gestion_gastos.alerta.IAlertaRepository;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.notificacion.INotificacionRepository;
import umu.tds.gestion_gastos.scene_manager.SceneManager;
import umu.tds.gestion_gastos.usuario.UsuarioRepository;

public class ConfiguracionImpl extends Configuracion {

    private final ControladorApp controlador;
    private final String NombreApp =".gestion_gastos";
    private final String NameAlertasJSON = "alertas.json";
    private final String NameNotificacionesJSON = "notificaciones.json";
    private final String data = "data";
    private String idCuentaActual;
    
    public ConfiguracionImpl() {
        // Crear repositorios con las rutas correctas
    	CategoriaRepository categoriaRepo = new CategoriaRepositoryJSONImpl(getRutaCategorias());
        CuentaRepository cuentaRepo = new CuentaRepositoryJSONImpl(getRutaCuentas());
        UsuarioRepository usuarioRepo = new UsuarioRepositoryJSONImpl(getRutaUsuarios());

        IAlertaRepository alertaRepo = AlertaRepositoryJSONImpl.INSTANCE;
        INotificacionRepository notiRepo = NotificacionRepositoryJSONImpl.INSTANCE;
        
        
        IAlertManager alertManager = new AlertManager(cuentaRepo, alertaRepo, notiRepo);
        //gastoRepo.addListener(alertManager);

        // Crear controlador con los repositorios
        this.controlador = new ControladorApp(cuentaRepo, categoriaRepo,
        					notiRepo, alertaRepo, alertManager, usuarioRepo, SceneManager.INSTANCE);
    
        this.controlador.addGastoListener((GastoListener) alertManager);
        SceneManager.INSTANCE.init(this.controlador);
        
    }    
    
    //Esto no se si va aqui o en el controlador
    @Override
    public void cargarTodo() throws IOException {
    	this.controlador.cargarDatos();
    }
    @Override
    public void guardarTodo() throws IOException {
    	this.controlador.guardarDatos();
    }
    
    @Override
    public ControladorApp getControladorApp() {
        return controlador;
    }

    /*@Override
    public String getRutaGastos() {
        return "/umu/tds/data/gastos.json";
    }*/

    @Override
    public String getRutaCategorias() {
        return "/umu/tds/data/categorias.json";
    }
    
    @Override
    public String getRutaCuentas() {
        return "/umu/tds/data/cuentas.json";
    }
    
    @Override
    public String getRutaUsuarios() {
        return "/umu/tds/data/usuarios.json";
    }
    

    //Se puede factorizar en un unico metodo al que solo 
    //le pasamos el nombre del json porque es igual pero por 
    //no modificar ahora lo dejamos asi.
    @Override
    public String getRutaAlertas() {
        return System.getProperty("user.home")
               + File.separator
               + NombreApp
               + File.separator
               + data
               + File.separator
               + NameAlertasJSON;
    }
    
    
    @Override 
    public String getRutaNotificaciones() {
    	return System.getProperty("user.home")
                + File.separator
                + NombreApp
                + File.separator
                + data
                + File.separator
                + NameNotificacionesJSON;
    }
    
    
    //Para el filtrado por cuentas
    
    @Override 
    public String getCuentaActual() {
    	return this.idCuentaActual;
    }
    
    @Override 
    public void setCuentaActual(String id) {
    	this.idCuentaActual = id;
    }

}
