package umu.tds.gestion_gastos.gasto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoGastos implements GastoRepository{
	
	private List<Gasto> gastos;
	
	// Constructor
	public RepoGastos() {
		this.gastos = new ArrayList<>();
	}
	
	// Obtener todos los gastos
	@Override
	public List<Gasto> getAll(){
		return new ArrayList<Gasto>(gastos);
	}
	
	// Funcionalidad
	@Override
	public void add(Gasto g) {
        gastos.add(g);
    }

	@Override
    public void remove(Gasto g) {
        gastos.remove(g);
    }
	
	@Override
    public void update(Gasto g) { 
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == g.getId()) {
                gastos.set(i, g);
                break;
            }
        }
    }
    
	@Override
    public Optional<Gasto> findById(int id) {
        return gastos.stream().filter(g -> g.getId() == id).findFirst();
    }

}
