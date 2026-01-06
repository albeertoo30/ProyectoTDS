package umu.tds.gestion_gastos.vista.gasto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;
import umu.tds.gestion_gastos.vista.categoria.ControladorCategoriasView;
import umu.tds.gestion_gastos.vista.inicio.VentanaInicioController;

public class ControladorGastosView {

    @FXML private TableView<Gasto> tablaGastos;
    @FXML private TableColumn<Gasto, String> colFecha;
    @FXML private TableColumn<Gasto, String> colCategoria;
    @FXML private TableColumn<Gasto, Double> colCantidad;
    @FXML private TableColumn<Gasto, String> colDescripcion;
    @FXML private TableColumn<Gasto, String> colCuenta;
    @FXML private DatePicker filtroFecha;
    @FXML private ComboBox<Categoria> filtroCategoria;
    @FXML private TextField filtroMin;
    @FXML private TextField filtroMax;

    @FXML private Button btnInicio;
    @FXML private Button btnCategorias;
    @FXML private Button btnNotificaciones;
    @FXML private Button btnFiltrar;
    @FXML private Button btnLimpiarFiltros;
    @FXML private Button btnAnadir;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnEstadisticas;
    @FXML private Button btnAlertas;

    
    private final ObservableList<Gasto> gastosObservable = FXCollections.observableArrayList();
    private ControladorApp controlador;

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        
        // Cargar categorías en el filtro
        filtroCategoria.getItems().setAll(controlador.obtenerCategorias());
        filtroCategoria.setPromptText("Todas");
        
