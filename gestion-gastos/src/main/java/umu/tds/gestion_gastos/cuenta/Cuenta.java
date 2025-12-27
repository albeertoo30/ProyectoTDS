package umu.tds.gestion_gastos.cuenta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class Cuenta {
	
	private int id;
	private String nombre;
	private List<Usuario> miembros;
	private List<Gasto> gastos;
	
	//Equitativo por defecto
	private Map<Usuario, Double> porcentajesParticipacion;
	
	
	public Cuenta(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
		this.miembros = new ArrayList<Usuario>();
		this.gastos = new ArrayList<Gasto>();
		this.porcentajesParticipacion = new HashMap<Usuario, Double>();
	}

	public Cuenta(int id, String nombre, List<Usuario> miembros) {
		this.id = id;
		this.nombre = nombre;
		this.miembros = new ArrayList<Usuario>(miembros);
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public List<Usuario> getMiembros(){
		return this.miembros;
	}
	
	public List<Gasto> getGastos(){
		return this.gastos;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public void setPorcentajes(Map<Usuario, Double> porcentajes) {
		this.porcentajesParticipacion = porcentajes;
	}
	
	public Map<Usuario, Double> getPorcentajes(){
		return this.porcentajesParticipacion;
	}
	
	public boolean esEquitativa() {
		return this.porcentajesParticipacion.isEmpty();
	}
	
	//Si no hay porcentajes setteados, simplemente se divide entre el nº de usuarios
	public double getCuotaUsuario(Usuario user) {
		if(this.esEquitativa()) {
			return 100.0/this.miembros.size();
		}else {
			return this.porcentajesParticipacion.getOrDefault(user, 0.0);
		}
	}
	
	public void agregarGasto(Gasto g) {
		this.gastos.add(g);
	}
	
	
	
	
	//No hago setMiembros ya que usaremos el add del gestor.
	
	
	
	
	
}
