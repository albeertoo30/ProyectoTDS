package umu.tds.gestion_gastos.sceneManager;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.alerta.controlador.ControladorVentanaAlerta;
import umu.tds.gestion_gastos.notificacion.controlador.ControladorVentanaNotificacion;
import umu.tds.gestion_gastos.vista.cuenta.ListaCuentasViewController;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public enum SceneManager {

    INSTANCE;

    private ControladorApp controlador;

    public void init(ControladorApp controlador) {
        this.controlador = controlador;
    }

    
    
    //abrir Alertas
    
    public void abrirVentanaAlertas() throws IOException {
        FXMLLoader loader = new FXMLLoader(
        	SceneManager.class.getResource("/umu/tds/gestion_gastos/alerta/VentanaAlertas.fxml")
        );

        Parent root = loader.load();
        ControladorVentanaAlerta vc = loader.getController();
        vc.setControlador(controlador);

        Stage stage = new Stage();
        stage.setTitle("Alertas");
        stage.setScene(new Scene(root));

        stage.setOnCloseRequest(e -> {
            try {
                controlador.guardarAlertas();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        stage.showAndWait();
    }

    
    
    
    //Abrir notificaciones
    
    public void abrirVentanaNotificaciones() throws IOException {
        FXMLLoader loader = new FXMLLoader(
        	SceneManager.class.getResource("/umu/tds/gestion_gastos/notificacion/VentanaNotificacion.fxml")
        );

        Parent root = loader.load();
        ControladorVentanaNotificacion vc = loader.getController();
        vc.setControlador(controlador);

        Stage stage = new Stage();
        stage.setTitle("Notificaciones");
        stage.setScene(new Scene(root));

        stage.setOnCloseRequest(e -> {
            try {
                controlador.guardarNotificaciones();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        stage.showAndWait();
    }
    
    
    public void abrirVentanaCuentasCompartidas() throws IOException {
    	
        FXMLLoader loader = new FXMLLoader(
            	SceneManager.class.getResource("/umu/tds/gestion_gastos/cuentas/ListaCuentasView.fxml")
            );

            Parent root = loader.load();
            ListaCuentasViewController vc = loader.getController();
            vc.setControlador(controlador);

            Stage stage = new Stage();
            stage.setTitle("Notificaciones");
            stage.setScene(new Scene(root));

            stage.setOnCloseRequest(e -> {
                try {
                    controlador.guardarNotificaciones();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            stage.showAndWait();
        }
    
}
