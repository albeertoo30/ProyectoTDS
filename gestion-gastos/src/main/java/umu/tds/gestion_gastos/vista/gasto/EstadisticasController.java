package umu.tds.gestion_gastos.vista.gasto;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import umu.tds.gestion_gastos.controlador.ControladorApp;
import umu.tds.gestion_gastos.gasto.Gasto;

public class EstadisticasController implements Initializable {

    @FXML private PieChart graficoQueso;
    @FXML private StackPane contenedorCalendario; // El contenedor vacío del FXML

    private ControladorApp controlador;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Inicialización básica si fuera necesaria
    }

    public void setControlador(ControladorApp controlador) {
        this.controlador = controlador;
        cargarDatos();
    }

    private void cargarDatos() {
        if (controlador == null) return;
        
        // Obtener solo mis gastos (igual que hiciste en la tabla)
        List<Gasto> misGastos = controlador.obtenerGastos().stream()
                .filter(g -> g.getUsuario() != null && g.getUsuario().equals(controlador.getUsuarioActual()))
                .collect(Collectors.toList());

        cargarGraficoQueso(misGastos);
        cargarCalendario(misGastos);
    }

    // --- 1. LÓGICA DEL GRÁFICO CIRCULAR ---
    private void cargarGraficoQueso(List<Gasto> gastos) {
        // Agrupar gastos por nombre de categoría y sumar cantidades
        Map<String, Double> gastosPorCategoria = gastos.stream()
            .collect(Collectors.groupingBy(
                g -> (g.getCategoria() != null) ? g.getCategoria().getNombre() : "Sin Categoría",
                Collectors.summingDouble(Gasto::getCantidad)
            ));

        // Convertir a datos para el PieChart
        ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
        gastosPorCategoria.forEach((nombreCat, total) -> {
            chartData.add(new PieChart.Data(nombreCat, total));
        });

        graficoQueso.setData(chartData);
    }

    // --- 2. LÓGICA DE CALENDAR FX ---
    private void cargarCalendario(List<Gasto> gastos) {
        Calendar calendarioGastos = new Calendar("Mis Gastos");
        calendarioGastos.setStyle(Style.STYLE1);
        calendarioGastos.setReadOnly(true);

        for (Gasto g : gastos) {
            String titulo = String.format("%.2f€ - %s", g.getCantidad(), g.getDescripcion());
            Entry<String> entrada = new Entry<>(titulo);
            
            // --- CORRECCIÓN ---
            // En lugar de calcular horas que pueden fallar, asignamos la fecha directamente.
            // Al pasar la misma fecha como inicio y fin, CalendarFX entiende el intervalo.
            entrada.setInterval(g.getFecha(), g.getFecha()); 
            
            entrada.setFullDay(true);
            entrada.setCalendar(calendarioGastos);
            calendarioGastos.addEntry(entrada);
        }

        // ... (el principio del método cargarCalendario sigue igual hasta crear 'miFuente') ...
        CalendarSource miFuente = new CalendarSource("Cartera");
        miFuente.getCalendars().add(calendarioGastos);

        // --- NUEVA CONFIGURACIÓN VISUAL LIMPIA ---

        // ... (parte inicial de cargarCalendario igual) ...

        CalendarView calendarView = new CalendarView();
        calendarView.getCalendarSources().setAll(miFuente);

        // 1. CONFIGURACIÓN VISUAL (Corregida)
        // En lugar de setShowHeader(false), desactivamos cada botón individualmente
        calendarView.setShowAddCalendarButton(false);
        calendarView.setShowPrintButton(false);
        calendarView.setShowPageToolBarControls(false); // Oculta botones de Día/Semana/Mes
        calendarView.setShowSearchField(false);         // Oculta la barra de búsqueda
        calendarView.setShowSourceTrayButton(false);    // Oculta el botón de "Fuentes"
        calendarView.setShowSourceTray(false);          // Oculta el panel lateral izquierdo
        
        // 2. FORZAR VISTA MENSUAL
        // Esta es la clave para que se vea limpio. 
        // La vista de mes es una cuadrícula perfecta para gastos de día completo.
        calendarView.showMonthPage();

        // 3. FIJAR FECHA ACTUAL
        calendarView.setRequestedTime(LocalTime.now());
        calendarView.setToday(LocalDate.now());

        // Añadir al contenedor
        contenedorCalendario.getChildren().clear();
        contenedorCalendario.getChildren().add(calendarView);
    }

    @FXML
    private void onCerrar() {
        Stage stage = (Stage) graficoQueso.getScene().getWindow();
        stage.close();
    }
}
