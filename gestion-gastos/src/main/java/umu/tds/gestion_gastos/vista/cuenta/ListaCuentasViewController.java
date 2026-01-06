package umu.tds.gestion_gastos.vista.cuenta;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class ListaCuentasViewController {

    @FXML private TableView<CuentaCompartida> tablaCuentas;
    @FXML private TableColumn<CuentaCompartida, String> colNombre;
    @FXML private TableColumn<CuentaCompartida, String> colMiembros;
    @FXML private TableColumn<CuentaCompartida, String> colInfo;
    
    @FXML private Button btnCrear;
    @FXML private Button btnEntrar;
    @FXML private Button btnEliminar;

    private ControladorApp controlador;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCuentas();
    }

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().getNombre()));

        colMiembros.setCellValueFactory(cellData -> {
            int num = cellData.getValue().getMiembros().size();
            return new SimpleStringProperty(num + " Personas");
        });
        
        colInfo.setCellValueFactory(cellData -> 
            new SimpleStringProperty("Compartida"));
        
        tablaCuentas.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tablaCuentas.getSelectionModel().getSelectedItem() != null) {
                onEntrarCuenta();
            }
        });
    }

    private void cargarCuentas() {
        if (controlador == null) return;
        
        List<CuentaCompartida> compartidas = controlador.obtenerTodasLasCuentas().stream()
                .filter(c -> c instanceof CuentaCompartida)
                .map(c -> (CuentaCompartida) c)
                .collect(Collectors.toList());

        tablaCuentas.getItems().setAll(compartidas);
    }

    @FXML
    private void onNuevaCuenta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/cuentas/FormularioCuenta.fxml"));
            Parent root = loader.load();

            FormularioCuentaController formController = loader.getController();
            formController.setControlador(controlador);
            
            // Callback ----> Cuando se guarde, recargamos la tabla
            formController.setOnSave(() -> cargarCuentas());

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nueva Cuenta Compartida");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir el formulario: " + e.getMessage());
        }
    }

    @FXML
    private void onEntrarCuenta() {
        CuentaCompartida seleccionada = tablaCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una cuenta primero.");
            return;
        }
        
        try {
            // cargamos vista detalle
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/cuentas/DetalleCuentaView.fxml"));
            Parent root = loader.load();

            DetalleCuentaController detalleController = loader.getController();
            detalleController.setControlador(controlador);
            detalleController.setCuenta(seleccionada); // <--- PASAMOS LA CUENTA SELECCIONADA

            Stage stage = new Stage();
            stage.setTitle("Detalle: " + seleccionada.getNombre());
            stage.setScene(new Scene(root, 1200, 700)); // Hacemos la ventana grande
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir detalle: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarCuenta() {
        CuentaCompartida seleccionada = tablaCuentas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Selecciona una cuenta para eliminar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Cuenta");
        alert.setHeaderText("¿Estás seguro de borrar '" + seleccionada.getNombre() + "'?");
        alert.setContentText("Esta acción es irreversible y borrará todos los gastos asociados.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                controlador.eliminarCuenta(seleccionada.getId());
                //actualizamos la vista
                cargarCuentas(); 

            } catch (Exception e) {
                e.printStackTrace();
                mostrarError("Error al eliminar: " + e.getMessage());
            }
        }
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
    
    private void mostrarInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK).showAndWait();
    }
}