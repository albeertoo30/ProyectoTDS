package umu.tds.gestion_gastos.vista.gasto;

import java.time.LocalDate;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class FormularioGastoController {

    @FXML private DatePicker campoFecha;
    @FXML private ComboBox<Categoria> campoCategoria;
    @FXML private TextField campoCantidad;
    @FXML private TextArea campoDescripcion;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ControladorApp controlador;

    private Gasto gastoEditando;
    private Runnable onSaveCallback;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCategorias();
    }

    public void setGasto(Gasto g) {
        this.gastoEditando = g;
        if (g != null) {
            campoFecha.setValue(g.getFecha());
            campoCategoria.setValue(g.getCategoria());
            campoCantidad.setText(String.valueOf(g.getCantidad()));
            campoDescripcion.setText(g.getDescripcion());
        }
    }

    public void setOnSave(Runnable r) {
        this.onSaveCallback = r;
    }

    @FXML
    private void initialize() {
        // No hay lógica de negocio aquí
    }

    @FXML
    private void onGuardar() {
    	if (campoFecha.getValue() == null || campoCategoria.getValue() == null || campoCantidad.getText().isBlank()) {
    		mostrarError("Todos los campos son obligatorios");
    		return;
    	}
        try {
            LocalDate fecha = campoFecha.getValue();
            Categoria cat = campoCategoria.getValue();
            double cantidad = Double.parseDouble(campoCantidad.getText());
            String desc = campoDescripcion.getText();

            if (gastoEditando == null) {
                // ID = 0 provisional (lo corregiremos con persistencia)
                Gasto nuevo = new Gasto(0, fecha, cantidad, desc, cat);
                controlador.registrarGasto(nuevo);
            } else {
                gastoEditando.setFecha(fecha);
                gastoEditando.setCategoria(cat);
                gastoEditando.setCantidad(cantidad);
                gastoEditando.setDescripcion(desc);
                controlador.editarGasto(gastoEditando);
            }

            if (onSaveCallback != null) onSaveCallback.run();
            cerrar();
        } catch (Exception e) {
            mostrarError("Datos incorrectos: " + e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    // Métodos auxiliares
    private void cargarCategorias() {
        if (controlador != null) {
            campoCategoria.getItems().setAll(controlador.obtenerCategorias());
        }
    }

    private void cerrar() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }
}