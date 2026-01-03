package umu.tds.gestion_gastos.vista.cuenta;

import java.time.LocalDate;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class FormularioGastoCompartidoController {


    @FXML private TextField campoCantidad;
    @FXML private DatePicker campoFecha;
    @FXML private ComboBox<Categoria> campoCategoria;
    @FXML private ComboBox<Usuario> campoUsuario;
    @FXML private TextArea campoDescripcion;

    private Gasto gastoCreado; 
    private Gasto gastoEdicion; 
    private boolean guardado = false;

    @FXML
    public void initialize() {
        campoFecha.setValue(LocalDate.now());
    }


    public void initData(List<Usuario> miembrosGrupo, List<Categoria> categoriasDisponibles) {
        campoUsuario.getItems().setAll(miembrosGrupo);
        campoCategoria.getItems().setAll(categoriasDisponibles);
        

        if (gastoEdicion == null && !miembrosGrupo.isEmpty()) {
            campoUsuario.getSelectionModel().select(0);
        }
    }



    public void setGastoEditar(Gasto g) {
        this.gastoEdicion = g;
        
        // rellenamos la vista
        campoDescripcion.setText(g.getDescripcion());
        campoCantidad.setText(String.valueOf(g.getCantidad()));
        campoFecha.setValue(g.getFecha());
        
        
        campoCategoria.getSelectionModel().select(g.getCategoria());
        campoUsuario.getSelectionModel().select(g.getUsuario());
    }

    @FXML
    private void onGuardar() {
        try {
            
            String descripcion = campoDescripcion.getText();
            if (descripcion == null || descripcion.trim().isEmpty()) {
                mostrarError("La descripción es obligatoria.");
                return;
            }
            
            String importeStr = campoCantidad.getText();
            if (importeStr == null || importeStr.trim().isEmpty()) {
                mostrarError("Introduce la cantidad.");
                return;
            }
            double importe = Double.parseDouble(importeStr.replace(",", "."));
            
            LocalDate fecha = campoFecha.getValue();
            if (fecha == null) {
                mostrarError("Selecciona una fecha.");
                return;
            }

            Categoria cat = campoCategoria.getValue();
            if (cat == null) {
                mostrarError("Selecciona una categoría.");
                return;
            }

            Usuario pagador = campoUsuario.getValue();
            if (pagador == null) {
                mostrarError("¡Es obligatorio indicar quién paga!");
                return;
            }
            if (this.gastoEdicion != null) {

                this.gastoEdicion.setDescripcion(descripcion);
                this.gastoEdicion.setCantidad(importe);
                this.gastoEdicion.setFecha(fecha);
                this.gastoEdicion.setCategoria(cat);
                this.gastoEdicion.setUsuario(pagador);
                

                this.gastoCreado = this.gastoEdicion;
                
            } else {
           
                int idTemp = (int)(System.currentTimeMillis() % 1000000); 


                this.gastoCreado = new Gasto(idTemp, fecha, importe, descripcion, cat);
                this.gastoCreado.setUsuario(pagador);
            }

            this.guardado = true;
            cerrar();

        } catch (NumberFormatException e) {
            mostrarError("La cantidad debe ser un número (ej: 10.50)");
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
        Stage stage = (Stage) campoCantidad.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    public boolean isGuardado() {
        return guardado;
    }

    public Gasto getGastoCreado() {
        return gastoCreado;
    }
}