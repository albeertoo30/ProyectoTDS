package umu.tds.gestion_gastos.gasto;

import java.util.List;
import java.util.stream.Collectors;

public class GestorGastos {
	
	private RepoGastos repositorio;
	
	public GestorGastos(RepoGastos repositorio) {
		this.repositorio = repositorio;
	}
	
	// Método de consulta
	public List<Gasto> obtenerTodos() {
        return repositorio.getAllGastos();
    }
	
	// Funcionalidad
	public void registrarGasto(Gasto g) {
		if (g == null) throw new IllegalArgumentException("El gasto no puede ser nulo");
        if (g.getCantidad() <= 0) throw new IllegalArgumentException("La cantidad debe ser positiva");
        repositorio.add(g);
	}
	
	public void editarGasto(Gasto g) {
		if (g == null) throw new IllegalArgumentException("El gasto no puede ser nulo");
		repositorio.add(g);
	}
	
	public void eliminarGasto(int id) {
		repositorio.findById(id).ifPresent(repositorio::remove);
	}
	
	public Gasto obtenerPorId(int id) {
		return repositorio.findById(id).orElse(null);
	}
	
	public List<Gasto> obtenerGastosFiltradosPorCategoria(String categoria){
		return repositorio.getAllGastos().stream()
				.filter(g -> g.getCategoria() != null &&
						g.getCategoria().getNombre().equalsIgnoreCase(categoria))
				.collect(Collectors.toList());
	}
	
	/* Usamos el tipo envoltorio Double porque el filtrado puede ser solo con un mínimo
	o solo con un máximo y de esta manera podemos permitir null. */
	public List<Gasto> obtenerGastosFiltrados(String categoria, Double min, Double max){
		return repositorio.getAllGastos().stream()
				.filter(g -> categoria == null || g.getCategoria().getNombre().equalsIgnoreCase(categoria))
				.filter(g -> min == null || g.getCantidad() >= min)
				.filter(g -> max == null || g.getCantidad() <= max)
				.collect(Collectors.toList());
	}
	
}
