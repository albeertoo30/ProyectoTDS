package umu.tds.gestion_gastos.vista.inicio;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.controlador.ControladorApp;
import umu.tds.gestion_gastos.scene_manager.SceneManager;
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
    
    @FXML
    private void onImportar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de gastos");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos CSV", "*.csv")
        );

        Stage stage = (Stage) btnCuentaPersonal.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            // delegamos ene l controlador
            controlador.importarGastos(file);
            
            new Alert(Alert.AlertType.INFORMATION, "Proceso de importación finalizado. Revisa la consola o los gastos.", ButtonType.OK).showAndWait();
        }
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
