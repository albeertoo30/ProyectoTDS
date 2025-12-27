package umu.tds.gestion_gastos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.cuenta.RepoCuenta;
import umu.tds.gestion_gastos.usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestVentanaCrearCuentaCompartida extends Application {

    private GestorCuenta gestor;
    private VBox containerMiembros;

    @Override
    public void start(Stage stage) {

        RepoCuenta repo = new RepoCuenta();
        this.gestor = new GestorCuenta(repo);


        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #f4f4f4;");

        Label lblTitulo = new Label("Crear Nueva Cuenta Compartida");
        lblTitulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        TextField txtNombreCuenta = new TextField();
        txtNombreCuenta.setPromptText("Ej: Viaje a Roma, Piso Alquiler...");


        Label lblMiembros = new Label("Miembros del grupo:");
        containerMiembros = new VBox(10);

        agregarFilaMiembro();
        agregarFilaMiembro();

        Button btnAddMiembro = new Button("+ Añadir otro miembro");
        btnAddMiembro.setOnAction(e -> agregarFilaMiembro());


        Button btnGuardar = new Button("CREAR CUENTA");
        btnGuardar.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
        btnGuardar.setMaxWidth(Double.MAX_VALUE);
        

        btnGuardar.setOnAction(e -> guardarCuenta(txtNombreCuenta.getText()));

        root.getChildren().addAll(
            lblTitulo, 
            new Label("Nombre de la Cuenta:"), 
            txtNombreCuenta, 
            new Separator(),
            lblMiembros,
            containerMiembros,
            btnAddMiembro,
            new Separator(),
            btnGuardar
        );

        Scene scene = new Scene(root, 400, 600);
        stage.setTitle("Formulario Cuenta - TDS");
        stage.setScene(scene);
        stage.show();
    }


    private void agregarFilaMiembro() {
        HBox fila = new HBox(10);
        
        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre Usuario");
        HBox.setHgrow(txtNombre, Priority.ALWAYS);

        TextField txtPorcentaje = new TextField();
        txtPorcentaje.setPromptText("% (Opcional)");
        txtPorcentaje.setPrefWidth(100);

        Button btnEliminar = new Button("X");
        btnEliminar.setStyle("-fx-text-fill: red;");
        btnEliminar.setOnAction(e -> containerMiembros.getChildren().remove(fila));

        fila.getChildren().addAll(txtNombre, txtPorcentaje, btnEliminar);
        containerMiembros.getChildren().add(fila);
    }


    private void guardarCuenta(String nombreCuenta) {
        if (nombreCuenta.isEmpty()) {
            mostrarAlerta("Error", "Debes poner un nombre a la cuenta.");
            return;
        }

        List<Usuario> usuariosTemp = new ArrayList<>();
        Map<Usuario, Double> mapaPorcentajes = new HashMap<>();
        double sumaPorcentajes = 0.0;
        boolean hayPorcentajes = false;


        for (Node node : containerMiembros.getChildren()) {
            if (node instanceof HBox) {
                HBox fila = (HBox) node;
                TextField txtUser = (TextField) fila.getChildren().get(0);
                TextField txtPerc = (TextField) fila.getChildren().get(1);

                String nombreUser = txtUser.getText().trim();
                String strPorc = txtPerc.getText().trim();

                if (nombreUser.isEmpty()) continue; 


                Usuario u = new Usuario((int)Math.floor(Math.random() * 100000), nombreUser);
                usuariosTemp.add(u);

                if (!strPorc.isEmpty()) {
                    try {
                        double val = Double.parseDouble(strPorc);
                        mapaPorcentajes.put(u, val);
                        sumaPorcentajes += val;
                        hayPorcentajes = true;
                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error", "El porcentaje de " + nombreUser + " no es un número válido.");
                        return;
                    }
                }
            }
        }

        if (usuariosTemp.isEmpty()) {
            mostrarAlerta("Error", "La cuenta debe tener al menos un miembro.");
            return;
        }


        if (hayPorcentajes) {

            if (Math.abs(sumaPorcentajes - 100.0) > 0.1) {
                mostrarAlerta("Error", "Los porcentajes suman " + sumaPorcentajes + "%. Deben sumar 100%.");
                return;
            }
            if (mapaPorcentajes.size() != usuariosTemp.size()) {
                mostrarAlerta("Error", "Si usas porcentajes, debes asignarlos a todos los miembros.");
                return;
            }
        }

        int idGenerado = (int) (Math.random() * 1000); 
        Cuenta nuevaCuenta = new Cuenta(idGenerado, nombreCuenta, usuariosTemp);
        
        if (hayPorcentajes) {
             nuevaCuenta.setPorcentajes(mapaPorcentajes); 
        }

        gestor.registrarCuenta(nuevaCuenta);

        mostrarAlerta("Éxito", "Cuenta '" + nombreCuenta + "' creada correctamente con " + usuariosTemp.size() + " miembros.");
        System.out.println("DEBUG: Cuenta creada -> " + nuevaCuenta.getNombre());
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}