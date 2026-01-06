package umu.tds.gestion_gastos.terminal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import umu.tds.gestion_gastos.categoria.Categoria;
import umu.tds.gestion_gastos.cuenta.Cuenta;
import umu.tds.gestion_gastos.cuenta.CuentaIndividual;
import umu.tds.gestion_gastos.gasto.Gasto;
import umu.tds.gestion_gastos.negocio.controladores.ControladorApp;
import umu.tds.gestion_gastos.usuario.Usuario;

public class Terminal {

    private final ControladorApp controlador;
    private final Scanner scanner;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Terminal(ControladorApp controlador) {
        this.controlador = controlador;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.println("║   GESTOR DE GASTOS - LÍNEA DE COMANDOS   ║");
        System.out.println("╚═══════════════════════════════════════╝");
        
        boolean salir = false;

        while (!salir) {
            mostrarMenu();
            String opcion = leerLinea();

            switch (opcion) {
                case "1" -> registrarGasto();
                case "2" -> modificarGasto();
                case "3" -> borrarGasto();
                case "4" -> listarGastos();
                case "5" -> salir = true;
                default -> System.out.println("MAL: Opción inválida. Por favor, selecciona 1-5.");
            }
        }

        scanner.close();
        System.out.println("\n¡Hasta pronto!");
    }

    private void mostrarMenu() {
        System.out.println("\n┌─────────────────────────┐");
        System.out.println("│       MENÚ PRINCIPAL       │");
        System.out.println("├─────────────────────────┤");
        System.out.println("│ 1. Registrar gasto        │");
        System.out.println("│ 2. Modificar gasto        │");
        System.out.println("│ 3. Borrar gasto           │");
        System.out.println("│ 4. Listar gastos          │");
        System.out.println("│ 5. Salir                  │");
        System.out.println("└─────────────────────────┘");
        System.out.print("Opción: ");
    }

    // ========== REGISTRAR GASTO ==========
    private void registrarGasto() {
        System.out.println("\n═══ REGISTRAR NUEVO GASTO ═══");
        
        try {
            // 1. FECHA
            LocalDate fecha = leerFecha();

            // 2. CANTIDAD
            System.out.print("Cantidad (€): ");
            double cantidad = Double.parseDouble(leerLinea());
            if (cantidad <= 0) {
                System.out.println("MAL: La cantidad debe ser mayor que 0");
                return;
            }

            // 3. DESCRIPCIÓN
            System.out.print("Descripción: ");
            String descripcion = leerLinea();
            if (descripcion.isBlank()) {
                System.out.println("MAL: La descripción no puede estar vacía");
                return;
            }

            // 4. CATEGORÍA
            Categoria categoria = seleccionarCategoria();
            if (categoria == null) return;

            // 5. CUENTA
            Cuenta cuenta = seleccionarCuenta();
            if (cuenta == null) return;

            // 6. USUARIO (el actual por defecto en terminal)
            Usuario usuario = controlador.getUsuarioActual();

            // 7. CREAR GASTO
            controlador.crearGasto(fecha, cantidad, descripcion, categoria, usuario, cuenta);

            System.out.println("\n Gasto registrado correctamente");
            System.out.printf("  * Fecha: %s\n", fecha.format(formatter));
            System.out.printf("  * Cantidad: %.2f€\n", cantidad);
            System.out.printf("  * Categoría: %s\n", categoria.getNombre());
            System.out.printf("  * Cuenta: %s\n", cuenta.getNombre());

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número válido");
        } catch (Exception e) {
            System.out.println("Error al registrar el gasto: " + e.getMessage());
        }
    }

    // ========== MODIFICAR GASTO ==========
    private void modificarGasto() {
        System.out.println("\n═══ MODIFICAR GASTO ═══");

        List<Gasto> gastos = obtenerMisGastos();
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos para modificar");
            return;
        }

        // Mostrar lista numerada
        mostrarGastosNumerados(gastos);

