package umu.tds.gestion_gastos;

import java.io.IOException;
import java.nio.file.Path;

import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;

public abstract class Configuracion {

    private static Configuracion instancia;

    static void setInstancia(Configuracion impl) {
        instancia = impl;
    }

    public static Configuracion getInstancia() {
        return instancia;
    }

    public abstract ControladorApp getControladorApp();

    public abstract String getRutaGastos();

    public abstract String getRutaCategorias();
    
    public abstract String getRutaCuentas();
    
    public abstract String getRutaUsuarios();
    
    public abstract Path getRutaDatos();
 
  //Esto no se si va aqui o en el controlador
    public abstract void cargarTodo() throws IOException;
    
    public abstract void guardarTodo() throws IOException;
    
}
