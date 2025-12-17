package umu.tds.gestion_gastos.vista.gasto;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class ControladorGastosView {

    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, Double> colCantidad;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private DatePicker filtroFecha;
    @FXML private ComboBox<Categoria> filtroCategoria;
    @FXML private TextField filtroMin;
    @FXML private TextField filtroMax;

    @FXML private Button btnLimpiarFiltros;
    @FXML private Button btnAñadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    
    private final ObservableList<Gasto> gastosObservable = FXCollections.observableArrayList();

    private ControladorApp controlador;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        refrescarTabla();
    }

    @FXML
    public void initialize() { 	
    	tablaGastos.setItems(gastosObservable);
   
        colFecha.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFecha().toString()
            )
        );

        colCategoria.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getCategoria() != null ?
                cellData.getValue().getCategoria().getNombre() : ""
            )
        );

        colCantidad.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleObjectProperty<>(
                cellData.getValue().getCantidad()
            )
        );

        colDescripcion.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getDescripcion()
            )
        );
    }

    private void refrescarTabla() {
        if (controlador != null) {
            gastosObservable.setAll(controlador.obtenerGastos());
        }
    }

    // ---- BOTÓN AÑADIR ----
    @FXML
    private void onNuevoGasto() throws IOException {
        abrirFormulario(null);
    }

    // ---- BOTÓN EDITAR ----
    @FXML
    private void onEditarGasto() throws IOException {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un gasto para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    // ---- BOTÓN ELIMINAR ----
    @FXML
    private void onEliminarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un gasto para eliminar.");
            return;
        }

        if (confirmar("¿Seguro que desea eliminar este gasto?")) {
            controlador.eliminarGasto(seleccionado.getId());
            refrescarTabla();
        }
    }

    // ---- ABRIR FORMULARIO ----
    private void abrirFormulario(Gasto gastoAEditar) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/umu/tds/gestion_gastos/gasto/FormularioGasto.fxml"));

        Parent root = loader.load();

        FormularioGastoController controladorForm = loader.getController();
        controladorForm.setControlador(controlador);
        controladorForm.setGasto(gastoAEditar);
        controladorForm.setOnSave(this::refrescarTabla);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(gastoAEditar == null ? "Nuevo gasto" : "Editar gasto");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // ---- UTILS ----
    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private boolean confirmar(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg,
                ButtonType.OK, ButtonType.CANCEL)
                .showAndWait()
                .orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
