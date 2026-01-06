package umu.tds.gestion_gastos.alerta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import umu.tds.gestion_gastos.Configuracion;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
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
    private final CuentaRepository cuentaRepo;
    private final IAlertaRepository alertaRepo;
    private final INotificacionRepository notiRepo;

    public AlertManager(CuentaRepository cuentaRepo, IAlertaRepository alertaRepo, INotificacionRepository notiRepo) {
        this.cuentaRepo = cuentaRepo;
        this.alertaRepo = alertaRepo;
        this.notiRepo = notiRepo;
    }

    @Override
    public void onGastoNuevo(Gasto gasto) {
    	if (gasto == null || gasto.getCuenta() == null) return;
    	//Necesario ver todos para calcular totales correctamente
    	List<Gasto> todosGastos = obtenerTodosLosGastos();
    	
        for (Alerta a : alertaRepo.getAlertasActivas()) {
        	//La estrategia se encarga del filtrado
        	boolean supera =  a.seSuperaCon(gasto, todosGastos);
        	String idAlerta = a.getIdCuenta();
            String idGasto = String.valueOf(gasto.getCuenta().getId()); 
            String nombreGasto = gasto.getCuenta().getNombre();
            
            // Comprobamos si coincide por ID (lo correcto) O por Nombre (por compatibilidad)
            boolean esMismaCuenta = idAlerta.equals(idGasto) || 
                                    idAlerta.equalsIgnoreCase(nombreGasto);
        	//Solo crea la notificacion si se supera, si no esta notificada y si la alerta es de la cuenta del gasto
        													//Violacion grasp, cambiarlo. Cambiado. Ya no se persisten las Cuentas en gastos, nueva comprobacion en gasto
        	if(supera && a.puedeNotificarse(gasto.getFecha()) && esMismaCuenta) {
        		crearNotificacionAlerta(a);   //a.getIdCuenta().equals(gasto.getCuenta().getNombre())
        		a.registrarNotificacion(gasto.getFecha());
        		//a.marcarComoNotificada();	//a.perteneceA(gasto.getNombreCuenta())	
        		//.update(a);  //Esto no lo tengo muy claro
        	}
        	
            	
        }
    }

    private List<Gasto> obtenerTodosLosGastos() {
        List<Gasto> todos = new ArrayList<>();      
        for (Cuenta c : cuentaRepo.getAll()) {
            for (Gasto g : c.getGastos()) {
                g.setCuenta(c);
                todos.add(g);
            }
        }
        return todos;
    }
    
    @Override public void onGastoModificado(Gasto gasto) { onGastoNuevo(gasto);} //Una vez salta ha saltado
    @Override public void onGastoEliminado(Gasto gasto) { /* opcional: re-evaluar */ }
    
    private void crearNotificacionAlerta(Alerta alerta) {
    	notiRepo.crearNotificacion(alerta.getDescripcion(), alerta.getLimite(), alerta.getId(), alerta.getCategoria(), alerta.getIdCuenta());
    	
    }
    
}
