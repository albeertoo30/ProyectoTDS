package umu.tds.gestion_gastos.usuario;

import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

	
	private List<Usuario> usuarios;
	
	public GestorUsuarios() {
		this.usuarios = new ArrayList<Usuario>();
	}
	
	public Usuario crearCuenta(Usuario usuario) {
		this.usuarios.add(usuario);
		return usuario;
	}
	
	public boolean borrarCuenta(int usuarioId) {
		boolean borrado = false;
		for(Usuario usuario : this.usuarios) {
			if(usuario.getId() == usuarioId) {
				this.usuarios.remove(this.usuarios.indexOf(usuario));
				borrado = true;
			}
		}
		return borrado;
	}
	
	public boolean actualizarCuenta(int usuarioId, String nombreUsuario) {
		boolean actualizado = false;
		for(Usuario usuario : this.usuarios) {
			if(usuario.getId() == usuarioId) {
				// TODO: añadir mas campos si hubiera para actualizar o crear un setAll
				this.usuarios.get(this.usuarios.indexOf(usuario)).setNombre(nombreUsuario);
				actualizado = true;
			}
		}
		return actualizado;
	}
	
	
	
}
