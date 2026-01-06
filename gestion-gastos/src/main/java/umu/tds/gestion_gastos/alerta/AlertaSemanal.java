package umu.tds.gestion_gastos.alerta;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import com.fasterxml.jackson.annotation.JsonTypeName;
import umu.tds.gestion_gastos.gasto.Gasto;


@JsonTypeName("semanal")
public class AlertaSemanal implements AlertaStrategy{
	
    @Override
    public boolean seSupera(Alerta alerta, Gasto nuevoGasto, List<Gasto> todosGastos) {
        
        // PASO 1: Determinar la semana del nuevo gasto
        LocalDate fechaNuevoGasto = nuevoGasto.getFecha();
        WeekFields wf = WeekFields.of(Locale.getDefault());
        int semanaNuevoGasto = fechaNuevoGasto.get(wf.weekOfWeekBasedYear());
        int anioNuevoGasto = fechaNuevoGasto.getYear();
        
        // PASO 2: Sumar gastos de la MISMA SEMANA
        // Filtrar por semana Y por categoría (si la alerta tiene categoría específica)
        double totalSemana = todosGastos.stream()
            .filter(gasto -> {
            	// Filtro de cuenta
            	if (!cumpleCuenta(gasto, alerta)) return false;
                // Filtro 1: para la semana
                LocalDate fechaGasto = gasto.getFecha();
                boolean mismaSemana = fechaGasto.getYear() == anioNuevoGasto && 
                                     fechaGasto.get(wf.weekOfWeekBasedYear()) == semanaNuevoGasto;
                
                if (!mismaSemana) {
                    return false; // No es de esta semana, no lo contamos
                }
                
                // Filtro 2: para ver si es de la misma categoria (null == todas)
                return cumpleCategoria(gasto, alerta);
            })
            .mapToDouble(Gasto::getCantidad)
            .sum();
        
        // PASO 3: Añadir el nuevo gasto si no está ya en la lista
        if (!todosGastos.contains(nuevoGasto)) {
            totalSemana += nuevoGasto.getCantidad();
        }
        
        // PASO 4: Comparar con el límite
        return totalSemana > alerta.getLimite();
    }

    //Se puede aplicar plantilla pero por compatibilidad lo dejamos
    @Override
    public boolean haCambiadoPeriodo(LocalDate ultima, LocalDate actual) {
    	if(ultima == null) return true;
        WeekFields wf = WeekFields.of(Locale.getDefault());
        return ultima.getYear() != actual.getYear() ||
               ultima.get(wf.weekOfWeekBasedYear()) != actual.get(wf.weekOfWeekBasedYear());
    }
}
