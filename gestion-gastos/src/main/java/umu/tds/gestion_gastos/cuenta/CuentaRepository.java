package umu.tds.gestion_gastos.cuenta;

public interface CuentaRepository {

	Cuenta add(Cuenta cuenta);
	boolean remove(int id);
	boolean update(int id, String nombre);
	
	
}
