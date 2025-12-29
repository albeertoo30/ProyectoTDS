package umu.tds.gestion_gastos.cuenta;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import umu.tds.gestion_gastos.gasto.Gasto;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY, 
    property = "tipo"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CuentaIndividual.class, name = "INDIVIDUAL"),
    @JsonSubTypes.Type(value = CuentaCompartida.class, name = "COMPARTIDA")
})
public interface Cuenta {
    int getId();
    void setId(int id);
    
    String getNombre();
    void setNombre(String nombre);
    
    List<Gasto> getGastos();
    void agregarGasto(Gasto g);
    
    double obtenerSaldo(String idUsuario);
}