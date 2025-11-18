package umu.tds.gestion_gastos.categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
	
	void add(Categoria categoria);
    void update(Categoria categoria);
    void remove(Categoria categoria);
    Optional<Categoria> findByName(String nombre);
    List<Categoria> getAll();
	
}
