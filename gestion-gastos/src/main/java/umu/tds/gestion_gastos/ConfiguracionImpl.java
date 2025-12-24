package umu.tds.gestion_gastos;

import umu.tds.gestion_gastos.adapters.repository.impl.CategoriaRepositoryJSONImpl;
import umu.tds.gestion_gastos.adapters.repository.impl.GastoRepositoryJSONImpl;
import umu.tds.gestion_gastos.categoria.CategoriaRepository;
import umu.tds.gestion_gastos.gasto.GastoRepository;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public class ConfiguracionImpl extends Configuracion {

    private final ControladorApp controlador;

    public ConfiguracionImpl() {
        // Crear repositorios con las rutas correctas
        GastoRepository gastoRepo = new GastoRepositoryJSONImpl(getRutaGastos());
        CategoriaRepository categoriaRepo = new CategoriaRepositoryJSONImpl(getRutaCategorias());
        
        // Crear controlador con los repositorios
        this.controlador = new ControladorApp(gastoRepo, categoriaRepo);
    }

    @Override
    public ControladorApp getControladorApp() {
        return controlador;
    }

    @Override
    public String getRutaGastos() {
        return "/umu/tds/data/gastos.json";
    }

    @Override
    public String getRutaCategorias() {
        return "/umu/tds/data/categorias.json";
    }
}