        refrescarTabla();
    }

    @FXML
    public void initialize() {
        tablaGastos.setItems(gastosObservable);

        colFecha.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getFecha().toString())
        );

        colCategoria.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                cellData.getValue().getCategoria() != null ?
                cellData.getValue().getCategoria().getNombre() : ""
            )
        );

        colCantidad.setCellValueFactory(cellData ->
            new SimpleObjectProperty<>(cellData.getValue().getCantidad())
        );

        colDescripcion.setCellValueFactory(cellData ->
            new SimpleStringProperty(cellData.getValue().getDescripcion())
        );
        
        colCuenta.setCellValueFactory(cellData -> {
            Gasto gasto = cellData.getValue();
            if (gasto.getCuenta() == null) {
                // Si la cuenta es null, asumimos que es un gasto INDIVIDUAL
                return new SimpleStringProperty("Individual");
            } else {
                // Si hay objeto cuenta, mostramos su nombre
                return new SimpleStringProperty(gasto.getCuenta().getNombre());
            }
        });
        
        filtroCategoria.setConverter(new StringConverter<Categoria>() {
            @Override
            public String toString(Categoria categoria) {
                return categoria == null ? "" : categoria.getNombre();
            }
            @Override
            public Categoria fromString(String string) {
                return null;
            }
        });
        
        //Se supone que es la cuenta individual.
        Configuracion.getInstancia().setCuentaActual("Cuenta Personal");
    }
    
    
    private void refrescarTabla() {
        if (controlador != null) {
        	Usuario actual = controlador.getUsuarioActual();
        	List<Gasto> misGastos = controlador.obtenerGastos().stream()
        			.filter(g -> g.getUsuario() != null && g.getUsuario().equals(actual))
        			.collect(Collectors.toList());
            gastosObservable.setAll(misGastos);
        }
    }

    // VOLVER A LA VENTANA DE INICIO
    @FXML
    private void onVolverInicio() {
        try {
            // Cargar la vista de Inicio
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/inicio/VentanaInicio.fxml"));
            Parent root = loader.load();

            // Inyectar el controlador principal a la ventana de inicio
            // (Para no perder la sesión del usuario ni los datos cargados)
            VentanaInicioController controladorInicio = loader.getController();
            controladorInicio.setControlador(this.controlador);

            // Crear y mostrar la ventana de Inicio
            Stage stageInicio = new Stage();
            stageInicio.setScene(new Scene(root, 1200, 700));
            stageInicio.setTitle("Gestor de Gastos");
            stageInicio.show();

            // Cerrar la ventana actual (GastosView)
            Stage stageActual = (Stage) btnCategorias.getScene().getWindow();
            stageActual.close();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("No se pudo volver a la pantalla de inicio.");
        }
    }
    
    // BOTON CATEGORIAS
    @FXML
    private void onIrACategorias() throws IOException {
        abrirVentana(
            "/umu/tds/gestion_gastos/categorias/CategoriaView.fxml",
            "Categorías"
        );
    }

    // BOTON NOTIFICACIONES
    @FXML
    private void onIrANotificaciones() throws IOException {
    	controlador.getSceneManager().abrirVentanaNotificaciones();
    }
    
    //BOTON ALERTAS
    
    @FXML
    private void onIrAAlertas() throws IOException {
        controlador.getSceneManager().abrirVentanaAlertas();
    }    
    
    // BOTÓN ESTADÍSTICAS
    @FXML
    private void onVerEstadisticas() {
        try {
            // 1. Cargar el archivo FXML de la ventana de estadísticas
            // Ajusta la ruta si decidiste guardarlo en otro paquete, 
            // pero basándonos en lo anterior debería ser esta:
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/umu/tds/gestion_gastos/gasto/Estadisticas.fxml"));
            Parent root = loader.load();

            // 2. Obtener el controlador de la nueva ventana
            EstadisticasController controller = loader.getController();
            
            // 3. Inyectar el ControladorApp
            // Esto es CRUCIAL para que la ventana pueda pedir los gastos y pintarlos
            controller.setControlador(this.controlador);

            // 4. Configurar y mostrar el Stage (Ventana)
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Mis Estadísticas");
            
            // APPLICATION_MODAL bloquea la ventana de gastos hasta que cierres las estadísticas
            stage.initModality(Modality.APPLICATION_MODAL); 
            
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Error al abrir las estadísticas: " + e.getMessage());
        }
    }

    // Método auxiliar para abrir ventanas
    private void abrirVentana(String rutaFXML, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
        Parent root = loader.load();
        
        // Inyectar el controlador si es necesario
        Object controller = loader.getController();
        
        // Si el controlador tiene método setControlador, inyectarlo
        if (controller instanceof ControladorCategoriasView) {
            ((ControladorCategoriasView) controller).setControlador(controlador);
        }
        // Agregar otros controladores según sea necesario
        
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
        
        // Refrescar tabla al cerrar (por si se modificaron categorías)
        refrescarTabla();
    }
    
    // BOTÓN AÑADIR
    @FXML
    private void onNuevoGasto() throws IOException {
        abrirFormulario(null);
    }

    // BOTÓN EDITAR
    @FXML
    private void onEditarGasto() throws IOException {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un gasto para editar.");
            return;
        }
        abrirFormulario(seleccionado);
    }

    // BOTÓN ELIMINAR
    @FXML
    private void onEliminarGasto() {
        Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarError("Seleccione un gasto para eliminar.");
            return;
        }

        if (confirmar("¿Seguro que desea eliminar este gasto?")) {
            controlador.eliminarGasto(seleccionado.getId());
            refrescarTabla();
        }
    }

    // BOTON FILTRAR
    @FXML
    private void onAplicarFiltros() {
        try {
            // Recoger valores de la vista
            LocalDate fecha = filtroFecha.getValue();
            Categoria categoria = filtroCategoria.getValue();
            String minStr = filtroMin.getText().trim();
            String maxStr = filtroMax.getText().trim();
            
            // Convertir a Double (null si vacío)
            Double min = minStr.isEmpty() ? null : Double.parseDouble(minStr);
            Double max = maxStr.isEmpty() ? null : Double.parseDouble(maxStr);
            
            Usuario actual = controlador.getUsuarioActual();
            
            // Delegar en ControladorApp
            List<Gasto> gastosFiltrados = controlador.obtenerGastosFiltrados(fecha, categoria, min, max);
            
            // Por si acaso
            List<Gasto> misGastos = gastosFiltrados.stream()
        			.filter(g -> g.getUsuario() != null && g.getUsuario().equals(actual))
        			.collect(Collectors.toList());
            
            // Actualizar vista
            gastosObservable.setAll(misGastos);
            
        } catch (NumberFormatException e) {
            mostrarError("Los valores de importe deben ser números válidos.");
        }
    }
    
    // BOTÓN LIMPIAR FILTROS
    @FXML
    private void onLimpiarFiltros() {
        filtroFecha.setValue(null);
        filtroCategoria.setValue(null);
        filtroMin.clear();
        filtroMax.clear();
        refrescarTabla();
    }
    
    // ABRIR FORMULARIO
    private void abrirFormulario(Gasto gastoAEditar) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/umu/tds/gestion_gastos/gasto/FormularioGasto.fxml"));

        Parent root = loader.load();

        FormularioGastoController controladorForm = loader.getController();
        
        // IMPORTANTE: Primero inyectar el controlador (esto carga las categorías)
        controladorForm.setControlador(controlador);
        
        // Luego configurar el gasto (si existe)
        if (gastoAEditar != null) {
            controladorForm.setGasto(gastoAEditar);
        }
        
        // Finalmente configurar el callback
        controladorForm.setOnSave(this::refrescarTabla);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle(gastoAEditar == null ? "Nuevo gasto" : "Editar gasto");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    // UTILS 
    private void mostrarError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
    }

    private boolean confirmar(String msg) {
        return new Alert(Alert.AlertType.CONFIRMATION, msg,
                ButtonType.OK, ButtonType.CANCEL)
                .showAndWait()
                .orElse(ButtonType.CANCEL) == ButtonType.OK;
    }
}
