package umu.tds.gestion_gastos.alerta;

import java.util.List;

//Para descaopplarlo de la vista cuando creamos un tipo de alerta
public class AlertaStrategyFactory {

    public static List<String> getTiposDisponibles() {
        return List.of("Semanal", "Mensual");
    }

    public static AlertaStrategy crear(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "semanal" -> new AlertaSemanal();
            case "mensual" -> new AlertaMensual();
            case "anual" -> new AlertaAnual();
            default -> throw new IllegalArgumentException("Tipo de alerta no soportado: " + tipo);
        };
    }
}
