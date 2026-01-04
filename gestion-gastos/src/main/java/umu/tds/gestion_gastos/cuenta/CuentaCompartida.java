package umu.tds.gestion_gastos.cuenta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.usuario.Usuario;

@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.PROPERTY, 
	    property = "tipo"
	)
	@JsonTypeName("COMPARTIDA")
public class CuentaCompartida implements Cuenta {
    
    private int id;
    private String nombre;
    private List<Usuario> miembros;
    private List<Gasto> gastos;
    
    // Equitativo por defecto
    private Map<String, Double> porcentajesParticipacion;
    
    public CuentaCompartida() {
        this.miembros = new ArrayList<>();
        this.gastos = new ArrayList<>();
        this.porcentajesParticipacion = new HashMap<>();
    }
    // --------------------------------------------------

    public CuentaCompartida(int id, String nombre) {
        this(); // Llama al vacío para inicializar listas
        this.id = id;
        this.nombre = nombre;
        this.gastos = new ArrayList<>();
    }

    public CuentaCompartida(int id, String nombre, List<Usuario> miembros) {
        this(); // Llama al vacío
        this.id = id;
        this.nombre = nombre;
        if (miembros != null) {
            this.miembros.addAll(miembros);
        }
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
    
    public List<Usuario> getMiembros(){ return this.miembros; }
    public void setMiembros(List<Usuario> miembros) { this.miembros = miembros; }
    
    public void setPorcentajes(Map<String, Double> porcentajes) {
        this.porcentajesParticipacion = porcentajes;
    }
    
    public Map<String, Double> getPorcentajes(){
        return this.porcentajesParticipacion;
    }
    
    public boolean esEquitativa() {
        return this.porcentajesParticipacion == null || this.porcentajesParticipacion.isEmpty();
    }
    
    @Override
    public double getCuotaUsuario(Usuario user) {
        if (this.esEquitativa()) {
            return this.miembros.isEmpty() ? 0 : 100.0 / this.miembros.size();
        } else {
            String key = String.valueOf(user.getId());
            return this.porcentajesParticipacion.getOrDefault(key, 0.0);
        }
    }

    @Override
    public double obtenerSaldo(String idUsuario) {
        return 0; // Pendiente implementar
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CuentaCompartida other = (CuentaCompartida) obj;
		return id == other.id;
	}
    
    
}