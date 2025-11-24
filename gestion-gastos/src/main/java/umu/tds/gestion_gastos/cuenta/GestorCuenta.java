package umu.tds.gestion_gastos.cuenta;

import umu.tds.gestion_gastos.gasto.Gasto;

public class GestorCuenta {
	
	private CuentaRepository repositorio;
	
	public GestorCuenta(RepoCuenta repo) {
		this.repositorio = repo;
	}
	
	
	public void definirPorcentajes() {
		// TODO
	}
	
	public double calcularSaldos() {
		// TODO
		return .0;
	}
	
	public boolean eliminarCuenta() {
		// TODO
		return true;
	}
	
	public void agregarGasto(Gasto gasto) {
		// TODO
	}
	
	
	
	
}
