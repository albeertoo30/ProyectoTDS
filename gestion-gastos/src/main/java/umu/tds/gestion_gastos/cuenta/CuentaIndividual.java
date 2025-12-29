package umu.tds.gestion_gastos.cuenta;

import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;


@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.PROPERTY, 
	    property = "tipo"
	)
	@JsonTypeName("INDIVIDUAL")
public class CuentaIndividual implements Cuenta{
	private int id;
    private String nombre;
    private List<Gasto> gastos;
    
    
    private Usuario propietario;
    
    public CuentaIndividual() {
    	this.gastos = new ArrayList<>();
    }
    
    public CuentaIndividual(int id, String nombre, Usuario propietario) {
        this.id = id;
        this.nombre = nombre;
        this.propietario = propietario;
        this.gastos = new ArrayList<>();
    }
    
    @Override
    public int getId() { return id; }
    
    @Override
    public void setId(int id) { this.id = id; }
    
    @Override
    public String getNombre() { return nombre; }
    
    @Override
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    @Override
    public List<Gasto> getGastos() { return gastos; }
    
    @Override
    public void agregarGasto(Gasto g) { this.gastos.add(g); }

    @Override
    public double obtenerSaldo(String idUsuario) {
        return 0.0;
    }
    
    public Usuario getPropietario() { return propietario; }
    //para jackson
    public void setPropietario(Usuario propietario) { this.propietario = propietario; }
    
    
}
