package umu.tds.gestion_gastos.categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoCategorias {
	
	private List<Categoria> categorias;
	
	// Constructor
	public RepoCategorias() {
        this.categorias = new ArrayList<>();
    }

	// Obtener todas las categorias
	public List<Categoria> getAllCategorias() {
        return new ArrayList<>(categorias);
    }
	
	// Funcionalidad
    public void add(Categoria c) {
        categorias.add(c);
    }

    public void remove(Categoria c) {
        categorias.remove(c);
    }

    public void update(Categoria c) {
        for (int i = 0; i < categorias.size(); i++) {
            if (categorias.get(i).getNombre().equalsIgnoreCase(c.getNombre())) {
                categorias.set(i, c);
                break;
            }
        }
    }

    public Optional<Categoria> findByName(String nombre) {
        return categorias.stream()
                         .filter(c -> c.getNombre().equalsIgnoreCase(nombre))
                         .findFirst();
    }
}
