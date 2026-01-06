package umu.tds.gestion_gastos.vista.alerta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.alerta.Alerta;
import umu.tds.gestion_gastos.alerta.AlertaFilterBuilder;
import umu.tds.gestion_gastos.alerta.AlertaMensual;
import umu.tds.gestion_gastos.alerta.AlertaSemanal;
import umu.tds.gestion_gastos.alerta.AlertaStrategy;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.filtros.Filtro;

import java.io.IOException;
import java.util.List;

public class ControladorVentanaAlerta {

    @FXML private ListView<Alerta> ListViewActivas;
    @FXML private ListView<Alerta> ListViewInactivas;
    @FXML private Button bEliminarAlerta;
    @FXML private Button bActivarAlerta;
    @FXML private Button bAplicarFiltros;
    @FXML private Button bLimpiarFiltros;
    @FXML private Button bNuevaAlerta;

    
    @FXML private TextField tfLimiteDesde;
    @FXML private TextField tfLimiteHasta;
    @FXML private ComboBox<Categoria> cbCategorias;

    private ControladorApp controlador;
    private final ObservableList<Alerta> activas = FXCollections.observableArrayList();
    private final ObservableList<Alerta> inactivas = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        ListViewActivas.setItems(activas);
        ListViewInactivas.setItems(inactivas);

        configurarSeleccion();
        configurarCeldas();
        configurarComboCategoria();
    }

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarCategorias();
        cargarAlertas();
    }

    private void cargarAlertas() {
        if (controlador == null) return;

        activas.clear();
        inactivas.clear();

        List<Alerta> todas = controlador.getAlertasPorCuenta();
        for (Alerta a : todas) {
            if (a.isActiva()) activas.add(a);
            else inactivas.add(a);
        }
    }

    private void configurarCeldas() {
        ListViewActivas.setCellFactory(lv -> new CeldaAlerta());
        ListViewInactivas.setCellFactory(lv -> new CeldaAlerta());
    }

    //Ahora se muestra a la cuenta que pertenece (que no tiene sentido porque es la individual siempre)
    private static class CeldaAlerta extends ListCell<Alerta> {
        @Override
        protected void updateItem(Alerta a, boolean empty) {
            super.updateItem(a, empty);
            if (empty || a == null) {
                setText(null);
            } else {
                String categoriaText = a.getCategoria() != null ? a.getCategoria().getNombre() : "Sin categoría";
                setText(String.format("%s | %.2f€ | %s | %s | %s ",
                        a.getDescripcion(),
                        a.getLimite(),
                        categoriaText,
                        a.isActiva() ? "Activa" : "Inactiva",
                        a.getIdCuenta()
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
                setText(empty || item == null ? "Todas las categorías" : item.getNombre());
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

    private void configurarSeleccion() {
        ListViewActivas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ListViewInactivas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        ListViewActivas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> actualizarBotones());
        ListViewInactivas.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> actualizarBotones());
    }

    private void actualizarBotones() {
        boolean algoSeleccionado = !ListViewActivas.getSelectionModel().getSelectedItems().isEmpty()
                || !ListViewInactivas.getSelectionModel().getSelectedItems().isEmpty();
        bActivarAlerta.setDisable(!algoSeleccionado);
        bEliminarAlerta.setDisable(!algoSeleccionado);
    }

    @FXML
    private void onAplicarFiltros() {
        Double limiteDesde = tfLimiteDesde.getText().isEmpty() ? null : Double.valueOf(tfLimiteDesde.getText());
        Double limiteHasta = tfLimiteHasta.getText().isEmpty() ? null : Double.valueOf(tfLimiteHasta.getText());

        Filtro<Alerta> filtroCompuesto = new AlertaFilterBuilder()
                .cuenta(Configuracion.getInstancia().getCuentaActual())  //Raro pero no se como hacerlo de otra forma
        		.limite(limiteDesde, limiteHasta)
                .categoria(cbCategorias.getValue())
                .build();

        List<Alerta> filtradas = controlador.filtrarAlertas(filtroCompuesto);

        activas.clear();
        inactivas.clear();
        for (Alerta a : filtradas) {
            if (a.isActiva()) activas.add(a);
            else inactivas.add(a);
        }
    }

    @FXML
    private void onLimpiarFiltros() {
        tfLimiteDesde.clear();
        tfLimiteHasta.clear();
        cbCategorias.setValue(null);
        cargarAlertas();
    }

    @FXML
    private void onEliminarAlerta() {
        List<Alerta> seleccionadas = FXCollections.observableArrayList();
        seleccionadas.addAll(ListViewActivas.getSelectionModel().getSelectedItems());
        seleccionadas.addAll(ListViewInactivas.getSelectionModel().getSelectedItems());

        for (Alerta a : seleccionadas) {
            controlador.eliminarAlerta(a);
        }
        cargarAlertas();
    }

    @FXML
    private void onActivarAlerta() {
        List<Alerta> seleccionadas = FXCollections.observableArrayList();
        seleccionadas.addAll(ListViewActivas.getSelectionModel().getSelectedItems());
        seleccionadas.addAll(ListViewInactivas.getSelectionModel().getSelectedItems());

        for (Alerta a : seleccionadas) {
            if (a.isActiva()) controlador.desactivarAlerta(a.getId());
            else controlador.activarAlerta(a.getId());
        }
        cargarAlertas();
    }
    

    @FXML
    private void onNuevaAlerta() throws IOException {
    	controlador.getSceneManager().abrirVentanaCrearAlerta();
    	cargarAlertas();
    }


    
}
