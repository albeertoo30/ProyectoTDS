package umu.tds.gestion_gastos.alerta;

import java.util.Objects;

import umu.tds.gestion_gastos.filtros.Filtro;

public class AlertaFilterLimite implements Filtro<Alerta>{

    private final Double limiteMin; 
    private final Double limiteMax; 

    public AlertaFilterLimite(Double limiteMin, Double limiteMax) {
        this.limiteMin = limiteMin;
        this.limiteMax = limiteMax;
    }

    @Override
    public boolean test(Alerta a) {
        Objects.requireNonNull(a);
        double l = a.getLimite();
        if (limiteMin != null && l < limiteMin) return false;
        if (limiteMax != null && l > limiteMax) return false;
        return true;
    }
}
