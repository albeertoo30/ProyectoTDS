package umu.tds.gestion_gastos.alerta;

public interface ComportamientoAlerta {
    boolean comprobar(List<Gasto> gastos, double limite);
    string getMensaje();
}
