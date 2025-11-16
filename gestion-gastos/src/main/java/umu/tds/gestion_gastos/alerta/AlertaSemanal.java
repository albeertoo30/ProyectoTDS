package umu.tds.gestion_gastos.alerta;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public class AlertaSemanal implements ComportamientoAlerta {

    @Override
    public boolean comprobar(List<Gasto> gastos, double limite) {
        LocalDate hoy = LocalDate.now();
        WeekFields wf = WeekFields.ISO;

        double totalSemana = gastos.stream()
                .filter(g -> g.getFecha().get(wf.weekOfWeekBasedYear()) == hoy.get(wf.weekOfWeekBasedYear()))
                .mapToDouble(Gasto::getCantidad)
                .sum();

        return totalSemana > limite;
    }

    @Override
    public String getMensaje() {   //Esto no se a que mensaje se refiere.
        return "Has superado el límite semanal de gasto.";
    }
}
