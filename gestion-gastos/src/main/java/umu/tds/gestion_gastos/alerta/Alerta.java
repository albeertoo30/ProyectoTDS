package umu.tds.gestion_gastos.alerta;

import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public class Alerta {
	
	//Atributos
	private int id;
	private double limite;
	private boolean activa;
	private ComportamientoAlerta comportamiento;

	
	//Constructores
	public Alerta(int id, double limite) {
		this.id = id;
		this.limite = limite;
		//Por defecto cuando se crea se activa
		this.activa = true;
	}
	
	//Metodos
	//Provisional
	public boolean ejecutarComprobar(List<Gasto> gastos) {
		return this.comportamiento.comprobar(gastos, this.limite);
	}
					//Esto yo lo cambiaria
	public boolean isActiva() {
		return this.activa;
	}
	
	//Para que se pueda modificar supongo
	public void setLimite(double limite) {
		this.limite = limite;
	}
	
	public void desactivarAlarma() {
		this.activa = false;
	}
	
	public double getLimite() {
		return this.limite;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getMensaje() {
		return this.comportamiento.getMensaje();
	}

}
