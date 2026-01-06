package umu.tds.gestion_gastos;

import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.terminal.Terminal;

public class AppTerminal {

    public static void main(String[] args) {
        try {
            // Inicializar configuración
            Configuracion.setInstancia(new ConfiguracionImpl());
            ControladorApp controlador = Configuracion.getInstancia().getControladorApp();

            // Cargar datos
            controlador.cargarDatos();

            // Iniciar terminal
            Terminal terminal = new Terminal(controlador);
            terminal.iniciar();

            // Guardar al salir
            controlador.guardarDatos();

        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }
}