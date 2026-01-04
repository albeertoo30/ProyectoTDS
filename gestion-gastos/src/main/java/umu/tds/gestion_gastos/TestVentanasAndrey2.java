package umu.tds.gestion_gastos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.vista.alerta.ControladorVentanaAlerta;
import umu.tds.gestion_gastos.vista.cuenta.FormularioCuentaController;
import umu.tds.gestion_gastos.vista.gasto.*;
//import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.vista.notificacion.ControladorVentanaNotificacion;

public class TestVentanasAndrey2 extends Application {

    @Override
    public void start(Stage stage) throws Exception {

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
    	    getClass().getResource("/umu/tds/gestion_gastos/alerta/VentanaAlertas.fxml")
    	);
    	Parent root = loader.load();

    	// Inyectar controlador de aplicación
    	ControladorVentanaAlerta viewController = loader.getController();
    	viewController.setControlador(appController);

    	stage.setScene(new Scene(root, 1200, 700));
    	stage.setTitle("Gestión de Gastos");
    	
    	stage.setOnCloseRequest(event -> {
    	    try {
    	        appController.guardarAlertas();
    	        System.out.println("Se han guardado correctamente los cambios de alerta");
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	        System.err.println("Hubo un error inesperado al cerrar la ventana, no se han guardado los cambios");
    	    }
    	});
    	
    	stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

