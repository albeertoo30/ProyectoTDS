package umu.tds.gestion_gastos.cuenta;

import java.util.ArrayList;
import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

public class Cuenta {
	
	private int id;
	private String nombre;
	private List<Usuario> miembros;
	private List<Gasto> gastos;
	
	
	public Cuenta(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
		this.miembros = new ArrayList<Usuario>();
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
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	//No hago setMiembros ya que usaremos el add del gestor.
	
	
	
	
	
}
