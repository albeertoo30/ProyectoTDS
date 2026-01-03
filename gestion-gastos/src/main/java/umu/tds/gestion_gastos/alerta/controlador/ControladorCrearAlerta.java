package umu.tds.gestion_gastos.alerta.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.alerta.AlertaStrategy;
import umu.tds.gestion_gastos.alerta.AlertaStrategyFactory;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class ControladorCrearAlerta {

    @FXML private TextField tfDescripcion;
    @FXML private TextField tfLimite;
    @FXML private ComboBox<Categoria> cbCategorias;
    @FXML private ComboBox<String> cbTipo;

    private ControladorApp controlador;

    @FXML
    private void initialize() {
        cbTipo.getItems().addAll(AlertaStrategyFactory.getTiposDisponibles());
    }

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cbCategorias.getItems().add(null);
        cbCategorias.getItems().addAll(controlador.obtenerCategorias());
    }

    @FXML
    private void onCrearAlerta() {
        try {
            String descripcion = tfDescripcion.getText();
            String tipo = cbTipo.getValue();
            Categoria categoria = cbCategorias.getValue();
            double limite = Double.parseDouble(tfLimite.getText());

            if (descripcion.isBlank() || tipo == null) {
                mostrarError("Debes rellenar descripción y tipo");
                return;
            }

            AlertaStrategy strategy = AlertaStrategyFactory.crear(tipo);

            controlador.crearAlerta(
                    descripcion,
                    categoria,
                    strategy,
                    limite,
                    Configuracion.getInstancia().getCuentaActual());

            mostrarInfo("Alerta creada correctamente");
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("El límite debe ser numérico");
        }
    }

    @FXML
    private void onCancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) tfDescripcion.getScene().getWindow();
        stage.close();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
