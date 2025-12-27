package umu.tds.gestion_gastos;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.cuenta.RepoCuenta;
import umu.tds.gestion_gastos.usuario.Usuario;

import java.util.ArrayList;
import java.util.List;

public class TestCuentaUsuarios extends Application {

    private GestorCuenta gestor;

    @Override
    public void start(Stage primaryStage) {
        

        RepoCuenta repo = new RepoCuenta();
        this.gestor = new GestorCuenta(repo);
        cargarDatosDePrueba(); 


        VBox root = new VBox(10);
        root.setStyle("-fx-padding: 20px;");

        Label titulo = new Label("Prueba de Integración: Cuentas");
        

        ListView<String> listaVisual = new ListView<>();
        

        List<Cuenta> misCuentas = gestor.getAll();
        

        ObservableList<String> items = FXCollections.observableArrayList();
        for (Cuenta c : misCuentas) {
            items.add(c.getId() + " - " + c.getNombre() + " (" + c.getMiembros().size() + " miembros)");
        }
        
        listaVisual.setItems(items);

        root.getChildren().addAll(titulo, listaVisual);
        
        Scene scene = new Scene(root, 400, 300);
        primaryStage.setTitle("Gestión de Gastos - TDS 2025");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }

    private void cargarDatosDePrueba() {
        Usuario u1 = new Usuario(1,"Pepe");
        Usuario u2 = new Usuario(2,"Maria");
        
        List<Usuario> grupo = new ArrayList<>();
        grupo.add(u1);
        grupo.add(u2);

        Cuenta c1 = new Cuenta(1, "Piso Estudiantes", grupo);
        Cuenta c2 = new Cuenta(2, "Regalo Madre", grupo);
        
        gestor.registrarCuenta(c1);
        gestor.registrarCuenta(c2);
    }
}