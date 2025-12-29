package umu.tds.gestion_gastos.cuenta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class GestorCuenta {
	
	private CuentaRepository repositorio;
	
	public GestorCuenta(RepoCuenta repo) {
		this.repositorio = repo;
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
		Cuenta cuenta = this.repositorio.getById(idCuenta);
		
		Map<Usuario, Double> saldos = new HashMap<>();
		List<Gasto> gastos = cuenta.getGastos();
		
		for(Gasto g : gastos) {
			
			// TODO: gasto debe tener getUsuario?
			Usuario pagador = g.getUsuario();
			double importe = g.getCantidad();
			
			//Si no exixste el pagador en el mapa, asignamos el importe
			//Si si existe, se lo sumamos con Double::sum
			saldos.merge(pagador, importe, Double::sum);
			
			//Ahora restamos a cada uno su parte para calcular los saldos negativos.
			
			for(Usuario miembro : cuenta.getMiembros()) {
				//Calculamos la asignacion de ese usuario en la cuenta
				double porc = cuenta.getCuotaUsuario(miembro);
				//Lo que debe es el importe * su participacion [0-100.0]
				double cantidadDeber = importe * (porc / 100.0);
				
				//Mismo que antes pero en negfativo para restar
				saldos.merge(miembro, -cantidadDeber, Double::sum);
			}
			
			
			
		}
		
		
	}
	
	public boolean eliminarCuenta() {
		// TODO
		return true;
	}
	
	public void agregarGasto(Gasto gasto) {
		// TODO
	}
	
	
	
	
}
