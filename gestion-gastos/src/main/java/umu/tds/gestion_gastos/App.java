package umu.tds.gestion_gastos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.vista.categoria.ControladorCategoriasView;
import umu.tds.gestion_gastos.vista.categoria.FormularioCategoriaController;
import umu.tds.gestion_gastos.vista.cuenta.FormularioCuentaController;
import umu.tds.gestion_gastos.vista.gasto.*;
//import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.vista.inicio.VentanaInicioController;
import umu.tds.gestion_gastos.vista.notificacion.ControladorVentanaNotificacion;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Inicializar Configuración
        Configuracion.setInstancia(new ConfiguracionImpl());
    	ControladorApp appController = Configuracion.getInstancia().getControladorApp();

    	// Cargar los datos desde JSON
    	try {
    	    appController.cargarDatos();
    	} catch (IOException e) {
    	    e.printStackTrace();
    	}
        
        
        // Cargar vista
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
            		"/umu/tds/gestion_gastos/inicio/VentanaInicio.fxml"
            )
        );

        Parent root = loader.load();

        // Inyectar controlador de aplicación
        VentanaInicioController viewController = loader.getController();
        viewController.setControlador(
            Configuracion.getInstancia().getControladorApp()
        );

        stage.setScene(new Scene(root, 1200, 700));
        stage.setTitle("Gestión de Gastos");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

