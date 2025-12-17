package umu.tds.gestion_gastos.categoria;

import java.util.List;

public class GestorCategorias {

	private CategoriaRepository repositorio;
	
	public GestorCategorias(CategoriaRepository repositorio) {
		this.repositorio = repositorio;
	}
	
	// Métodos de consulta
	public List<Categoria> getCategorias() {
        return repositorio.getAll();
    }
	
	public Categoria obtenerCategoriaPorNombre(String nombre) {
        return repositorio.findByName(nombre).orElse(null);
    }
	
	// Funcionalidad 
	public void crearCategoria(String nombre, String descripcion) {
		if(nombre == null || nombre.trim().isEmpty()) 
			throw new IllegalArgumentException("El nombre de la categoría no puede estar vacío");
		if(repositorio.findByName(nombre).isPresent()) 
			throw new IllegalArgumentException("Ya existe una categoría con el nombre " + nombre);
		repositorio.add(new Categoria(nombre, descripcion));
	}
	
	public void editarCategoria(String oldName, String newName, String newDescripcion) {
		repositorio.findByName(oldName).ifPresent(categoria -> {
			if(newName != null && !newName.trim().isEmpty()) {
				categoria.setNombre(newName);
			}
			if(newDescripcion != null) {
				categoria.setDescripcion(newDescripcion);
			}
			repositorio.update(categoria);
		});
	}
	
	public void eliminarCategoria(String nombre) {
        var categoria = repositorio.findByName(nombre)
                .orElseThrow(() -> new IllegalArgumentException("No existe una categoría con el nombre" + nombre));
        repositorio.remove(categoria);
    }
	
}
