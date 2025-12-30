package umu.tds.gestion_gastos.gasto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import umu.tds.gestion_gastos.alerta.GastoListener;

public interface GastoRepository {
	
	void add(Gasto g);
	void update(Gasto g);
	void remove(Gasto g);
	Optional<Gasto> findById(int id);
	List<Gasto> getAll();
	void cargar(String ruta) throws IOException;
	void guardar(String ruta) throws IOException;
	//Necesito esta implementacion, que sea un observable.
	void addListener(GastoListener gastoListener);
	
}
