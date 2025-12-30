package umu.tds.gestion_gastos.usuario;

import java.util.List;

public interface UsuarioRepository {
    void guardar(Usuario u);
    List<Usuario> obtenerTodos();
    Usuario obtenerPorId(int id);
}