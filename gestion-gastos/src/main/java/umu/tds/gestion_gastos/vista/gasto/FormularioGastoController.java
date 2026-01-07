package umu.tds.gestion_gastos.vista.gasto;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.controlador.ControladorApp;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class FormularioGastoController {

    @FXML private DatePicker campoFecha;
    @FXML private ComboBox<Categoria> campoCategoria;
    @FXML private TextField campoCantidad;
    @FXML private TextArea campoDescripcion;
    @FXML private ComboBox<Object> campoCuenta;
    @FXML private ComboBox<Usuario> campoUsuario;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;

    private ControladorApp controlador;
    private Gasto gastoEditando;
    private Runnable onSaveCallback;

    //@Override
    // public void initialize(URL location, ResourceBundle resources) {}
    
    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCategorias();
        cargarCuentas();
        configurarConvertidores();
        if (controlador.getUsuarioActual() != null) {
            campoUsuario.setValue(controlador.getUsuarioActual());
            campoUsuario.setDisable(true);
        }
    }

    public void setGasto(Gasto g) {
        this.gastoEditando = g;
        if (g != null) {
            campoFecha.setValue(g.getFecha());
            campoCategoria.setValue(g.getCategoria());
            campoCantidad.setText(String.valueOf(g.getCantidad()));
            campoDescripcion.setText(g.getDescripcion());
            if(g.getCuenta() == null) {
            	campoCuenta.setValue("Individual");
            }else {
            	campoCuenta.setValue(g.getCuenta());
            }
            campoUsuario.setValue(g.getUsuario());
        }
    }

    public void setOnSave(Runnable r) {
        this.onSaveCallback = r;
    }
    
    // Configura cómo se muestran los objetos en los ComboBox
    private void configurarConvertidores() {
        // Convertidor Categoría
        campoCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria c) { 
                String resultado = c == null ? "" : c.getNombre();
                return resultado;
            }
            @Override
            public Categoria fromString(String s) { return null; }
        });

        // Convertidor Cuenta (Acepta String y Objeto Cuenta)
        campoCuenta.setConverter(new StringConverter<Object>() {
            @Override
            public String toString(Object obj) {
                if (obj == null) return "";
                if (obj instanceof String) return (String) obj; // Caso "Individual"
                if (obj instanceof Cuenta) return ((Cuenta) obj).getNombre(); // Caso Cuenta Compartida
                return obj.toString();
            }
            @Override
            public Object fromString(String string) { return null; }
        });
        
        // Convertidor Usuario
        campoUsuario.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario u) { return u == null ? "" : u.getNombre(); }
            @Override
            public Usuario fromString(String s) { return null; }
        });
    }


    @FXML
    private void onGuardar() {
        // 1. Validaciones de interfaz
        if (campoFecha.getValue() == null || 
            campoCategoria.getValue() == null || 
            campoCantidad.getText().isBlank() ||
            campoCuenta.getValue() == null) {
            mostrarError("Todos los campos son obligatorios");
            return;
        }

        try {
            // 2. Extracción de datos de la vista
            LocalDate fecha = campoFecha.getValue();
            Categoria cat = campoCategoria.getValue();
            double cantidad = Double.parseDouble(campoCantidad.getText());
            String desc = campoDescripcion.getText();
            Usuario pagador = controlador.getUsuarioActual();
            Object seleccionCuenta = campoCuenta.getValue();
            Cuenta cuentaDestino = null;

            if (seleccionCuenta.equals("Individual") || (seleccionCuenta instanceof String && ((String)seleccionCuenta).equalsIgnoreCase("Individual"))) {
                // CASO INDIVIDUAL:
                cuentaDestino = controlador.obtenerTodasLasCuentas().stream()
                    .filter(c -> c instanceof umu.tds.gestion_gastos.cuenta.CuentaIndividual)
                    .findFirst()
                    .orElse(null);
                
                if (cuentaDestino == null) {
                    mostrarError("Error crítico: No se encuentra tu Cuenta Individual. Contacta con soporte.");
                    return;
                }
                
            } else if (seleccionCuenta instanceof Cuenta) {
                // CASO COMPARTIDA:
                cuentaDestino = (Cuenta) seleccionCuenta;
                if (pagador == null) {
                    mostrarError("En una cuenta compartida debes indicar quién ha pagado (Usuario).");
                    return;
                }
            }

            // 3. Delegación al Controlador (Patrón Creador / Controlador)
            if (gastoEditando == null) {
                controlador.crearGasto(fecha, cantidad, desc, cat, pagador, cuentaDestino);
            } else {
                // EDICIÓN: Pasamos el objeto a editar y los nuevos datos
                controlador.actualizarGasto(gastoEditando, fecha, cantidad, desc, cat, pagador, cuentaDestino);
            }

            if (onSaveCallback != null) onSaveCallback.run();
            cerrar();

        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número válido.");
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    // Métodos auxiliares
    private void cargarCategorias() {
        if (controlador != null) {
            List<Categoria> categorias = controlador.obtenerCategorias();
            campoCategoria.getItems().setAll(categorias);
            campoCategoria.setPromptText("Seleccione una categoría");
        }
    }

    private void cargarCuentas() {
        if (controlador != null) {
            campoCuenta.getItems().clear();
            
            // 1. Añadir opción por defecto
            campoCuenta.getItems().add("Individual");
            
            // 2. Añadir cuentas compartidas reales
            List<Cuenta> cuentasCompartidas = controlador.obtenerCuentasCompartidas();
            campoCuenta.getItems().addAll(cuentasCompartidas);
            
            // 3. Seleccionar "Individual" por defecto
            campoCuenta.getSelectionModel().select("Individual");
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