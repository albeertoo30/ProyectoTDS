package umu.tds.gestion_gastos.gasto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.gestion_gastos.categoria.Categoria;

public class GestorGastos {
	
	private GastoRepository repositorio;
	
	public GestorGastos(GastoRepository repositorio) {
		this.repositorio = repositorio;
	}
	
	// Método de consulta
	public List<Gasto> obtenerTodos() {
        return repositorio.getAll();
    }
	
	// Funcionalidad
	public void registrarGasto(Gasto g) {
		if (g == null) throw new IllegalArgumentException("El gasto no puede ser nulo");
        if (g.getCantidad() <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva");
        repositorio.add(g);
	}
	
	public void editarGasto(Gasto g) {
	    if (g == null) throw new IllegalArgumentException("El gasto no puede ser nulo");
	    if (g.getId() <= 0) throw new IllegalArgumentException("El gasto debe tener un ID válido");

	    if (repositorio.findById(g.getId()).isEmpty())
	        throw new IllegalArgumentException("No existe un gasto con ID " + g.getId());

	    repositorio.update(g);
	}
	
	public void eliminarGasto(int id) {
	    var gasto = repositorio.findById(id)
	            .orElseThrow(() -> new IllegalArgumentException("No existe un gasto con ID " + id));

	    repositorio.remove(gasto);
	}
	
	public Gasto obtenerPorId(int id) {
		return repositorio.findById(id).orElse(null);
	}
	
	public List<Gasto> obtenerGastosFiltradosPorCategoria(String categoria){
		return repositorio.getAll().stream()
				.filter(g -> g.getCategoria() != null &&
						g.getCategoria().getNombre().equalsIgnoreCase(categoria))
				.collect(Collectors.toList());
	}
	
	/* Usamos el tipo envoltorio Double porque el filtrado puede ser solo con un mínimo
	o solo con un máximo y de esta manera podemos permitir null. */
	public List<Gasto> obtenerGastosFiltrados(String categoria, Double min, Double max){
		return repositorio.getAll().stream()
				.filter(g -> categoria == null || g.getCategoria().getNombre().equalsIgnoreCase(categoria))
				.filter(g -> min == null || g.getCantidad() >= min)
				.filter(g -> max == null || g.getCantidad() <= max)
				.collect(Collectors.toList());
	}
	
	// Otra opción de filtrado
	public List<Gasto> obtenerGastosFiltrados(LocalDate fecha, Categoria categoria, Double min, Double max) {
	    String nombreCategoria = categoria != null ? categoria.getNombre() : null;
	    
	    return obtenerGastosFiltrados(nombreCategoria, min, max).stream()
	        .filter(g -> fecha == null || g.getFecha().equals(fecha))
	        .collect(Collectors.toList());
	}
	
	public void cargar(String ruta) throws IOException{
		repositorio.cargar(ruta);
	}
	
	public void guardar(String ruta) throws IOException{
		repositorio.guardar(ruta);
	}
}
