package umu.tds.gestion_gastos.vista.cuenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.cuenta.CuentaCompartida;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;
import umu.tds.gestion_gastos.vista.alerta.ControladorCrearAlerta;
import umu.tds.gestion_gastos.vista.alerta.ControladorVentanaAlerta;
import umu.tds.gestion_gastos.vista.notificacion.ControladorVentanaNotificacion;

public class DetalleCuentaController {

	@FXML
	private Label lblNombreCuenta;
	@FXML
	private ListView<Usuario> listaMiembros;

	@FXML
	private TableView<Gasto> tablaGastos;
	@FXML
	private TableColumn<Gasto, String> colFecha;
	@FXML
	private TableColumn<Gasto, String> colConcepto;
	@FXML
	private TableColumn<Gasto, String> colPagador;
	@FXML
	private TableColumn<Gasto, String> colImporte;

	private ControladorApp controlador;
	private CuentaCompartida cuentaActual;
	private Map<Usuario, Double> balancesCalculados = new HashMap<>();

    public void setCuenta(CuentaCompartida cuenta) {
        this.cuentaActual = cuenta;

        //Para filtrar alertas y notificaciones segun la cuenta actual. Nombre porque el ID esta raro
        Configuracion.getInstancia().setCuentaActual(cuentaActual.getNombre());
        actualizarVista();
    }

	public void setControlador(ControladorApp controlador) {
		this.controlador = controlador;
	}


	@FXML
	private void initialize() {
		colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));
		colConcepto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescripcion()));
		colImporte.setCellValueFactory(
				cell -> new SimpleStringProperty(String.format("%.2f €", cell.getValue().getCantidad())));
		colPagador.setCellValueFactory(cell -> {
			Usuario u = cell.getValue().getUsuario();
			return new SimpleStringProperty(u != null ? u.getNombre() : "Desconocido");
		});

		listaMiembros.setCellFactory(param -> new ListCell<Usuario>() {
			@Override
			protected void updateItem(Usuario item, boolean empty) {
				super.updateItem(item, empty);

				if (empty || item == null) {
					setText(null);
					setGraphic(null);
					setStyle("");
				} else {
					double saldo = balancesCalculados.getOrDefault(item, 0.0);
					String textoExtra = "";
					String estilo = "";

					if (saldo > 0.01) {
						textoExtra = String.format(" (+%.2f €)", saldo);
						estilo = "-fx-text-fill: #28a745; -fx-font-weight: bold;";
					} else if (saldo < -0.01) {
						textoExtra = String.format(" (%.2f €)", saldo);
						estilo = "-fx-text-fill: #dc3545; -fx-font-weight: bold;";
					} else {
						estilo = "-fx-text-fill: black;";
					}

					setText(item.getNombre() + textoExtra);
					setStyle(estilo);
				}
			}
		});

		listaMiembros.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				filtrarGastosPorUsuario(newVal);
			} else {
				tablaGastos.getItems().setAll(cuentaActual.getGastos());
			}
		});
	}


	private void recalcularSaldos() {
		if (cuentaActual == null)
			return;
		this.balancesCalculados = controlador.calcularSaldos(cuentaActual.getId());
	}
	private void filtrarGastosPorUsuario(Usuario u) {
		List<Gasto> filtrados = cuentaActual.getGastos().stream()
				.filter(g -> g.getUsuario() != null && g.getUsuario().equals(u)).collect(Collectors.toList());
		tablaGastos.getItems().setAll(filtrados);

		// Para filtrar alertas y notificaciones segun la cuenta actual. Nombre porque
		// el ID esta raro
		Configuracion.getInstancia().setCuentaActual(cuentaActual.getNombre());

	}

	private void actualizarVista() {
		if (cuentaActual == null)
			return;

		lblNombreCuenta.setText(cuentaActual.getNombre());

		recalcularSaldos();

		listaMiembros.getItems().setAll(cuentaActual.getMiembros());
		listaMiembros.refresh();

		tablaGastos.getItems().setAll(cuentaActual.getGastos());
		tablaGastos.refresh();
	}

	@FXML
	private void onNuevoGasto() {
		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/umu/tds/gestion_gastos/cuentas/FormularioGastoCompartido.fxml"));
			javafx.scene.Parent root = loader.load();
			FormularioGastoCompartidoController formController = loader.getController();

			formController.initData(cuentaActual.getMiembros(), controlador.obtenerCategorias());

			Stage stage = new Stage();
			stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			stage.setTitle("Nuevo Gasto - " + cuentaActual.getNombre());
			stage.setScene(new javafx.scene.Scene(root));
			stage.showAndWait();

			if (formController.isGuardado()) {
				Gasto nuevoGasto = formController.getGastoCreado();
				nuevoGasto.setCuenta(this.cuentaActual);
				this.cuentaActual.agregarGasto(nuevoGasto);

				controlador.registrarCuenta(this.cuentaActual);
				controlador.procesarNuevoGasto(nuevoGasto);
				actualizarVista();
			}

		} catch (Exception e) {
			e.printStackTrace();
			mostrarError("Error al abrir formulario: " + e.getMessage());
		}
	}

	@FXML
	private void onEditarGasto() {
		Gasto seleccionado = tablaGastos.getSelectionModel().getSelectedItem();
		if (seleccionado == null) {
			mostrarError("Selecciona un gasto para editar.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/umu/tds/gestion_gastos/cuentas/FormularioGastoCompartido.fxml"));
			javafx.scene.Parent root = loader.load();

			FormularioGastoCompartidoController formController = loader.getController();

			formController.initData(cuentaActual.getMiembros(), controlador.obtenerCategorias());
			formController.setGastoEditar(seleccionado);

			Stage stage = new Stage();
			stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
			stage.setTitle("Editar Gasto");
			stage.setScene(new javafx.scene.Scene(root));
			stage.showAndWait();

			if (formController.isGuardado()) {
				controlador.registrarCuenta(this.cuentaActual);
				controlador.procesarNuevoGasto(seleccionado);
				actualizarVista();
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

	private void mostrarError(String msg) {
		new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK).showAndWait();
	}

	@FXML
	private void onLimpiarFiltro() {
		listaMiembros.getSelectionModel().clearSelection();
		tablaGastos.getItems().setAll(cuentaActual.getGastos());
	}

	@FXML
	private void onVerEstadisticas() {
		if (cuentaActual.getGastos().isEmpty()) {
			mostrarError("No hay datos para mostrar gráficas.");
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/umu/tds/gestion_gastos/cuentas/EstadisticasView.fxml"));
			javafx.scene.Parent root = loader.load();

			EstadisticasController statsController = loader.getController();

			// pasamos datos al controaldor de estadidsticas
			statsController.initData(cuentaActual.getNombre(), cuentaActual.getGastos());

			Stage stage = new Stage();
			stage.setTitle("Estadísticas - " + cuentaActual.getNombre());
			stage.setScene(new javafx.scene.Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			mostrarError("Error al abrir estadísticas.");
		}
	}

	@FXML
	private void onAlertas() throws IOException {
		controlador.getSceneManager().abrirVentanaAlertas();
	}

	@FXML
	private void onNotificaciones() throws IOException {
		controlador.getSceneManager().abrirVentanaNotificaciones();
	}

}