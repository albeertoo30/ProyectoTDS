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

    public abstract String getRutaCategorias();
    
    public abstract String getRutaCuentas();
    
    public abstract String getRutaUsuarios();
    
    public abstract String getRutaAlertas();
 
    public abstract String getRutaNotificaciones();
    
    public abstract void cargarTodo() throws IOException;
    
    public abstract void guardarTodo() throws IOException;
    
    public abstract String getCuentaActual();
    
    public abstract void setCuentaActual(String id);
    
}