        System.out.print("\nSelecciona el número del gasto a modificar (0 para cancelar): ");
        try {
            int seleccion = Integer.parseInt(leerLinea());
            
            if (seleccion == 0) {
                System.out.println("Operación cancelada");
                return;
            }

            if (seleccion < 1 || seleccion > gastos.size()) {
                System.out.println("MAL: Número inválido");
                return;
            }

            Gasto gastoOriginal = gastos.get(seleccion - 1);
            
            System.out.println("\n--- Gasto seleccionado ---");
            mostrarDetalleGasto(gastoOriginal);
            System.out.println("\n(Pulsa ENTER para mantener el valor actual)");

            // NUEVA FECHA
            System.out.print("Nueva fecha [" + gastoOriginal.getFecha().format(formatter) + "]: ");
            String inputFecha = leerLinea();
            LocalDate nuevaFecha = inputFecha.isBlank() ? 
                gastoOriginal.getFecha() : LocalDate.parse(inputFecha, formatter);

            // NUEVA CANTIDAD
            System.out.print("Nueva cantidad [" + gastoOriginal.getCantidad() + "€]: ");
            String inputCantidad = leerLinea();
            double nuevaCantidad = inputCantidad.isBlank() ? 
                gastoOriginal.getCantidad() : Double.parseDouble(inputCantidad);

            // NUEVA DESCRIPCIÓN
            System.out.print("Nueva descripción [" + gastoOriginal.getDescripcion() + "]: ");
            String inputDesc = leerLinea();
            String nuevaDesc = inputDesc.isBlank() ? 
                gastoOriginal.getDescripcion() : inputDesc;

            // NUEVA CATEGORÍA
            System.out.print("¿Cambiar categoría? (s/N): ");
            Categoria nuevaCat = leerLinea().equalsIgnoreCase("s") ? 
                seleccionarCategoria() : gastoOriginal.getCategoria();
            if (nuevaCat == null) nuevaCat = gastoOriginal.getCategoria();

            // NUEVA CUENTA
            System.out.print("¿Cambiar cuenta? (s/N): ");
            Cuenta nuevaCuenta = leerLinea().equalsIgnoreCase("s") ? 
                seleccionarCuenta() : gastoOriginal.getCuenta();
            if (nuevaCuenta == null) nuevaCuenta = gastoOriginal.getCuenta();

            // USUARIO (mantenemos el mismo)
            Usuario usuario = gastoOriginal.getUsuario();

            // ACTUALIZAR
            controlador.actualizarGasto(gastoOriginal, nuevaFecha, nuevaCantidad, 
                nuevaDesc, nuevaCat, usuario, nuevaCuenta);

            System.out.println("\n Gasto modificado correctamente");

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número válido");
        } catch (DateTimeParseException e) {
            System.out.println("Error: Formato de fecha incorrecto (usa dd/MM/yyyy)");
        } catch (Exception e) {
            System.out.println("Error al modificar el gasto: " + e.getMessage());
        }
    }

    // ========== BORRAR GASTO ==========
    private void borrarGasto() {
        System.out.println("\n═══ BORRAR GASTO ═══");

        List<Gasto> gastos = obtenerMisGastos();
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos para borrar");
            return;
        }

        mostrarGastosNumerados(gastos);

        System.out.print("\nSelecciona el número del gasto a borrar (0 para cancelar): ");
        try {
            int seleccion = Integer.parseInt(leerLinea());

            if (seleccion == 0) {
                System.out.println("Operación cancelada");
                return;
            }

            if (seleccion < 1 || seleccion > gastos.size()) {
                System.out.println("MAL: Número inválido");
                return;
            }

            Gasto gasto = gastos.get(seleccion - 1);
            
            System.out.println("\n--- Gasto a borrar ---");
            mostrarDetalleGasto(gasto);

            System.out.print("\n¿Estás seguro? (s/N): ");
            if (leerLinea().equalsIgnoreCase("s")) {
                controlador.eliminarGasto(gasto.getId());
                System.out.println("\n✓ Gasto borrado correctamente");
            } else {
                System.out.println("Operación cancelada");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Debes introducir un número válido");
        } catch (Exception e) {
            System.out.println("Error al borrar el gasto: " + e.getMessage());
        }
    }

    // ========== LISTAR GASTOS ==========
    private void listarGastos() {
        System.out.println("\n═══ MIS GASTOS ═══");

        List<Gasto> gastos = obtenerMisGastos();

        if (gastos.isEmpty()) {
            System.out.println("No hay gastos registrados");
            return;
        }

        System.out.printf("\n%-12s | %-10s | %-20s | %-15s | %s\n", 
            "FECHA", "IMPORTE", "CATEGORÍA", "CUENTA", "DESCRIPCIÓN");
        System.out.println("─".repeat(85));

        for (Gasto g : gastos) {
            String nombreCuenta = (g.getCuenta() != null) ? 
                g.getCuenta().getNombre() : "Individual";
            
            System.out.printf("%-12s | %9.2f€ | %-20s | %-15s | %s\n",
                g.getFecha().format(formatter),
                g.getCantidad(),
                g.getCategoria().getNombre(),
                nombreCuenta,
                g.getDescripcion()
            );
        }

        double total = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        System.out.println("─".repeat(85));
        System.out.printf("TOTAL: %.2f€ (%d gastos)\n", total, gastos.size());
    }

    // ========== MÉTODOS AUXILIARES ==========

    private LocalDate leerFecha() {
        System.out.print("Fecha (dd/MM/yyyy) [ENTER = hoy]: ");
        String input = leerLinea();
        
        if (input.isBlank()) {
            return LocalDate.now();
        }
        
        try {
            return LocalDate.parse(input, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("MAL: Formato incorrecto. Usando fecha de hoy.");
            return LocalDate.now();
        }
    }

    private Categoria seleccionarCategoria() {
        List<Categoria> categorias = controlador.obtenerCategorias();
        
        if (categorias.isEmpty()) {
            System.out.println("MAL: No hay categorías disponibles");
            return null;
        }

        System.out.println("\nCategorías disponibles:");
        for (int i = 0; i < categorias.size(); i++) {
            System.out.printf("  %d. %s\n", i + 1, categorias.get(i).getNombre());
        }

        System.out.print("Selecciona categoría (número): ");
        try {
            int index = Integer.parseInt(leerLinea()) - 1;
            if (index >= 0 && index < categorias.size()) {
                return categorias.get(index);
            } else {
                System.out.println("MAL: Número inválido");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("MAL: Debes introducir un número");
            return null;
        }
    }

    private Cuenta seleccionarCuenta() {
        List<Cuenta> cuentas = controlador.obtenerTodasLasCuentas();
        
        if (cuentas.isEmpty()) {
            System.out.println("MAL: No hay cuentas disponibles");
            return null;
        }

        System.out.println("\nCuentas disponibles:");
        for (int i = 0; i < cuentas.size(); i++) {
            String tipo = (cuentas.get(i) instanceof CuentaIndividual) ? 
                "Individual" : "Compartida";
            System.out.printf("  %d. %s (%s)\n", 
                i + 1, cuentas.get(i).getNombre(), tipo);
        }

        System.out.print("Selecciona cuenta (número): ");
        try {
            int index = Integer.parseInt(leerLinea()) - 1;
            if (index >= 0 && index < cuentas.size()) {
                return cuentas.get(index);
            } else {
                System.out.println("MAL: Número inválido");
                return null;
            }
        } catch (NumberFormatException e) {
            System.out.println("MAL: Debes introducir un número");
            return null;
        }
    }

    private List<Gasto> obtenerMisGastos() {
        Usuario actual = controlador.getUsuarioActual();
        return controlador.obtenerGastos().stream()
            .filter(g -> g.getUsuario() != null && g.getUsuario().equals(actual))
            .toList();
    }

    private void mostrarGastosNumerados(List<Gasto> gastos) {
        System.out.println();
        for (int i = 0; i < gastos.size(); i++) {
            Gasto g = gastos.get(i);
            System.out.printf("%2d. %s | %.2f€ | %s | %s\n",
                i + 1,
                g.getFecha().format(formatter),
                g.getCantidad(),
                g.getCategoria().getNombre(),
                g.getDescripcion()
            );
        }
    }

    private void mostrarDetalleGasto(Gasto g) {
        System.out.printf("  * Fecha: %s\n", g.getFecha().format(formatter));
        System.out.printf("  * Cantidad: %.2f€\n", g.getCantidad());
        System.out.printf("  * Categoría: %s\n", g.getCategoria().getNombre());
        System.out.printf("  * Cuenta: %s\n", 
            g.getCuenta() != null ? g.getCuenta().getNombre() : "Individual");
        System.out.printf("  * Descripción: %s\n", g.getDescripcion());
    }

    private String leerLinea() {
        return scanner.nextLine().trim();
    }
}