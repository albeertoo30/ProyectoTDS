package umu.tds.gestion_gastos.alerta;

import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public interface ComportamientoAlerta {
    boolean comprobar(List<Gasto> gastos, double limite); 
    String getMensaje();
}
