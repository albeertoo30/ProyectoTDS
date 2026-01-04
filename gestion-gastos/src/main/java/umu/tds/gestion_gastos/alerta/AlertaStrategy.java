package umu.tds.gestion_gastos.alerta;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import umu.tds.gestion_gastos.gasto.Gasto;

/**
 * Calcula si se supera el límite de la alerta con el nuevo gasto.
 * 
 * RESPONSABILIDADES de la estrategia:
 * 1. Filtrar gastos del periodo correspondiente (semana/mes/año del nuevoGasto)
 * 2. Filtrar por categoría SI la alerta tiene una categoría específica
 * 3. Sumar los importes
 * 4. Comparar con el límite de la alerta
 * 
 * @param alerta La alerta que contiene límite y categoría
 * @param nuevoGasto El gasto que acaba de añadirse
 * @param todosGastos Lista completa de gastos del sistema
 * @return true si el total del periodo supera el límite
 */
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.PROPERTY, 
	    property = "type"
	)
	@JsonSubTypes({
	    @JsonSubTypes.Type(value = AlertaSemanal.class, name = "semanal"),
	    @JsonSubTypes.Type(value = AlertaMensual.class, name = "mensual"),
	    @JsonSubTypes.Type(value = AlertaMensual.class, name = "anual")   })
public interface AlertaStrategy {

	boolean seSupera(Alerta alerta, Gasto nuevoGasto, List<Gasto> todosGastos);

	
    /**
     * MÉTODO AUXILIAR: Verifica si un gasto cumple con la categoría de la alerta
     * - Si alerta.categoria == null -> acepta TODOS los gastos
     * - Si alerta.categoria != null ->  solo acepta gastos de esa categoría
     */

    default boolean cumpleCategoria(Gasto gasto, Alerta alerta) {
        if (alerta.getCategoria() == null) {
            return true;
        }
        
        if (gasto.getCategoria() == null) {
            return false;
        }
        
        return gasto.getCategoria().getNombre()
                   .equalsIgnoreCase(alerta.getCategoria().getNombre());
    }

}
