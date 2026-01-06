package umu.tds.gestion_gastos.vista.notificacion;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.notificacion.Notificacion;
import umu.tds.gestion_gastos.notificacion.NotificacionFilterBuilder;

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

        List<Notificacion> todas = controlador.getNotificacionesPorCuenta();
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
                String categoriaText = n.getCategoria() != null ? n.getCategoria().getNombre() : "Sin categoría";
                setText(String.format("[%s] %s | %.2f€ | %s | %s ", 
                    n.getFecha(), 
                    n.getMensaje(), 
                    n.getImporte(), 
                    categoriaText,
                    n.getIdCuenta()
                ));
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
    private void onAplicarFiltros() {			//creo que se puede hacer en el controlador
       /* Filtro<Notificacion> filtroCompuesto = new NotificacionFilterBuilder()
            .cuenta(Configuracion.getInstancia().getCuentaActual())
        	.fecha(dpDesde.getValue(), dpHasta.getValue())
            .categoria(cbCategorias.getValue())
            .build();
        */
    	
    	Filtro<Notificacion> filtroCompuesto = 
    			controlador.crearFiltroCompuestoN(Configuracion.getInstancia().getCuentaActual(),
    					dpDesde.getValue(), dpHasta.getValue(), cbCategorias.getValue());
    	
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