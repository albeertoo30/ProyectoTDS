package umu.tds.gestion_gastos.categoria;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
	
	void add(Categoria categoria);
    void update(Categoria categoria);
    void remove(Categoria categoria);
    Optional<Categoria> findByName(String nombre);
    List<Categoria> getAll();
    void cargar(String ruta) throws IOException;
	void guardar(String ruta) throws IOException;
	
}
