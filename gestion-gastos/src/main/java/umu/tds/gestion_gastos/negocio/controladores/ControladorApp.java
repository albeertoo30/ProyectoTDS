package umu.tds.gestion_gastos.negocio.controladores;

import java.util.List;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.gasto.GestorGastos;

public class ControladorApp {

    private final GestorGastos gestorGastos;
    private final GestorCategorias gestorCategorias;

    public ControladorApp(GastoRepository gastoRepo, CategoriaRepository categoriaRepo) {
        this.gestorGastos = new GestorGastos(gastoRepo);
        this.gestorCategorias = new GestorCategorias(categoriaRepo);
    }

    // Operaciones con gastos
    public List<Gasto> obtenerGastos() {
        return gestorGastos.obtenerTodos();
    }

    public void registrarGasto(Gasto gasto) {
        gestorGastos.registrarGasto(gasto);
    }

    public void editarGasto(Gasto gasto) {
        gestorGastos.editarGasto(gasto);
    }

    public void eliminarGasto(int id) {
        gestorGastos.eliminarGasto(id);
    }

    public Gasto obtenerGastoPorId(int id) {
        return gestorGastos.obtenerPorId(id);
    }
    
    // Operaciones con categorias
    public List<Categoria> obtenerCategorias() {
        return gestorCategorias.getCategorias();
    }
    
    public void crearCategoria(String nombre, String descripcion) {
        gestorCategorias.crearCategoria(nombre, descripcion);
    }

    public void editarCategoria(Categoria categoria) {
        gestorCategorias.editarCategoria(categoria);
    }
    
    public boolean categoriaTieneGastos(String nombreCategoria) {
        return gestorGastos.obtenerTodos().stream()
                .anyMatch(g ->
                    g.getCategoria() != null &&
                    g.getCategoria().getNombre().equalsIgnoreCase(nombreCategoria)
                );
    }
    
    public void eliminarCategoria(String nombre) {
        if (categoriaTieneGastos(nombre)) {
            throw new IllegalStateException(
                "No se puede eliminar la categoría porque tiene gastos asociados."
            );
        }
        gestorCategorias.eliminarCategoria(nombre);
    }
}
