package umu.tds.gestion_gastos.alerta;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;

import umu.tds.gestion_gastos.alerta.Alerta;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.filtros.Filtro;
import umu.tds.gestion_gastos.notificacion.Notificacion;

/**
 * Interfaz que define las operaciones del repositorio de alertas
 * Persiste y consulta alertas
 * NO DECIDE CUANDO SE DISPARA UNA ALERTA
 * No depende de gastos ni notificaciones,
 * 
 * necesito: add remove getAlertas getAlertasActivas setAlertas cargar y guardar.
 * 
 * 
 * 
 */
public interface IAlertaRepository {
    
	/**
     * Carga las alertas desde el almacenamiento persistente
     * @param rutaBase Ruta base donde se encuentra el archivo JSON
     * @throws IOException Si hay error al leer el archivo
     */
    void cargar(String rutaBase) throws IOException;
    
    /**
     * Guarda las alertas en el almacenamiento persistente
     * @param rutaBase Ruta base donde se guardará el archivo JSON
     * @throws IOException Si hay error al escribir el archivo
     */
    void guardar(String rutaBase) throws IOException;
    
    /**
     * Añade una nueva alerta al repositorio
     * @param alerta La alerta a añadir
     */
    void add(Alerta alerta);
    
    /**
     * Elimina una alerta del repositorio
     * @param alerta La alerta a eliminar
     */
    void remove(Alerta alerta);
    
    
    /**
     * Crea una nuev aalerta
     * @param Descripcion de la alerta
     * @param Categoria de la alerta
     * @param Estrategia de la alerta
     * @param limite de la alerta
     * @param id de la cuenta
     */
    void crearAlerta(String descripcion, Categoria categoria, AlertaStrategy strategy, double limite, String idCuenta);
    
    
    /**
     * Obtiene todas las alertas (activas e inactivas)
     * @return Lista inmutable de todas las alertas
     */
    List<Alerta> getAlertas();
    
    /**
     * Obtiene solo las alertas activas
     * @return Lista de alertas activas
     */
    List<Alerta> getAlertasActivas();
    
    /**
     * Reemplaza todas las alertas con una nueva lista
     * @param nuevasAlertas Lista de alertas a establecer
     */
    void setAlertas(List<Alerta> nuevasAlertas);

    
    
	List<Alerta> findByFilter(Filtro<Alerta> filter);

	void activarAlerta(String id);

	void desactivarAlerta(String id);
	
	
	Optional<Alerta> getById(String id);

	
	List<Alerta> getAlertasPorCuenta(String cuentaActual);
	
}