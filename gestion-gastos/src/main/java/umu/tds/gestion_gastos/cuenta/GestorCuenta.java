package umu.tds.gestion_gastos.cuenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class GestorCuenta {
	
	private CuentaRepository repositorio;
	
	public GestorCuenta(CuentaRepository repo) {
		this.repositorio = repo;
	}
	
	public boolean esCompartida(Cuenta c) {
		return c instanceof CuentaCompartida;
	}
	
	public void registrarCuenta(Cuenta c) {
		
		this.repositorio.add(c);
	}
	
	public void eliminarCuenta(int cuentaId) {
		this.repositorio.remove(cuentaId);
	}
	
	public List<Cuenta> getAll(){
		return this.repositorio.getAll();
	}
	
	public Map<Usuario, Double> calcularSaldos(int idCuenta) {
        Cuenta cuenta = this.repositorio.getById(idCuenta); // OJO: Asegúrate de que tu repo tiene getById
        Map<Usuario, Double> saldos = new HashMap<>();
        
        // Validación de seguridad
        if (cuenta == null || cuenta.getGastos() == null) return saldos;
        
        List<Gasto> gastos = cuenta.getGastos();
        
        // 1. ITERAMOS SOBRE LOS GASTOS REALES
        for(Gasto g : gastos) {
            Usuario pagador = g.getUsuario();
            double importe = g.getCantidad();
            
            // A) El que paga, SUMA saldo (le deben dinero)
            if (pagador != null) {
                saldos.merge(pagador, importe, Double::sum);
            }
            
            // B) Todos los miembros RESTAN su parte (deben dinero)
            for(Usuario miembro : cuenta.getMiembros()) {
                // Aquí usamos el método polimórfico que acabamos de arreglar
                double porcentaje = cuenta.getCuotaUsuario(miembro); 
                
                double cantidadQueDebe = importe * (porcentaje / 100.0);
                
                // Restamos la deuda
                saldos.merge(miembro, -cantidadQueDebe, Double::sum);
            }
        }
        
        return saldos;
    }
	
	public boolean eliminarCuenta() {
		// TODO
		return true;
	}
	
	public void agregarGasto(Gasto gasto) {
		// TODO
	}
	
	
	
	
}
