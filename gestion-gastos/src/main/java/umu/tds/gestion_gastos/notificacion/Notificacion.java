package umu.tds.gestion_gastos.notificacion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import umu.tds.gestion_gastos.categoria.Categoria;

public class Notificacion {
	
	private final String id;
	private String mensaje;
	private final double importe;
	private final LocalDate fecha;
	private final String alertaId;
	private final Categoria categoria;
	private boolean leida;
	
	//Constructor
	public Notificacion(String mensaje, double importe, 
			String alertaId, Categoria categoria) {
	
		this.id = UUID.randomUUID().toString();
		this.mensaje = Objects.requireNonNull(mensaje);
		this.importe = importe;
		this.fecha = LocalDate.now();
		this.alertaId = alertaId;
		this.categoria = categoria;
		this.leida = false;
	}
	
	
	public void marcarLeida() {
		this.leida = true;
	}
	

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @return the importe
	 */
	public double getImporte() {
		return importe;
	}

	/**
	 * @return the fecha
	 */
	public LocalDate getFecha() {
		return fecha;
	}

	/**
	 * @return the alertaId
	 */
	public String getAlertaId() {
		return alertaId;
	}

	/**
	 * @return the categoria
	 */
	public Categoria getCategoria() {
		return categoria;
	}

	/**
	 * @return the leida
	 */
	public boolean isLeida() {
		return leida;
	}
		

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if (!(o instanceof Notificacion)) return false;
		Notificacion that = (Notificacion) o;
		return id.equals(that.id);
	}
	
	@Override
	public int hashCode() { return Objects.hash(id); }


	@Override
	public String toString() {
		return "Notificacion [id=" + id + ", mensaje=" + mensaje + ", importe=" + importe + ", fecha=" + fecha
				+ ", categoria=" + categoria + "]";
	}

	
	
	

}
