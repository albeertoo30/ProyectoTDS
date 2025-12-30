package umu.tds.gestion_gastos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.vista.categoria.ControladorCategoriasView;
import umu.tds.gestion_gastos.vista.categoria.FormularioCategoriaController;
import umu.tds.gestion_gastos.vista.cuenta.FormularioCuentaController;
import umu.tds.gestion_gastos.vista.gasto.*;
//import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // Inicializar Configuración
        Configuracion.setInstancia(new ConfiguracionImpl());

        // Cargar vista
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
            		"/umu/tds/gestion_gastos/gasto/GastosView.fxml"
            )
        );

        Parent root = loader.load();

        // Inyectar controlador de aplicación
        ControladorGastosView viewController = loader.getController();
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

