package umu.tds.gestion_gastos.negocio.controladores;

import java.util.List;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.categoria.GestorCategorias;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.gasto.GestorGastos;

public class ControladorApp {

    private GestorGastos gestorGastos;
    private GestorCategorias gestorCategorias;

    public ControladorApp(GestorGastos gestorGastos, GestorCategorias gestorCategorias) {
        this.gestorGastos = gestorGastos;
        this.gestorCategorias = gestorCategorias;
    }

    //GASTOS

    public void registrarGasto(Gasto gasto) {
        gestorGastos.registrarGasto(gasto);
    }

    public void editarGasto(Gasto gasto) {
        gestorGastos.editarGasto(gasto);
    }

    public void eliminarGasto(int id) {
        gestorGastos.eliminarGasto(id);
    }

    public List<Gasto> obtenerGastos() {
        return gestorGastos.obtenerTodos();
    }

    // CATEGORÍAS

    public List<Categoria> obtenerCategorias() {
        return gestorCategorias.getCategorias();
    }
}