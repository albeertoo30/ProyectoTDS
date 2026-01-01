package umu.tds.gestion_gastos.vista.cuenta;

import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;

public class DetalleCuentaController {

    @FXML private Label lblNombreCuenta;
    @FXML private ListView<Usuario> listaMiembros;
    
    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colConcepto;
    @FXML private TableColumn<Gasto, String> colPagador;
    @FXML private TableColumn<Gasto, String> colImporte;

    private ControladorApp controlador;
    private CuentaCompartida cuentaActual;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
    }

    public void setCuenta(CuentaCompartida cuenta) {
        this.cuentaActual = cuenta;
        actualizarVista();
    }

    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));
        colConcepto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescripcion()));
        colImporte.setCellValueFactory(cell -> new SimpleStringProperty(String.format("%.2f €", cell.getValue().getCantidad())));
        
        // TODO: CAMPO USUARIO O PAGADOR EN GASTO
        colPagador.setCellValueFactory(cell -> new SimpleStringProperty("Pendiente")); 
    }

    private void actualizarVista() {
        if (cuentaActual == null) return;
        
        lblNombreCuenta.setText(cuentaActual.getNombre());
        listaMiembros.getItems().setAll(cuentaActual.getMiembros());
        tablaGastos.getItems().setAll(cuentaActual.getGastos());
    }

    @FXML
    private void onNuevoGasto() {
        System.out.println("Abriendo formulario de gasto para: " + cuentaActual.getNombre());
    }

    @FXML
    private void onEliminarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "¿Borrar gasto?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                actualizarVista();
            }
        });
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) lblNombreCuenta.getScene().getWindow();
        stage.close();
    }
}