package umu.tds.gestion_gastos.alerta;

import java.time.LocalDate;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;
import umu.tds.gestion_gastos.notificacion.Notificacion;
import umu.tds.gestion_gastos.notificacion.NotificacionFilterBuilder;
import umu.tds.gestion_gastos.notificacion.NotificacionFilterFechaRange;

public class AlertaFilterBuilder {

	
    private Filtro<Alerta> filter = Filtro.alwaysTrue();

    public AlertaFilterBuilder limite(Double desde, Double hasta) {
        if (desde != null || hasta != null) {
            filter =filter.and(new AlertaFilterLimite(desde, hasta));
        }
        return this;
    }

    
    public AlertaFilterBuilder categoria(Categoria categoria) {
        if (categoria != null) {
            Filtro<Alerta> catFilter = n -> {
                Categoria nc = n.getCategoria();
                return nc != null && nc.getNombre().equals(categoria.getNombre());
            };
            filter = filter.and(catFilter);
        }
        return this;
    }

    public Filtro<Alerta> build() {
        return filter;
    }
	
}










