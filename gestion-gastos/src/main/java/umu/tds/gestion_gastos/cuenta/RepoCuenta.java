package umu.tds.gestion_gastos.cuenta;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

public class RepoCuenta implements CuentaRepository{

	private List<CuentaCompartida> cuentas;
	
	public RepoCuenta() {
		this.cuentas = new ArrayList<CuentaCompartida>();
	}
	
	public CuentaCompartida add(CuentaCompartida cuenta) {
		this.cuentas.add(cuenta);
		return cuenta;
	}
	
	public boolean remove(int id) {
		boolean borrado = false;
		for(CuentaCompartida c : this.cuentas) {
			if(c.getId() == id) {
				borrado = true;
				this.cuentas.remove(this.cuentas.indexOf(c));
			}
		}
		return borrado;
	}
	
	public boolean update(int id, String nombre) {
		boolean updated = false;
		for(CuentaCompartida c : this.cuentas) {
			if(c.getId() == id) {
				updated = true;
				this.cuentas.get(this.cuentas.indexOf(c)).setNombre(nombre);
			}
		}
		return updated;
	}
	
	public List<CuentaCompartida> getAll(){
		return this.cuentas;
	}
	
	public CuentaCompartida getById(int cuentaId) {
		CuentaCompartida c =  this.cuentas.stream().filter(cu -> cu.getId() == cuentaId).findFirst().orElse(null);
		if(c == null) throw new IllegalArgumentException("La cuenta con id " + cuentaId + " no exisiste.");
		return c;
	}
	
	
	
	
	
}
