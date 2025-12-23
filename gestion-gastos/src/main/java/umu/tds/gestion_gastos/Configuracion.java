package umu.tds.gestion_gastos;

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
}
