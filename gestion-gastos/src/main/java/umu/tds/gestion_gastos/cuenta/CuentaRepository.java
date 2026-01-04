package umu.tds.gestion_gastos.cuenta;

import java.io.IOException;
import java.util.List;

public interface CuentaRepository {

	Cuenta add(Cuenta cuenta);
	boolean remove(int id);
	boolean update(Cuenta cuentaActualizada);
	List<Cuenta> getAll();
	Cuenta getById(int id);
	
	public void cargar(String rutaRecurso) throws IOException;
	public void guardar(String rutaRecurso) throws IOException;
}
