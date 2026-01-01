package umu.tds.gestion_gastos;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.vista.cuenta.FormularioCuentaController;
import umu.tds.gestion_gastos.vista.cuenta.ListaCuentasViewController;
import javafx.application.Application;
public class TestVentanasLuis extends Application{
	
	@Override
    public void start(Stage stage) throws Exception {

        // Inicializar Configuración
        Configuracion.setInstancia(new ConfiguracionImpl());

        // Cargar vista
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/umu/tds/gestion_gastos/cuentas/ListaCuentasView.fxml"
            )
        );

        Parent root = loader.load();

        // Inyectar controlador de aplicación
        ListaCuentasViewController viewController = loader.getController();
        viewController.setControlador(
            Configuracion.getInstancia().getControladorApp()
        );

        stage.setScene(new Scene(root, 1200, 700));
        stage.setTitle("Gestión de Cuenta");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
