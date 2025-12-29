package umu.tds.gestion_gastos.cuenta;

import java.util.List;

public interface CuentaRepository {

	Cuenta add(Cuenta cuenta);
	boolean remove(int id);
	boolean update(int id, String nombre);
	List<Cuenta> getAll();
	Cuenta getById(int id);
	
	
}
