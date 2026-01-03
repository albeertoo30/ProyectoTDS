package umu.tds.gestion_gastos.vista.cuenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class EstadisticasController {

    @FXML private Label lblTitulo;
    @FXML private PieChart graficoQueso;
    @FXML private BarChart<String, Number> graficoBarras;

    public void initData(String nombreCuenta, List<Gasto> gastos) {
        lblTitulo.setText("Estadísticas: " + nombreCuenta);
        cargarDatos(gastos);
    }

    private void cargarDatos(List<Gasto> gastos) {

        Map<String, Double> porCategoria = new HashMap<>();
        Map<String, Double> porUsuario = new HashMap<>();

        //obtener datos
        for (Gasto g : gastos) {
            // Categoría
            String catNombre = g.getCategoria().getNombre();
            porCategoria.merge(catNombre, g.getCantidad(), Double::sum);

            // Usuario (Pagador)
            Usuario u = g.getUsuario();
            String userNombre = (u != null) ? u.getNombre() : "Desconocido";
            porUsuario.merge(userNombre, g.getCantidad(), Double::sum);
        }

        // llenar grafico queso
        graficoQueso.getData().clear();
        for (Map.Entry<String, Double> entry : porCategoria.entrySet()) {
            graficoQueso.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        // llenar grafico barras
        graficoBarras.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Gasto Total");

        for (Map.Entry<String, Double> entry : porUsuario.entrySet()) {
            serie.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        
        graficoBarras.getData().add(serie);
    }

    @FXML
    private void onCerrar() {
        Stage stage = (Stage) lblTitulo.getScene().getWindow();
        stage.close();
    }
}