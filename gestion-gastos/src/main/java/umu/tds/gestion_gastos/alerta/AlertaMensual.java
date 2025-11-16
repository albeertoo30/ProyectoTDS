package umu.tds.gestion_gastos.alerta;

import java.time.LocalDate;
import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public class AlertaMensual implements ComportamientoAlerta {

    @Override
    public boolean comprobar(List<Gasto> gastos, double limite) {
        LocalDate hoy = LocalDate.now();

        double totalMes = gastos.stream()
                .filter(g -> g.getFecha().getMonth() == hoy.getMonth())
                .mapToDouble(Gasto::getCantidad)
                .sum();

        return totalMes > limite;
    }

    @Override
    public String getMensaje() { //Esto tampoco se a que mensaje se refiere.
        return "Has superado el límite mensual de gasto.";
    }
}
