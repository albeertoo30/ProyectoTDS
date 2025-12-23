package umu.tds.gestion_gastos.vista.categoria;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class FormularioCategoriaController {

    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;
    @FXML private TextArea campoDescripcion;
    @FXML private TextField campoNombre;

    private ControladorApp controlador;
    private Categoria categoriaEditando;
    private Runnable onSaveCallback;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
    }

    public void setCategoria(Categoria categoria) {
        this.categoriaEditando = categoria;
        if (categoria != null) {
            campoNombre.setText(categoria.getNombre());
            campoDescripcion.setText(categoria.getDescripcion());
            campoNombre.setDisable(true);
        }
    }

    public void setOnSave(Runnable callback) {
        this.onSaveCallback = callback;
    }

    @FXML
    private void initialize() {
        // Sin lógica de negocio
    }

    @FXML
    private void onGuardar() {
        try {
            String nombre = campoNombre.getText();
            String descripcion = campoDescripcion.getText();

            if (nombre == null || nombre.isBlank()) {
                mostrarError("El nombre de la categoría es obligatorio.");
                return;
            }
            if (categoriaEditando == null) {
                controlador.crearCategoria(nombre, descripcion);
            } else {
                categoriaEditando.setDescripcion(descripcion);
                controlador.editarCategoria(categoriaEditando);
            }
            if (onSaveCallback != null) onSaveCallback.run();
            cerrar();
        } catch (Exception e) {
            mostrarError("Error al guardar la categoría: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}
