package umu.tds.gestion_gastos;

import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.time.LocalDate;

public class TestVistaDetalleCuenta extends Application {

    @Override
    public void start(Stage stage) {
        String nombreCuenta = "Viaje Fin de Curso - Canarias";
        ObservableList<GastoVisual> listaGastos = getGastosFalsos();

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: white;");

        HBox header = new HBox(15);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: #2c3e50;"); 
        header.setAlignment(Pos.CENTER_LEFT);

        Button btnAtras = new Button("🡠 Volver");
        btnAtras.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-border-color: white; -fx-border-radius: 5;");
        
        Label lblTitulo = new Label(nombreCuenta);
        lblTitulo.setTextFill(Color.WHITE);
        lblTitulo.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));

        header.getChildren().addAll(btnAtras, lblTitulo);
        root.setTop(header);

        VBox centro = new VBox(10);
        centro.setPadding(new Insets(20));
        
        Label lblHistorial = new Label("Historial de Gastos");
        lblHistorial.setFont(Font.font("System", FontWeight.BOLD, 16));

        TableView<GastoVisual> tabla = new TableView<>();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<GastoVisual, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha()));
        
        TableColumn<GastoVisual, String> colConcepto = new TableColumn<>("Concepto");
        colConcepto.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getConcepto()));
        
        TableColumn<GastoVisual, String> colPagador = new TableColumn<>("Pagado por");
        colPagador.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getPagador()));

        TableColumn<GastoVisual, Double> colImporte = new TableColumn<>("Importe (€)");
        colImporte.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getImporte()).asObject());
        colImporte.setStyle("-fx-alignment: CENTER-RIGHT;"); 

        tabla.getColumns().addAll(colFecha, colConcepto, colPagador, colImporte);
        tabla.setItems(listaGastos);

        centro.getChildren().addAll(lblHistorial, tabla);
        root.setCenter(centro);

        VBox panelDerecho = new VBox(15);
        panelDerecho.setPadding(new Insets(20));
        panelDerecho.setPrefWidth(250);
        panelDerecho.setStyle("-fx-background-color: #f8f9fa; -fx-border-color: #dee2e6; -fx-border-width: 0 0 0 1;");

        Label lblBalance = new Label("Balance del Grupo");
        lblBalance.setFont(Font.font("System", FontWeight.BOLD, 16));
        
        VBox listaSaldos = new VBox(10);
        listaSaldos.getChildren().add(crearFilaSaldo("Yo", 150.50));  
        listaSaldos.getChildren().add(crearFilaSaldo("María", -45.20)); 
        listaSaldos.getChildren().add(crearFilaSaldo("Pedro", -105.30)); 

        Button btnAnadirGasto = new Button("+ Añadir Gasto");
        btnAnadirGasto.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        btnAnadirGasto.setMaxWidth(Double.MAX_VALUE);
        btnAnadirGasto.setPrefHeight(40);

        Button btnLiquidar = new Button("Liquidar Deudas");
        btnLiquidar.setMaxWidth(Double.MAX_VALUE);

        panelDerecho.getChildren().addAll(lblBalance, new Separator(), listaSaldos, new Separator(), btnAnadirGasto, btnLiquidar);
        root.setRight(panelDerecho);

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Detalle Cuenta - Prototipo");
        stage.setScene(scene);
        stage.show();
    }

    private HBox crearFilaSaldo(String nombre, double saldo) {
        HBox fila = new HBox(10);
        fila.setAlignment(Pos.CENTER_LEFT);
        
        boolean positivo = saldo >= 0;
        Circle icono = new Circle(5, positivo ? Color.GREEN : Color.RED);
        
        VBox info = new VBox(2);
        Label lblNombre = new Label(nombre);
        lblNombre.setStyle("-fx-font-weight: bold;");
        
        String textoSaldo = (positivo ? "+" : "") + String.format("%.2f €", saldo);
        Label lblDinero = new Label(textoSaldo);
        lblDinero.setTextFill(positivo ? Color.GREEN : Color.RED);
        
        info.getChildren().addAll(lblNombre, lblDinero);
        
        fila.getChildren().addAll(icono, info);
        return fila;
    }

    private ObservableList<GastoVisual> getGastosFalsos() {
        ObservableList<GastoVisual> lista = FXCollections.observableArrayList();
        lista.add(new GastoVisual("2025-01-02", "Billetes de Avión", "Yo", 450.00));
        lista.add(new GastoVisual("2025-01-03", "Taxi al Hotel", "María", 35.50));
        lista.add(new GastoVisual("2025-01-04", "Cena Bienvenida", "Pedro", 120.00));
        lista.add(new GastoVisual("2025-01-05", "Excursión Volcán", "Yo", 80.00));
        lista.add(new GastoVisual("2025-01-06", "Compra Supermercado", "María", 65.20));
        return lista;
    }

    public static class GastoVisual {
        private final String fecha;
        private final String concepto;
        private final String pagador;
        private final double importe;

        public GastoVisual(String fecha, String concepto, String pagador, double importe) {
            this.fecha = fecha;
            this.concepto = concepto;
            this.pagador = pagador;
            this.importe = importe;
        }

        public String getFecha() { return fecha; }
        public String getConcepto() { return concepto; }
        public String getPagador() { return pagador; }
        public double getImporte() { return importe; }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}