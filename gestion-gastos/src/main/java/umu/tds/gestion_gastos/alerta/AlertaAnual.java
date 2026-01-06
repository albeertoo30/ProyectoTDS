package umu.tds.gestion_gastos.alerta;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

import umu.tds.gestion_gastos.gasto.Gasto;

/**
 * Calcula si se supera el límite sumando gastos del MISMO AÑO que el nuevo gasto.
 */
@JsonTypeName("anual")
public class AlertaAnual implements AlertaStrategy {

    @Override
    public boolean seSupera(Alerta alerta, Gasto nuevoGasto, List<Gasto> todosGastos) {
        
        // PASO 1: Determinar el año del nuevo gasto
        int anioNuevoGasto = nuevoGasto.getFecha().getYear();
        
        // PASO 2: Sumar gastos del MISMO AÑO
        double totalAnio = todosGastos.stream()
            .filter(gasto -> {
            	// Filtro cuenta
            	if (!cumpleCuenta(gasto, alerta)) return false;
                // Filtro 1: Es del mismo año?
                boolean mismoAnio = gasto.getFecha().getYear() == anioNuevoGasto;
                
                if (!mismoAnio) {
                    return false;
                }
                
                // Filtro 2: Cumple con la categoría?
                return cumpleCategoria(gasto, alerta);
            })
            .mapToDouble(Gasto::getCantidad)
            .sum();
        
        // PASO 3: Añadir el nuevo gasto si no está en la lista
        if (!todosGastos.contains(nuevoGasto)) {
            totalAnio += nuevoGasto.getCantidad();
        }
        
        // PASO 4: Comparar con el límite
        return totalAnio > alerta.getLimite();
    }

	@Override
	public boolean haCambiadoPeriodo(LocalDate ultima, LocalDate actual) {
		 if (ultima == null) return true;
		    return ultima.getYear() != actual.getYear();	
	}
    
    

}