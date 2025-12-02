package umu.tds.gestion_gastos;

import java.util.List;

import umu.tds.gestion_gastos.notificacion.Notificacion;

public interface NotificacionRepository {

	/**
	 * Devuelve una lista con todas las compras cargadas
	 * 
	 * @return Lista de compras
	 */
	List<? extends Notificacion> getNotificaciones();
	/**
	 * Busca una compra por su id y la devuelve
	 * 
	 * @param id
	 * @throws ElementoNoEncontradoException si no encuentra la compra buscada
	 * @return Compra
	 */

	Notificacion findById(int id);

	/**
	 * Aniade una compra 
	 * 
	 * @param compra
	 * @throws ElementoExistenteException si existe una compra igual en el
	 *                                    repositorio
	 * @return void
	 */
	//void addCompra(CarritoCompraImpl compra) throws ElementoExistenteException,ErrorPersistenciaException;
	
	/**
	 * Elimina una compra de la lista de compras, si ya estaba eliminada no
	 * hace nada
	 * 
	 * @param compra
	 * @return void
	 */

	////void removeCompra(CarritoCompraImpl compra) throws ErrorPersistenciaException;
	
	/**
	 * Actualiza una compra de la lista de compras, devolviendo la referencia a la compra que
	 * se ha actualizado
	 * 
	 * @param compra
	 * @return Compra
	 */
	//CarritoCompraImpl updateCompra(CarritoCompraImpl compra) throws ErrorPersistenciaException;
	
}
