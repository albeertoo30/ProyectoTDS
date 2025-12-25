package umu.tds.gestion_gastos.alerta;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.gasto.Gasto;

public class Alerta {
	
	//Atributos
	private String id;
	private double limite;
	private boolean activa;
	private String descripcion;
	private Categoria categoria; // opcional: null = todas las categorías
	private AlertaStrategy strategy;
	
	//Constructores
	public Alerta() {}
	
	@JsonCreator
	public Alerta(@JsonProperty("descripcion") String descripcion,
                  @JsonProperty("categoria") Categoria categoria,
                  @JsonProperty("strategy") AlertaStrategy strategy,
				  @JsonProperty("limite")double limite) {
		this.id = UUID.randomUUID().toString();
		this.limite = limite;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.strategy = strategy;
		//Por defecto cuando se crea se activa
		this.activa = true;
	}
	
	
	
	//Metodos
	//Provisional
	public String getDescripcion() { return descripcion; }
	public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
	public void setId(String id) { this.id = id; }
	public Categoria getCategoria() { return categoria; }
	public void setCategoria(Categoria categoria) { this.categoria = categoria; }
	public AlertaStrategy getStrategy() { return strategy; }
	public void setStrategy(AlertaStrategy strategy) { this.strategy = strategy; }
	

	
	public boolean seSuperaCon(Gasto nuevoGasto, java.util.List<Gasto> todosGastos) {
		if (!activa || strategy== null) return false;
		return strategy.seSupera(this, nuevoGasto, todosGastos);
	}	
		
		
	//public boolean ejecutarComprobar(List<Gasto> gastos) {
	//	return this.comportamiento.comprobar(gastos, this.limite);
	//}
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
	
	public String getId() {
		return this.id;
	}
	
	@Override
    public String toString() {
        return String.format("Alerta[id=%s, descripcion='%s', limite=%.2f, categoria=%s, activa=%s]",
            id, descripcion, limite, categoria != null ? categoria : "todas", activa);
    }
	
	
}
