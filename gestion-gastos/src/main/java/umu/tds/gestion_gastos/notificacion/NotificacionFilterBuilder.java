package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;
import java.util.Objects;

import umu.tds.gestion_gastos.categoria.Categoria;

public class NotificacionFilterBuilder {
    private INotificacionFilter filter = INotificacionFilter.alwaysTrue();

    public NotificacionFilterBuilder fecha(LocalDate desde, LocalDate hasta) {
        filter = filter.and(new NotificacionFilterFechaRange(desde, hasta));
        return this;
    }

    public NotificacionFilterBuilder categoria(Categoria categoria) {
        INotificacionFilter catFilter = n -> {
            if (categoria == null) return true;
            Categoria nc = n.getCategoria();
            return nc != null && nc.equals(categoria);
        };
        filter = filter.and(catFilter);
        return this;
    }

    public NotificacionFilterBuilder leida(Boolean leida) {
        INotificacionFilter lFilter = n -> leida == null || n.isLeida() == leida;
        filter = filter.and(lFilter);
        return this;
    }

    public INotificacionFilter build() {
        return filter;
    }
}
