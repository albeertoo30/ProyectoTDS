package umu.tds.gestion_gastos.vista.categoria;

import java.io.IOException;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class ControladorCategoriasView {

    @FXML private Button btnAñadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;

    @FXML private TableView<Categoria> tablaCategorias;
    @FXML private TableColumn<Categoria, String> colNombre;
    @FXML private TableColumn<Categoria, String> colDescripcion;

    private ControladorApp controlador;
    private ObservableList<Categoria> categoriasObservable;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCategorias();
    }

    @FXML
    public void initialize() {
    	categoriasObservable = FXCollections.observableArrayList();
        tablaCategorias.setItems(categoriasObservable);
    	
        colNombre.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getNombre())
        );
        colDescripcion.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion())
        );
    }

    private void cargarCategorias() {
        if (controlador != null) {
            categoriasObservable.setAll(controlador.obtenerCategorias());
        }
    }

    //BOTONES

    @FXML
    private void onAñadirCategoria() throws IOException {
        abrirFormulario(null);
    }

    @FXML
    private void onEditarCategoria() throws IOException {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Seleccione una categoría para editar.");
            return;
        }
        abrirFormulario(seleccionada);
    }

    @FXML
    private void onEliminarCategoria() {
        Categoria seleccionada = tablaCategorias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarError("Seleccione una categoría para eliminar.");
            return;
        }
        if (!confirmar("¿Seguro que desea eliminar esta categoría?")) {
            return;
        }
        try {
        	controlador.eliminarCategoria(seleccionada.getNombre());
            cargarCategorias();
		} catch (IllegalStateException e) {
			mostrarError(e.getMessage());
		}
    }

    // FORMULARIO

    private void abrirFormulario(Categoria categoria) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "/umu/tds/gestion_gastos/categorias/FormularioCategoria.fxml"
            )
        );
        Parent root = loader.load();
        FormularioCategoriaController controladorForm = loader.getController();

        controladorForm.setControlador(controlador);
        controladorForm.setCategoria(categoria);
        controladorForm.setOnSave(this::cargarCategorias);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(categoria == null ? "Nueva categoría" : "Editar categoría");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // UTILIDADES

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK)
            .showAndWait();
    }

    private boolean confirmar(String msg) {
        return new Alert(
                Alert.AlertType.CONFIRMATION,
                msg,
                ButtonType.OK,
                ButtonType.CANCEL)
        		.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}

