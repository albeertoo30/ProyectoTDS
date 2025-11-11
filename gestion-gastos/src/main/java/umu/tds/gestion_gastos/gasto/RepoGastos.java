package umu.tds.gestion_gastos.gasto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RepoGastos {
	
	private List<Gasto> gastos;
	
	// Constructor
	public RepoGastos() {
		this.gastos = new ArrayList<Gasto>();
	}
	
	// Obtener todos los gastos
	public List<Gasto> getAllGastos(){
		return new ArrayList<Gasto>(gastos);
	}
	
	// Funcionalidad
	public void add(Gasto g) {
        gastos.add(g);
    }

    public void remove(Gasto g) {
        gastos.remove(g);
    }
	
    public void update(Gasto g) { 
        for (int i = 0; i < gastos.size(); i++) {
            if (gastos.get(i).getId() == g.getId()) {
                gastos.set(i, g);
                break;
            }
        }
    }
    
    public Optional<Gasto> findById(int id) {
        return gastos.stream().filter(g -> g.getId() == id).findFirst();
    }

}
