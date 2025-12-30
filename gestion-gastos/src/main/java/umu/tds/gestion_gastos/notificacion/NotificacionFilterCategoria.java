package umu.tds.gestion_gastos.notificacion;
import umu.tds.gestion_gastos.categoria.Categoria;

public class NotificacionFilterCategoria implements INotificacionFilter {
    
    private final Categoria categoria;
    
    public NotificacionFilterCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    @Override
    public boolean test(Notificacion n) {
        if (categoria == null) return true;
        
        Categoria catNotif = n.getCategoria();
        if (catNotif == null) return false;
        
        return catNotif.getNombre().equalsIgnoreCase(categoria.getNombre());
    }
}