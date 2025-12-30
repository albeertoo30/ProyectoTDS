package umu.tds.gestion_gastos.notificacion.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.notificacion.INotificacionFilter;
import umu.tds.gestion_gastos.notificacion.Notificacion;
import umu.tds.gestion_gastos.notificacion.NotificacionFilterCategoria;
import umu.tds.gestion_gastos.notificacion.NotificacionFilterFechaRange;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ControladorVentanaNotificacion {

    @FXML private ListView<Notificacion> ListViewNN;
    @FXML private ListView<Notificacion> ListViewVN;
    @FXML private Button bLimpiarHN;
    @FXML private Button bMarcarLeidaN;
    @FXML private Button bAplicarFiltros;
    @FXML private Button bLimpiarFiltros;
    
    @FXML private DatePicker dpDesde;
    @FXML private DatePicker dpHasta;
    @FXML private ComboBox<Categoria> cbCategorias;

    private ControladorApp controlador;
    private final ObservableList<Notificacion> nuevas = FXCollections.observableArrayList();
    private final ObservableList<Notificacion> vistas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        ListViewNN.setItems(nuevas);
        ListViewVN.setItems(vistas);

        ListViewNN.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListViewNN.setFocusTraversable(true);

        ListViewNN.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            bMarcarLeidaN.setDisable(newSel == null);
        });

        configurarCeldas();
        configurarComboCategoria();
        
    }
    
    

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCategorias();
        cargarNotificaciones();
    }

    private void cargarNotificaciones() {
        if (controlador == null) return;

        nuevas.clear();
        vistas.clear();

        List<Notificacion> todas = controlador.getNotificaciones();
        for (Notificacion n : todas) {
            if (n.isLeida()) vistas.add(n);
            else nuevas.add(n);
        }
    }

    @FXML
    private void onMarcarLeida() {
        List<Notificacion> seleccionadas = new ArrayList<>(ListViewNN.getSelectionModel().getSelectedItems());
        for (Notificacion n : seleccionadas) {
            controlador.marcarNotificacionLeida(n.getId());
        }
        cargarNotificaciones();
    }

    @FXML
    private void onLimpiarHistorial() {
        controlador.limpiarHistorialNotificaciones();
        cargarNotificaciones();
    }

    private void configurarCeldas() {
        ListViewNN.setCellFactory(lv -> new CeldaNotificacion());
        ListViewVN.setCellFactory(lv -> new CeldaNotificacion());
    }

    private static class CeldaNotificacion extends ListCell<Notificacion> {
        @Override
        protected void updateItem(Notificacion n, boolean empty) {
            super.updateItem(n, empty);
            if (empty || n == null) {
                setText(null);
            } else {
                setText("[" + n.getFecha() + "] " + n.getMensaje() + " , " +  n.getImporte() + " , " + n.getCategoria().getNombre());
            }
        }
    }

    private void configurarComboCategoria() {
        cbCategorias.setPromptText("Todas las categorías");
        
        cbCategorias.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Todas las categorías");
                } else {
                    setText(item.getNombre());
                }
            }
        });
        
        cbCategorias.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Categoria item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getNombre());
            }
        });
    }

    private void cargarCategorias() {
        cbCategorias.getItems().clear();
        cbCategorias.getItems().add(null);
        cbCategorias.getItems().addAll(controlador.obtenerCategorias());
    }

    @FXML
    private void onAplicarFiltros() {
        INotificacionFilter filtroCompuesto = INotificacionFilter.alwaysTrue();
        
        LocalDate desde = dpDesde.getValue();
        LocalDate hasta = dpHasta.getValue();
        if (desde != null || hasta != null) {
            filtroCompuesto = filtroCompuesto.and(new NotificacionFilterFechaRange(desde, hasta));
        }
        
        Categoria categoriaSeleccionada = cbCategorias.getValue();
        if (categoriaSeleccionada != null) {
            filtroCompuesto = filtroCompuesto.and(new NotificacionFilterCategoria(categoriaSeleccionada));
        }
        
        //Aqui se agregan los futuros filtros
        
        
        
        List<Notificacion> filtradas = controlador.filtrarNotificaciones(filtroCompuesto);
        
        nuevas.clear();
        vistas.clear();
        for (Notificacion n : filtradas) {
            if (n.isLeida()) vistas.add(n);
            else nuevas.add(n);
        }
    }

    @FXML
    private void onLimpiarFiltros() {
        dpDesde.setValue(null);
        dpHasta.setValue(null);
        cbCategorias.setValue(null);
        cargarNotificaciones();
    }
}