package umu.tds.gestion_gastos.alerta;
import umu.tds.gestion_gastos.gasto.Gasto;

public interface GastoListener {

	 /**
     * Notifica que se ha añadido un nuevo gasto
     */
    void onGastoNuevo(Gasto gasto);
    
    /**
     * Notifica que se ha modificado un gasto
     */
    public void onGastoModificado(Gasto gasto);
    /**
     * Notifica que se ha eliminado un gasto
     */
    void onGastoEliminado(Gasto gasto);

}
