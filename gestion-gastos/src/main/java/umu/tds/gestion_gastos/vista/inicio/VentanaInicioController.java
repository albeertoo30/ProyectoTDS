package umu.tds.gestion_gastos.vista.inicio;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.sceneManager.SceneManager;
import umu.tds.gestion_gastos.vista.gasto.ControladorGastosView;

public class VentanaInicioController {

    @FXML private Button btnCuentaPersonal;
    @FXML private Button btnCuentasCompartidas;
    
    private ControladorApp controlador;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
    }

    @FXML
    private void onCuentaPersonal() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                    "/umu/tds/gestion_gastos/gasto/GastosView.fxml"
                )
            );

            Parent root = loader.load();

            ControladorGastosView controladorVista = loader.getController();
            controladorVista.setControlador(controlador);

            Stage stage = (Stage) btnCuentaPersonal.getScene().getWindow();
            stage.setScene(new Scene(root, 1200, 700));
            stage.setTitle("Gestión de Gastos");

        } catch (IOException e) {
            mostrarError("No se pudo cargar la vista de gastos.");
        }
    }


    @FXML
    private void onCuentasCompartidas() throws IOException {
        /*new Alert(
            Alert.AlertType.INFORMATION,
            "Funcionalidad de cuentas compartidas pendiente de implementar.",
            ButtonType.OK
        ).showAndWait();
    	*/
    	
    	controlador.getSceneManager().abrirVentanaCuentasCompartidas();
    
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
