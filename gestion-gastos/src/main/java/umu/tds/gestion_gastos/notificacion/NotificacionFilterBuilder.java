package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;

import umu.tds.gestion_gastos.alerta.AlertaFilterBuilder;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;

public class NotificacionFilterBuilder {

    private Filtro<Notificacion> filter = Filtro.alwaysTrue();

    public NotificacionFilterBuilder fecha(LocalDate desde, LocalDate hasta) {
        if (desde != null || hasta != null) {
            filter =filter.and(new NotificacionFilterFechaRange(desde, hasta));
        }
        return this;
    }

    public NotificacionFilterBuilder categoria(Categoria categoria) {
        if (categoria != null) {
            Filtro<Notificacion> catFilter = n -> {
                Categoria nc = n.getCategoria();
                return nc != null && nc.getNombre().equals(categoria.getNombre());
            };
            filter = filter.and(catFilter);
        }
        return this;
    }

    public NotificacionFilterBuilder leida(Boolean leida) {
        if (leida != null) {
            Filtro<Notificacion> lFilter = n -> n.isLeida() == leida;
            filter = filter.and(lFilter);
        }
        return this;
    }

    public NotificacionFilterBuilder cuenta(String cuenta) {
        if (cuenta != null) {
            filter = filter.and(a -> cuenta.equals(a.getIdCuenta()));
        }
        return this;
    }

    public Filtro<Notificacion> build() {
        return filter;
    }
}