package umu.tds.gestion_gastos;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.notificacion.Notificacion;

public class NotificacionRepositoryImpl implements NotificacionRepository{
	private static final Logger log = LogManager.getLogger();

	private List<Notificacion> compras = null;
	private String rutaFichero;
	
	private void cargaNotificaciones(){
		try {
			rutaFichero = Configuracion.getInstancia().getRutaGestor(); //Configuracion.getInstancia().getRutaCompras();
			this.compras = cargarNotificaciones(rutaFichero);
			if (compras == null) compras = new ArrayList<>();
		} catch (Exception e) {
			log.error("Error cargando las compras ", e);
		}
	}

	@Override
	public List<? extends Notificacion> getNotificaciones() {
		if (compras == null) {
			cargaNotificaciones();
		}
		return compras;
	}

	@Override
	public Notificacion findById(int id){ 
		Optional<Notificacion> compra = compras.stream()
				.filter( c -> c.getId() == id)
				.findFirst();
	
		return compra.orElse(null);
	}

	
private List<Notificacion> cargarNotificaciones(String rutaFichero) throws Exception {
		
		InputStream ficheroStream = getClass().getResourceAsStream(rutaFichero);
		
		ObjectMapper mapper = new ObjectMapper();
		GestorImpl gestor = (GestorImpl) Configuracion.getInstancia().getGestor();
		
		GestorImpl gestorCargado = mapper.readValue(ficheroStream, new TypeReference<GestorImpl>() {});
		gestor.setNotificaciones((List<Notificacion>) gestorCargado.getNotificaciones());		
		gestor.setGastos((List<Gasto>) gestorCargado.getGastos());

		this.compras = (List<Notificacion>) gestor.getNotificaciones();
		return compras;
	}


	private void guardarNotificaciones(List<Notificacion> compras, String rutaFichero) throws Exception {
		String rutaAbsoluta = getClass().getResource(rutaFichero).getPath();

		GestorImpl gestor = (GestorImpl) Configuracion.getInstancia().getGestor();

		gestor.setNotificaciones(compras);
		this.compras = compras;
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File(rutaAbsoluta), gestor);
	}
	

}
