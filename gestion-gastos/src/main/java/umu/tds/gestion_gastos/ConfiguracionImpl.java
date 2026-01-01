package umu.tds.gestion_gastos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import umu.tds.gestion_gastos.adapters.repository.impl.CategoriaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.CuentaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.GastoRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.UsuarioRepositoryJSONImpl;
import umu.tds.gestion_gastos.alerta.AlertManager;
import umu.tds.gestion_gastos.alerta.AlertaRepository;
import umu.tds.gestion_gastos.alerta.IAlertManager;
import umu.tds.gestion_gastos.alerta.IAlertaRepository;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.notificacion.INotificacionRepository;
import umu.tds.gestion_gastos.notificacion.NotificacionRepository;
import umu.tds.gestion_gastos.usuario.UsuarioRepository;

public class ConfiguracionImpl extends Configuracion {

    private final ControladorApp controlador;
    private final String NombreApp ="gestion-gastos";
    private final String NameAlertasJSON = "alertas.json";
    private final String NameNotificacionesJSON = "notificaciones.json";
    
    
    public ConfiguracionImpl() {
        // Crear repositorios con las rutas correctas
        GastoRepository gastoRepo = new GastoRepositoryJSONImpl(getRutaGastos());
        CategoriaRepository categoriaRepo = new CategoriaRepositoryJSONImpl(getRutaCategorias());
        CuentaRepository cuentaRepo = new CuentaRepositoryJSONImpl(getRutaCuentas());
        UsuarioRepository usuarioRepo = new UsuarioRepositoryJSONImpl(getRutaUsuarios());

        IAlertaRepository alertaRepo = AlertaRepository.INSTANCE;
        INotificacionRepository notiRepo = NotificacionRepository.INSTANCE;
        
        IAlertManager alertManager = new AlertManager(gastoRepo, alertaRepo, notiRepo);
        gastoRepo.addListener(alertManager);

        // Crear controlador con los repositorios
        this.controlador = new ControladorApp(gastoRepo, categoriaRepo,
        					notiRepo, alertaRepo,  alertManager, cuentaRepo, usuarioRepo);
    
    
    
    
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

    @Override
    public String getRutaGastos() {
        return "/umu/tds/data/gastos.json";
    }

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
    

    @Override
    public String getRutaAlertas() {
        return System.getProperty("user.home")
               + File.separator
               + NombreApp
               + File.separator
               + NameAlertasJSON;
    }
    
    
    @Override 
    public String getRutaNotificaciones() {
    	return System.getProperty("user.home")
                + File.separator
                + NombreApp
                + File.separator
                + NameNotificacionesJSON;
    }
    
    
}
