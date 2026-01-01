package umu.tds.gestion_gastos.filtros;

import java.util.function.Predicate;

public interface Filtro<T> extends Predicate<T> {

    default Filtro<T> and(Filtro<T> other) {
        return n -> this.test(n) && other.test(n);
    }
    default Filtro<T> or(Filtro<T> other) {
        return n -> this.test(n) || other.test(n);
    }
    default Filtro<T> negateFilter() {
        return n -> !this.test(n);
    }
    
    static <T> Filtro<T> alwaysTrue() {
        return n -> true;
    }
	
}
