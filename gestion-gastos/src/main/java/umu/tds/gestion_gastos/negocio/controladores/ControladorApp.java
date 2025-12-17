package umu.tds.gestion_gastos.negocio.controladores;

import java.util.List;

import umu.tds.gestion_gastos.adapters.repository.impl.CategoriaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.GastoRepositoryJSONImpl;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.gasto.GestorGastos;

public class ControladorApp {

    private final GastoRepository gastoRepository;
    private final CategoriaRepository categoriaRepository;

    private final GestorGastos gestorGastos;
    private final GestorCategorias gestorCategorias;

    public ControladorApp() {
        this.gastoRepository = new GastoRepositoryJSONImpl();
        this.categoriaRepository = new CategoriaRepositoryJSONImpl();

        this.gestorGastos = new GestorGastos(gastoRepository);
        this.gestorCategorias = new GestorCategorias(categoriaRepository);
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
    
}
