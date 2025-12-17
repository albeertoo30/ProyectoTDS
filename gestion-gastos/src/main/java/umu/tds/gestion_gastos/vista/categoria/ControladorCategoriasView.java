package umu.tds.gestion_gastos.vista.categoria;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class ControladorCategoriasView {

    @FXML
    private Button btnAñadir;

    @FXML
    private Button btnEditar;

    @FXML
    private Button btnEliminar;

    @FXML
    private TableColumn<?, ?> colDescripcion;

    @FXML
    private TableColumn<?, ?> colNombre;

    @FXML
    private TableView<?> tablaCategorias;

}
