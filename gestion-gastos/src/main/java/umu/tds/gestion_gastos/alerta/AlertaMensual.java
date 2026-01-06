package umu.tds.gestion_gastos.alerta;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeName;
import umu.tds.gestion_gastos.gasto.Gasto;

@JsonTypeName("mensual")
public class AlertaMensual implements AlertaStrategy{
	
	@Override 
	public boolean seSupera(Alerta alerta, Gasto nuevoGasto, List<Gasto> todosGastos) {
	
		// PASO 1: Determinar el mes del nuevo gasto
        // DECISIÓN: Usar YearMonth para comparaciones más sencillas
        // ¿Por qué? YearMonth.equals() compara año y mes automáticamente
        LocalDate fechaNuevoGasto = nuevoGasto.getFecha();
        YearMonth mesNuevoGasto = YearMonth.from(fechaNuevoGasto);
        
        // PASO 2: Sumar gastos del MISMO MES
        double totalMes = todosGastos.stream()
            .filter(gasto -> {
            	// Filtro cuenta
            	if (!cumpleCuenta(gasto, alerta)) return false;
                // Filtro 1: Es del mismo mes?
                YearMonth mesGasto = YearMonth.from(gasto.getFecha());
                boolean mismoMes = mesGasto.equals(mesNuevoGasto);
                
                if (!mismoMes) {
                    return false;
                }
                
                // Filtro 2: Cumple con la categoría?
                return cumpleCategoria(gasto, alerta);
            })
            .mapToDouble(Gasto::getCantidad)
            .sum();
        
        // PASO 3: Añadir el nuevo gasto si no está en la lista
        if (!todosGastos.contains(nuevoGasto)) {
            totalMes += nuevoGasto.getCantidad();
        }
        
        // PASO 4: Comparar con el límite
        return totalMes > alerta.getLimite();
    }
	
	@Override
    public boolean haCambiadoPeriodo(LocalDate ultima, LocalDate actual) {
    	if(ultima == null) return true;
    	return ultima.getYear() != actual.getYear()
    	        || ultima.getMonth() != actual.getMonth();
	}
	
}




