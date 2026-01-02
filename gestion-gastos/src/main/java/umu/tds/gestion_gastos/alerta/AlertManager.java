package umu.tds.gestion_gastos.alerta;
import java.time.LocalDate;
import java.util.List;

import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.notificacion.INotificacionRepository;

/*
 * Implementa la lógica de negocio de alertas
 * Es observador de Gasto
 * 
 * 
 * */

public class AlertManager implements IAlertManager {
    private final GastoRepository gastoRepo;
    private final IAlertaRepository alertaRepo;
    private final INotificacionRepository notiRepo;

    public AlertManager(GastoRepository gastoRepo, IAlertaRepository alertaRepo, INotificacionRepository notiRepo) {
        this.gastoRepo = gastoRepo;
        this.alertaRepo = alertaRepo;
        this.notiRepo = notiRepo;
    }

    @Override
    public void onGastoNuevo(Gasto gasto) {
    	//Necesario ver todos para calcular totales correctamente
    	List<Gasto> todosGastos = gastoRepo.getAll();
    	
        for (Alerta a : alertaRepo.getAlertasActivas()) {
        	//La estrategia se encarga del filtrado
            if(a.seSuperaCon(gasto, todosGastos))
            	crearNotificacionAlerta(a, gasto , todosGastos);
        }
    }

    @Override public void onGastoModificado(Gasto gasto) { onGastoNuevo(gasto); }
    @Override public void onGastoEliminado(Gasto gasto) { /* opcional: re-evaluar */ }
    
    private void crearNotificacionAlerta(Alerta alerta, Gasto nuevoGasto, List<Gasto> todosGastos) {
        String mensaje = generarMensajeNotificacion(alerta);
    	notiRepo.crearNotificacion(mensaje, alerta.getLimite(), alerta.getId(), alerta.getCategoria(), Configuracion.getInstancia().getCuentaActual());

    }
    
    private String generarMensajeNotificacion(Alerta alerta) {
    	System.out.println("Limite de: " + alerta.getLimite() + " superado");
    	System.out.println(alerta.toString());
    	
        StringBuilder mensaje = new StringBuilder();

        mensaje.append("LÍMITE SUPERADO\n");
        mensaje.append("Alerta: ").append(alerta.getDescripcion()).append("\n");
        mensaje.append("Límite: ").append(String.format("%.2f€", alerta.getLimite())).append("\n");
        
        if (alerta.getCategoria() != null) {
            mensaje.append("Categoría: ").append(alerta.getCategoria().getNombre()).append("\n");
        } else {
            mensaje.append("Categoría: TODAS\n");
        }
        return mensaje.toString();
    }
}
