package umu.tds.gestion_gastos.alerta;

import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public class Alerta {
	
	//Atributos
	private int id;
	private double limite;
	private boolean isActiva;
	
	//Constructores
	public Alerta(int id, double limite) {
		this.id = id;
		this.limite = limite;
		//Por defecto cuando se crea se activa
		this.isActiva = true;
	}
	
	//Metodos
	//Provisional
	public boolean ejecutarComprobar(List<Gasto> gastos) {
		return true;
	}
					//Esto yo lo cambiaria
	public boolean estaActiva() {
		return this.isActiva;
	}
	
	//Para que se pueda modificar supongo
	public void setLimite(double limite) {
		this.limite = limite;
	}
	
	public void desactivarAlarma() {
		this.isActiva = false;
	}
	
	public double getLimite() {
		return this.limite;
	}
	
	public int getId() {
		return this.id;
	}
	

}
