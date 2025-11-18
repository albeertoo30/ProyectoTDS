package umu.tds.gestion_gastos.gasto;

import java.util.List;
import java.util.Optional;

public interface GastoRepository {
	
	void add(Gasto g);
	void update(Gasto g);
	void remove(Gasto g);
	Optional<Gasto> findById(int id);
	List<Gasto> getAll();
}
