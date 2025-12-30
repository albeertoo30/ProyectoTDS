package umu.tds.gestion_gastos.vista.cuenta;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.cuenta.CuentaIndividual;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;

public class FormularioCuentaController {

    @FXML private TextField campoNombre;
    @FXML private ComboBox<String> comboTipo;
    @FXML private ListView<Usuario> listaUsuarios;
    @FXML private HBox boxNuevoUsuario;
    @FXML private TextField campoNuevoUsuario;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ControladorApp controlador;
    private Runnable onSaveCallback;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarDatosIniciales();
    }

    public void setOnSave(Runnable r) {
        this.onSaveCallback = r;
    }

    @FXML
    private void initialize() {
        comboTipo.getItems().addAll("INDIVIDUAL", "COMPARTIDA");
        comboTipo.setValue("INDIVIDUAL");

        listaUsuarios.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        actualizarEstadoComponentes(false);

        comboTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean esCompartida = "COMPARTIDA".equals(newVal);
            actualizarEstadoComponentes(esCompartida);
        });
    }

    private void actualizarEstadoComponentes(boolean esCompartida) {
        listaUsuarios.setDisable(!esCompartida);
        boxNuevoUsuario.setDisable(!esCompartida);
    }

    private void cargarDatosIniciales() {
        if (controlador != null) {
            List<Usuario> usuarios = controlador.obtenerTodosLosUsuarios();
            listaUsuarios.getItems().setAll(usuarios);
        }
    }

    @FXML
    private void onAgregarUsuario() {
        String nombreNuevo = campoNuevoUsuario.getText();
        if (nombreNuevo == null || nombreNuevo.isBlank()) {
            mostrarError("Escribe el nombre del nuevo amigo.");
            return;
        }

        int idGenerado = (int) (Math.random() * 100000);
        Usuario nuevoAmigo = new Usuario(idGenerado, nombreNuevo);

        try {
            // Asumo que añades este método a ControladorApp delegando en GestorUsuarios
            // controlador.registrarUsuario(nuevoAmigo); 
            // Si no lo tienes, añádelo. Por ahora lo añadimos a la lista visual.
            
            listaUsuarios.getItems().add(nuevoAmigo);
            listaUsuarios.getSelectionModel().select(nuevoAmigo);
            campoNuevoUsuario.clear();
        } catch (Exception e) {
            mostrarError("Error al crear usuario: " + e.getMessage());
        }
    }

    @FXML
    private void onGuardar() {
        String nombre = campoNombre.getText();
        String tipo = comboTipo.getValue();

        if (nombre == null || nombre.isBlank()) {
            mostrarError("El nombre es obligatorio");
            return;
        }

        try {
            Usuario yo = controlador.getUsuarioActual();
            Cuenta nuevaCuenta;

            if ("INDIVIDUAL".equals(tipo)) {
                nuevaCuenta = new CuentaIndividual(0, nombre, yo);
            } else {
                ObservableList<Usuario> seleccionados = listaUsuarios.getSelectionModel().getSelectedItems();
                List<Usuario> miembros = new ArrayList<>(seleccionados);

                if (!miembros.contains(yo)) {
                    miembros.add(yo);
                }

                if (miembros.size() < 2) {
                    mostrarError("Selecciona al menos un amigo de la lista.");
                    return;
                }
                nuevaCuenta = new CuentaCompartida(0, nombre, miembros);
            }

            controlador.registrarCuenta(nuevaCuenta);

            if (onSaveCallback != null) onSaveCallback.run();
            cerrar();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al guardar: " + e.getMessage());
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