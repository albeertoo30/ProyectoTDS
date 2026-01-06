package umu.tds.gestion_gastos.vista.cuenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;

public class FormularioCuentaController {

    @FXML private TextField campoNombre;
    @FXML private ComboBox<String> comboTipo;
    
    @FXML private TableView<ParticipanteFx> tablaParticipantes;
    @FXML private TableColumn<ParticipanteFx, Boolean> colSeleccion;
    @FXML private TableColumn<ParticipanteFx, String> colNombre;
    @FXML private TableColumn<ParticipanteFx, Double> colPorcentaje;
    
    @FXML private CheckBox checkEquitativo;
    @FXML private HBox boxNuevoUsuario;
    @FXML private TextField campoNuevoUsuario;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ControladorApp controlador;
    private Runnable onSaveCallback;
    
    private ObservableList<ParticipanteFx> listaParticipantes = FXCollections.observableArrayList();

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarDatosIniciales();
    }

    public void setOnSave(Runnable r) {
        this.onSaveCallback = r;
    }

    @FXML
    private void initialize() {
        comboTipo.getItems().add("COMPARTIDA");
        comboTipo.setValue("COMPARTIDA");
        
        // Configuración de columnas
        colSeleccion.setCellValueFactory(cellData -> cellData.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUsuario().getNombre()));
        
        colPorcentaje.setCellValueFactory(cellData -> cellData.getValue().porcentajeProperty().asObject());
        colPorcentaje.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        
        colPorcentaje.setOnEditCommit(event -> {
            ParticipanteFx row = event.getRowValue();
            row.setPorcentaje(event.getNewValue());
        });

        tablaParticipantes.setItems(listaParticipantes);

        checkEquitativo.selectedProperty().addListener((obs, oldVal, isEquitativo) -> {
            actualizarEstadoColumnas(isEquitativo);
        });
        
        comboTipo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean esCompartida = "COMPARTIDA".equals(newVal);
            actualizarVisibilidad(esCompartida);
        });

        actualizarEstadoColumnas(true); 
    }

    private void actualizarVisibilidad(boolean esCompartida) {
        tablaParticipantes.setDisable(!esCompartida);
        boxNuevoUsuario.setDisable(!esCompartida);
        checkEquitativo.setDisable(!esCompartida);
    }
    
    private void actualizarEstadoColumnas(boolean esEquitativo) {
        colPorcentaje.setEditable(!esEquitativo);
        
        if (esEquitativo) {
            colPorcentaje.setVisible(false);
        } else {
            colPorcentaje.setVisible(true);
        }
    }

    private void cargarDatosIniciales() {
        if (controlador != null) {
            List<Usuario> usuariosSistema = controlador.obtenerTodosLosUsuarios();
            Usuario yo = controlador.getUsuarioActual();
            
            agregarParticipanteATabla(yo, true);
            
            for (Usuario u : usuariosSistema) {
                agregarParticipanteATabla(u, false);
            }
        }
    }

    private void agregarParticipanteATabla(Usuario u, boolean seleccionadoPorDefecto) {
        boolean existe = listaParticipantes.stream().anyMatch(p -> {
            Usuario existente = p.getUsuario();
            
            if (existente.getId() > 0 && u.getId() > 0) {
                return existente.getId() == u.getId();
            }
            
            return existente.getNombre().equalsIgnoreCase(u.getNombre());
        });
        
        if (!existe) {
            listaParticipantes.add(new ParticipanteFx(u, 0.0, seleccionadoPorDefecto));
        }
    }

    @FXML
    private void onAgregarUsuario() {
        String nombreNuevo = campoNuevoUsuario.getText();
        if (nombreNuevo == null || nombreNuevo.isBlank()) {
            mostrarError("Escribe el nombre del nuevo amigo.");
            return;
        }

        Usuario nuevoAmigo = new Usuario(0, nombreNuevo); 
        agregarParticipanteATabla(nuevoAmigo, true);
        campoNuevoUsuario.clear();
    }
    
    @FXML
    private void onGuardar() {
        String nombre = campoNombre.getText();

        if (nombre == null || nombre.isBlank()) {
            mostrarError("El nombre es obligatorio");
            return;
        }

        try {
            List<ParticipanteFx> participantesSeleccionados = listaParticipantes.stream()
                    .filter(ParticipanteFx::isSeleccionado)
                    .collect(Collectors.toList());

            if (participantesSeleccionados.size() < 1) {
                    mostrarError("Debes seleccionar al menos un participante.");
                    return;
            }

            for (ParticipanteFx p : participantesSeleccionados) {
                Usuario u = p.getUsuario();
                if (u.getId() == 0) {
                    controlador.registrarUsuario(u);
                }
            }

            
            List<Usuario> miembros = participantesSeleccionados.stream()
                    .map(ParticipanteFx::getUsuario)
                    .collect(Collectors.toList());

            CuentaCompartida cuentaComp = new CuentaCompartida(0, nombre, miembros);
            
            if (!checkEquitativo.isSelected()) {
                double suma = participantesSeleccionados.stream().mapToDouble(ParticipanteFx::getPorcentaje).sum();
                
                if (Math.abs(suma - 100.0) > 0.1) {
                    mostrarError("La suma de porcentajes seleccionados debe ser 100%. Actual: " + suma + "%");
                    return;
                }
                
                Map<String, Double> mapaPorcentajes = new HashMap<>();
                for (ParticipanteFx row : participantesSeleccionados) {
                    // AHORA ES SEGURO: Todos los usuarios tienen ID real porque los acabamos de guardar
                    mapaPorcentajes.put(String.valueOf(row.getUsuario().getId()), row.getPorcentaje());
                }
                cuentaComp.setPorcentajes(mapaPorcentajes);
            }

            controlador.registrarCuenta(cuentaComp);

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
    
    public static class ParticipanteFx {
        private final Usuario usuario;
        private final DoubleProperty porcentaje;
        private final BooleanProperty seleccionado;

        public ParticipanteFx(Usuario usuario, Double porcentaje, boolean isSelected) {
            this.usuario = usuario;
            this.porcentaje = new SimpleDoubleProperty(porcentaje);
            this.seleccionado = new SimpleBooleanProperty(isSelected);
        }

        public Usuario getUsuario() { return usuario; }
        
        public double getPorcentaje() { return porcentaje.get(); }
        public void setPorcentaje(double v) { porcentaje.set(v); }
        public DoubleProperty porcentajeProperty() { return porcentaje; }
        
        public boolean isSeleccionado() { return seleccionado.get(); }
        public void setSeleccionado(boolean v) { seleccionado.set(v); }
        public BooleanProperty seleccionadoProperty() { return seleccionado; }
    }
}