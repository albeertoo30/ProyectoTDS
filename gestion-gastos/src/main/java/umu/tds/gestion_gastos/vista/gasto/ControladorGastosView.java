package umu.tds.gestion_gastos.vista.gasto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

    @FXML private Button btnFiltrar;
    @FXML private Button btnLimpiarFiltros;
    @FXML private Button btnAñadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    
    private final ObservableList<Gasto> gastosObservable = FXCollections.observableArrayList();
    private ControladorApp controlador;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        
        // Cargar categorías en el filtro
        filtroCategoria.getItems().setAll(controlador.obtenerCategorias());
        filtroCategoria.setPromptText("Todas");
        
        refrescarTabla();
    }

    @FXML
    public void initialize() { 	
    	tablaGastos.setItems(gastosObservable);
   
        colFecha.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFecha().toString())
        );

        colCategoria.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                cellData.getValue().getCategoria() != null ?
                cellData.getValue().getCategoria().getNombre() : ""
            )
        );

        colCantidad.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getCantidad())
        );

        colDescripcion.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion())
        );
    }

    private void refrescarTabla() {
        if (controlador != null) {
            gastosObservable.setAll(controlador.obtenerGastos());
        }
    }

    // BOTÓN AÑADIR
    @FXML
    private void onNuevoGasto() throws IOException {
        abrirFormulario(null);
    }

    // BOTÓN EDITAR
    @FXML
    private void onEditarGasto() throws IOException {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un gasto para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    // BOTÓN ELIMINAR
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

    // BOTON FILTRAR
    @FXML
    private void onAplicarFiltros() {
        try {
            // Recoger valores de la vista
            LocalDate fecha = filtroFecha.getValue();
            Categoria categoria = filtroCategoria.getValue();
            String minStr = filtroMin.getText().trim();
            String maxStr = filtroMax.getText().trim();
            
            // Convertir a Double (null si vacío)
            Double min = minStr.isEmpty() ? null : Double.parseDouble(minStr);
            Double max = maxStr.isEmpty() ? null : Double.parseDouble(maxStr);
            
            // Delegar en ControladorApp
            List<Gasto> gastosFiltrados = controlador.obtenerGastosFiltrados(fecha, categoria, min, max);
            
            // Actualizar vista
            gastosObservable.setAll(gastosFiltrados);
            
        } catch (NumberFormatException e) {
            mostrarError("Los valores de importe deben ser números válidos.");
        }
    }
    
    // BOTÓN LIMPIAR FILTROS
    @FXML
    private void onLimpiarFiltros() {
        filtroFecha.setValue(null);
        filtroCategoria.setValue(null);
        filtroMin.clear();
        filtroMax.clear();
        refrescarTabla();
    }
    
    // ABRIR FORMULARIO
    private void abrirFormulario(Gasto gastoAEditar) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/umu/tds/gestion_gastos/gasto/FormularioGasto.fxml"));

        Parent root = loader.load();

        FormularioGastoController controladorForm = loader.getController();
        
        // IMPORTANTE: Primero inyectar el controlador (esto carga las categorías)
        controladorForm.setControlador(controlador);
        
        // Luego configurar el gasto (si existe)
        if (gastoAEditar != null) {
            controladorForm.setGasto(gastoAEditar);
        }
        
        // Finalmente configurar el callback
        controladorForm.setOnSave(this::refrescarTabla);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(gastoAEditar == null ? "Nuevo gasto" : "Editar gasto");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // UTILS 
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
