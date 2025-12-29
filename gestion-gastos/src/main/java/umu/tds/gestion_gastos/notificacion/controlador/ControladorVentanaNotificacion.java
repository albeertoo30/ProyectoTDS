package umu.tds.gestion_gastos.notificacion.controlador;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.notificacion.Notificacion;

import java.util.List;

public class ControladorVentanaNotificacion {

    // ====== FXML ======
    @FXML private ListView<Notificacion> ListViewNN; // Nuevas
    @FXML private ListView<Notificacion> ListViewVN; // Vistas
    @FXML private Button bLimpiarHN;
    @FXML private Button bMarcarLeidaN;

    // ====== Dependencias ======
    private ControladorApp controlador;

    // ====== Modelos observables ======
    private final ObservableList<Notificacion> nuevas =
            FXCollections.observableArrayList();
    private final ObservableList<Notificacion> vistas =
            FXCollections.observableArrayList();

    // ====== Inicialización JavaFX ======
    @FXML
    private void initialize() {
        ListViewNN.setItems(nuevas);
        ListViewVN.setItems(vistas);

        ListViewNN.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ListViewVN.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        configurarCeldas();
    }

    // ====== Inyección del controlador de la app ======
    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarNotificaciones();
    }

    // ====== Cargar datos ======
    private void cargarNotificaciones() {
        if (controlador == null) return;

        nuevas.clear();
        vistas.clear();

        List<Notificacion> todas = controlador.getNotificaciones();

        for (Notificacion n : todas) {
            if (n.isLeida()) {
                vistas.add(n);
            } else {
                nuevas.add(n);
            }
        }
    }

    // ====== Botón: Marcar como leída ======
    @FXML
    private void onMarcarLeida() {
        Notificacion seleccionada =
                ListViewNN.getSelectionModel().getSelectedItem();

        if (seleccionada == null) return;

        controlador.marcarNotificacionLeida(seleccionada.getId());
        cargarNotificaciones();
    }

    // ====== Botón: Limpiar historial ======
    @FXML
    private void onLimpiarHistorial() {
        controlador.limpiarHistorialNotificaciones();
        cargarNotificaciones();
    }

    // ====== Renderizado de notificaciones ======
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
                setText(
                    "[" + n.getFecha() + "] " +
                    n.getMensaje()
                );
            }
        }
    }
}
