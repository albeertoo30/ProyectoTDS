package umu.tds.gestion_gastos.importacion;

import java.io.File;
import java.util.List;

import umu.tds.gestion_gastos.gasto.Gasto;

public interface IImportadorGastos {
	
	//Controlador llama a esta funcion
	// asi cubrimos si el dia de mañana cambiamos de xml a json o a lo que sea
	List<Gasto> importarGastos(File fichero) throws Exception;
}
