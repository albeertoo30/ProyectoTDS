package umu.tds.gestion_gastos.vista.cuenta;

import java.util.Optional;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
        colPagador.setCellValueFactory(cell -> {
            Usuario u = cell.getValue().getUsuario();
            return new SimpleStringProperty(u != null ? u.getNombre() : "Desconocido");
        });
    }

    private void actualizarVista() {
        if (cuentaActual == null) return;
        
        lblNombreCuenta.setText(cuentaActual.getNombre());
        listaMiembros.getItems().setAll(cuentaActual.getMiembros());
        tablaGastos.getItems().setAll(cuentaActual.getGastos());
    }

    @FXML
    private void onNuevoGasto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/cuentas/FormularioGastoCompartido.fxml"));
            javafx.scene.Parent root = loader.load();
            FormularioGastoCompartidoController formController = loader.getController();
            
            formController.initData(
                cuentaActual.getMiembros(), 
                controlador.obtenerCategorias() 
            );
            
            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Nuevo Gasto - " + cuentaActual.getNombre());
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();


            if (formController.isGuardado()) {
                Gasto nuevoGasto = formController.getGastoCreado();
                
                //Vincular gasto a esta cuenta
                nuevoGasto.setCuenta(this.cuentaActual);
                
                this.cuentaActual.agregarGasto(nuevoGasto);
                
                controlador.registrarCuenta(this.cuentaActual);
                
                actualizarVista();
                
                System.out.println("Gasto añadido y guardado.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al abrir formulario: " + e.getMessage());
        }
    }
    

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();

    @FXML
    private void onEditarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un gasto para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/cuentas/FormularioGastoCompartido.fxml"));
            javafx.scene.Parent root = loader.load();

            FormularioGastoCompartidoController formController = loader.getController();
            
            formController.initData(cuentaActual.getMiembros(), controlador.obtenerCategorias());
            
            // 2. Relleneamos campos con el gasto a editar
            formController.setGastoEditar(seleccionado);

            Stage stage = new Stage();
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Gasto");
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();

            if (formController.isGuardado()) {

                controlador.registrarCuenta(this.cuentaActual);
                
                
                tablaGastos.refresh(); 
                actualizarVista(); 
                
                
                System.out.println("Gasto editado correctamente.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al editar: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Selecciona un gasto para borrar.");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar Gasto");
        alert.setHeaderText("¿Borrar gasto de " + seleccionado.getCantidad() + "€?");
        alert.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean borrado = cuentaActual.getGastos().remove(seleccionado);
            
            if (borrado) {
                // cuenta actualizada en el JSON
                controlador.registrarCuenta(this.cuentaActual);
                
                actualizarVista();
            } else {
                mostrarError("No se pudo borrar el gasto de la lista.");
            }
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) lblNombreCuenta.getScene().getWindow();
        stage.close();
    }
}