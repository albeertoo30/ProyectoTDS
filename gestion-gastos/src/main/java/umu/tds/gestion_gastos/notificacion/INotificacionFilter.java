package umu.tds.gestion_gastos.notificacion;

import java.util.function.Predicate;


public interface INotificacionFilter extends Predicate<Notificacion> {
    default INotificacionFilter and(INotificacionFilter other) {
        return n -> this.test(n) && other.test(n);
    }
    default INotificacionFilter or(INotificacionFilter other) {
        return n -> this.test(n) || other.test(n);
    }
    default INotificacionFilter negateFilter() {
        return n -> !this.test(n);
    }
    static INotificacionFilter alwaysTrue() {
        return n -> true;
    }
}
