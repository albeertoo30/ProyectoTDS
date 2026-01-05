package umu.tds.gestion_gastos.gasto;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import umu.tds.gestion_gastos.alerta.GastoListener;
import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaRepository;
import umu.tds.gestion_gastos.usuario.Usuario;

public class GestorGastos {
	
	private CuentaRepository repositorio;
	private List<GastoListener> listeners;
	
	public GestorGastos(CuentaRepository repositorio) {
		this.repositorio = repositorio;
		this.listeners = new ArrayList<GastoListener>();
	}
	
	// Obtener todos los gastos de todas las cuentas
	public List<Gasto> obtenerTodos() {
        List<Gasto> todosLosGastos = new ArrayList<>();
        List<Cuenta> cuentas = repositorio.getAll();

        for (Cuenta c : cuentas) {
            List<Gasto> gastosDeLaCuenta = c.getGastos();
            for (Gasto g : gastosDeLaCuenta) {
                // Restauramos la referencia 'padre' en memoria.
                // Como pusimos @JsonIgnore en Gasto, al leer del JSON 'g.getCuenta()' será null.
                // Aquí lo arreglamos para que la aplicación funcione bien.
                g.setCuenta(c);
                todosLosGastos.add(g);
            }
        }
        return todosLosGastos;
    }
	
	
    // PATRÓN CREADOR: GestorGastos tiene la información para crear y registrar el Gasto.
    public boolean crearGasto(LocalDate fecha, double cantidad, String descripcion, Categoria categoria, Usuario pagador, Cuenta cuenta) {
    	if(cuenta == null) return false;
    	//ID 0 provisionalmente hasta que la persistencia asigne el real
        Gasto nuevoGasto = new Gasto(0, fecha, cantidad, descripcion, categoria);
        // Asignación de cuenta y usuario pagador
        nuevoGasto.setUsuario(pagador);
        nuevoGasto.setCuenta(cuenta);
        // Añadir gasto a la cuenta
        cuenta.agregarGasto(nuevoGasto);
   
        boolean exito = repositorio.update(cuenta);
        if (exito) {
            notifyGastoCreado(nuevoGasto); 
        }
        return exito;
    }
	
	// Método auxiliar
	private Cuenta buscarCuentaDelGasto(int idGasto) {
        for (Cuenta c : repositorio.getAll()) {
            for (Gasto g : c.getGastos()) {
                if (g.getId() == idGasto) {
                    return c;
                }
            }
        }
        return null;
    }
	
	public void modificarGasto(Gasto gasto, LocalDate fecha, double cantidad, String descripcion, Categoria categoria, Usuario pagador, Cuenta cuenta) {
		if (gasto == null) throw new IllegalArgumentException("El gasto no puede ser nulo");
        if (cuenta == null) throw new IllegalArgumentException("La cuenta destino no puede ser nula");
        Cuenta antigua = buscarCuentaDelGasto(gasto.getId());
        if (antigua == null) {
            throw new IllegalArgumentException("Error crítico: El gasto a editar no pertenece a ninguna cuenta guardada.");
        }
        
        boolean cambioDeCuenta = antigua.equals(cuenta);
        if (cambioDeCuenta) {
            // CASO 1: MOVER DE CUENTA (Borrar de antigua -> Añadir a nueva)
            
            // A. Borrar de la antigua
            cuenta.getGastos().removeIf(g -> g.getId() == gasto.getId());
            repositorio.update(cuenta);
            
            // B. Actualizar datos del gasto
            gasto.setFecha(fecha);
            gasto.setCantidad(cantidad);
            gasto.setDescripcion(descripcion);
            gasto.setCategoria(categoria);
            gasto.setUsuario(pagador);
            gasto.setCuenta(cuenta); // Enlazar a la nueva
            
            // C. Añadir a la nueva
            cuenta.agregarGasto(gasto);
            repositorio.update(cuenta);
            
        } else {
            // CASO 2: MISMA CUENTA (Actualización in-situ)
            
            // Buscamos el gasto dentro de la lista de la cuenta para actualizar esa referencia
            // (Recordatorio: cuentaAntigua es una copia fresca del repo, hay que buscar dentro)
            for (Gasto g : antigua.getGastos()) {
                if (g.getId() == gasto.getId()) {
                    g.setFecha(fecha);
                    g.setCantidad(cantidad);
                    g.setDescripcion(descripcion);
                    g.setCategoria(categoria);
                    g.setUsuario(pagador);
                    // La cuenta sigue siendo la misma
                    break;
                }
            }
            // Guardamos la cuenta con el gasto modificado
            repositorio.update(antigua);
        }
        notifyGastoModificado(gasto);
	}
	
	public void eliminarGasto(int id) {
	    List<Cuenta> cuentas = repositorio.getAll();
	    Gasto gastoABorrar = null;
	    Cuenta cuentaGasto = null;
	    for(Cuenta c: cuentas) {
	    	for(Gasto g: c.getGastos()) {
	    		if(g.getId() == id) {
	    			gastoABorrar = g;
	    			cuentaGasto = c;
	    			break;
	    		}
	    	}
	    }
	    if(gastoABorrar != null && cuentaGasto != null) {
	    	cuentaGasto.getGastos().remove(gastoABorrar);
	    	repositorio.update(cuentaGasto);
	    	gastoABorrar.setCuenta(cuentaGasto);
	    	notifyGastoEliminado(gastoABorrar);
	    }else {
	    	throw new IllegalArgumentException("No existe un gasto con ID " + id);
	    }
	}
	
	public Gasto obtenerPorId(int id) {
		return obtenerTodos().stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .orElse(null);
	}
	
	public List<Gasto> obtenerGastosFiltradosPorCategoria(String categoria){
		return obtenerTodos().stream()
                .filter(g -> g.getCategoria() != null &&
                        g.getCategoria().getNombre().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
	}
	
	/* Usamos el tipo envoltorio Double porque el filtrado puede ser solo con un mínimo
	o solo con un máximo y de esta manera podemos permitir null. */
	public List<Gasto> obtenerGastosFiltrados(String categoria, Double min, Double max){
		return obtenerTodos().stream()
				.filter(g -> categoria == null || g.getCategoria().getNombre().equalsIgnoreCase(categoria))
				.filter(g -> min == null || g.getCantidad() >= min)
				.filter(g -> max == null || g.getCantidad() <= max)
				.collect(Collectors.toList());
	}
	
	// Otra opción de filtrado
	public List<Gasto> obtenerGastosFiltrados(LocalDate fecha, Categoria categoria, Double min, Double max) {
	    String nombreCategoria = (categoria != null) ? categoria.getNombre() : null;
	    
	    return obtenerGastosFiltrados(nombreCategoria, min, max).stream()
	        .filter(g -> fecha == null || g.getFecha().equals(fecha))
	        .collect(Collectors.toList());
	}
	
	// Patrón observer
	public void addListener(GastoListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    // 3. MÉTODOS DE NOTIFICACIÓN PRIVADOS
    private void notifyGastoCreado(Gasto g) {
        for (GastoListener l : listeners) l.onGastoNuevo(g);
    }

    private void notifyGastoModificado(Gasto g) {
        for (GastoListener l : listeners) l.onGastoModificado(g);
    }

    private void notifyGastoEliminado(Gasto g) {
        for (GastoListener l : listeners) l.onGastoEliminado(g);
    }
	
}
