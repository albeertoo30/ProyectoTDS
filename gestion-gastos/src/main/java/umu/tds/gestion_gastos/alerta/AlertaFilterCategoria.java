package umu.tds.gestion_gastos.alerta;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;

public class AlertaFilterCategoria implements Filtro<Alerta>{

    private final Categoria categoria;
    
    public AlertaFilterCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public boolean test(Alerta n) {
        if (categoria == null) return true;
        
        Categoria catNotif = n.getCategoria();
        if (catNotif == null) return false;
        
        return catNotif.getNombre().equalsIgnoreCase(categoria.getNombre());
    }
}
