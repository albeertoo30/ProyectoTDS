package umu.tds.gestion_gastos.usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorUsuarios {

	private UsuarioRepository usuarioRepo;
	
	
	public GestorUsuarios(UsuarioRepository usuarioRepo) {
		this.usuarioRepo = usuarioRepo;
	}
	
	public void crearUsuario(Usuario usuario) {
		this.usuarioRepo.guardar(usuario);
	}
	/*
	 
	 
	 
	public boolean borrarUsuario(int usuarioId) {
		boolean borrado = false;
		for(Usuario usuario : this.usuarios) {
			if(usuario.getId() == usuarioId) {
				this.usuarios.remove(this.usuarios.indexOf(usuario));
				borrado = true;
			}
		}
		return borrado;
	}
	
	public boolean actualizarUsuario(int usuarioId, String nombreUsuario) {
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
	 */
	
    //Inicio de sesion falso ( Si ya hay un usuario en el JSON se asume que eres tu. Si no crea uno para ti 
    public Usuario inicializarSesion() {
        List<Usuario> todos = this.usuarioRepo.obtenerTodos();
        
        if (todos.isEmpty()) {
            Usuario nuevo = new Usuario((int) Math.floor(Math.random() * 1000), "Yo (Propietario)");
            this.usuarioRepo.guardar(nuevo);
            
            // Creamos un par de amigos para probar
            // TODO:  BORRAR ESTO LUEGO O DEJAR SI QUEREMOS USUARIOS POR DEFECTO
            this.crearUsuario(new Usuario(this.generarId(), "Pepe"));
            this.crearUsuario(new Usuario(this.generarId(), "Maria"));
            
            return nuevo;
        } else {
            return todos.get(0);
        }
    }
    
    //Devuelve usuarios que no son el usuario principal
    public List<Usuario> obtenerOtrosUsuarios(int idUsuarioActual) {
        return usuarioRepo.obtenerTodos().stream()
                .filter(u -> u.getId() != (idUsuarioActual))
                .collect(Collectors.toList());
    }
    
    private int generarId() {
        return (int) (Math.random() * 100000);
    }
	
	
	
}
