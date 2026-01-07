package umu.tds.gestion_gastos.importacion;

import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.cuenta.GestorCuenta;
import umu.tds.gestion_gastos.usuario.GestorUsuarios;
import umu.tds.gestion_gastos.usuario.Usuario;

public class ImportadorFactory {
    public static IImportadorGastos crear(String tipoArchivo,
    		GestorCategorias gCat, 
    		GestorCuenta gCuen, 
    		GestorUsuarios gUsu, 
    		Usuario uAct) {
        return switch (tipoArchivo.toLowerCase()) {
            case "csv" -> new ImportadorCSVAdapter(gCat,gCuen,gUsu,uAct);
            default -> throw new IllegalArgumentException("Formato no soportado");
        };
    }
}
