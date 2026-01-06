# PATRONES  DE DISEÑO USADOS

## SINGLETON

Para manejar que se usa una única instancia para los repositorios y para el controlador, esto lo manejamos gracias a la clase de Configuracion que es la que mantiene estas instancias. Algunas implementadas como enum y otras como implementación directa de la interfaz.

Esto nos evita problemas de:
- concurrencia al acceder a los JSON
- inconsistencias de datos por usar diferentes instancias
- uso innecesario de memoria

Ejemplo

    public enum AlertaRepositoryJSONImpl implements IAlertaRepository {
        INSTANCE;
        // ...
    }

## STRATEGY

Usado en Alerta y Cuenta (con Polimorfismo)

En las alertas tenemos la interfaz AlertaStrategy cuyas implementaciones son las diferentes periodicidades de las alertas, cada alerta necesita calcular si se supera un límite de manera diferente.

Esto nos facilita a la hora de crear más tipos de alertas, hace que cada alerta sea independiente y fácil de probar. (se apoya de una factoría)

Ejemplo

    AlertaStrategy strategy = AlertaStrategyFactory.crear("Mensual");
    boolean supera = strategy.seSupera(alerta, nuevoGasto, todosGastos);

En Cuenta tenemos las implementaciones CuentaIndividual y CuentaCompartida, cada cuenta tiene un cimportamiento diferente, la compartida cada miembro puede tener un porcentaje diferente.

Los métodos en cuestión: 

* getCuotaUsuario(): En compartida se calcula según porcentajes. 
* getMiembros(): compartida retorna la lista de usuarios.


## FACTORY

AlertStrategyFactory, se encarga de la creación de estrategias de alerta según un parámetro String que se le asigna en la vista. (Mejorable)

Esto nos desacopla la vista de las clases concretas, facilita la extensibilidad y nos valida los tipos de alerta disponibles.

    public static AlertaStrategy crear(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "semanal" -> new AlertaSemanal();
            case "mensual" -> new AlertaMensual();
            case "anual" -> new AlertaAnual();
            default -> throw new IllegalArgumentException("Tipo no soportado");
        };
    }


## BUILDER

AlertaFilterBuilder y NotificacionFilterBuilder nos permite construir filtros complejos de forma fluida combinando múltiples criterios.
    
    //Operaciones con filtros.
    public Filtro<Alerta> crearFiltroCompuestoA(String cuenta, Double d, Double h, Categoria c ){   
    	 return new AlertaFilterBuilder()
    			 .cuenta(cuenta)
    			 .limite(d,h)
    			 .categoria(c)
    			 .build();
    }
    

## OBSERVER

Utilizado para notificar los gastos, la interfaz es una personalizada "GastoListener", nuestro observable es GestorGastos, que tiene una lista de listeners y los métodos para notificar, y el observer concreto es AlertManager.

Cuando se toca un gasto, AlertManager necesita verificar si se supera algun límite de alerta y entonces crear notificaciones correspondientes. Este patrón nos permite desacoplar la gestión de gastos de la de alertas, tenemos la posibilidad de tener varios observadores que reaccionen a los cambios y nos facilita agregar nuevos comportamientos sin modificar GestorGastos.

Flujo:

* Usuario crea un gasto llamando a ControladorApp.crearGasto() 
* GestorGastos crea el gasto y notifica notifyGastoModificado(gasto);
* AlertManager.onGastoNuevo() se ejecuta automáticamente, verifica y si es necesario crea las notificaciones.

    // GestorGastos mantiene lista de observers
    private List<GastoListener> listeners;

    // Notifica a todos los observers
    private void notifyGastoCreado(Gasto g) {
        for (GastoListener l : listeners) l.onGastoNuevo(g);
    }

    // AlertManager reacciona al evento
    public void onGastoNuevo(Gasto gasto) {
        // Verifica alertas y crea notificaciones
    }

## ADAPTER

ImportadorCSVAdapter nos adapta la lectura de archivos CSV al formato esperado por la app.
Lee el CSV línea a línea, parsea cambos y crea Gastos

Nos facilita añadir otros formatos implementando la misma interfaz, el controldor no sabe nada, y encapsulamos la lógica de importación.

Ejemplo: 
    
    public interface IImportadorGastos {
        List<Gasto> importarGastos(File fichero) throws Exception;
    }

## FACADE
ControladorApp nos proporciona uan interfaz simplificada para todas las operacioens del sistema, la vista solo necesita conocer el ControladorApp, no los gestores individuales.


## TEMPLATE METHOD
Filtro<T> es una interfaz con métodos por defecto. Tiene una estructura común para los filtros de alertas y notificaciones, con operaciones por defecto que pueden combinarse.

    public interface Filtro<T> extends Predicate<T> {
        default Filtro<T> and(Filtro<T> other) {
            return n -> this.test(n) && other.test(n);
        }
        
        default Filtro<T> or(Filtro<T> other) {
            return n -> this.test(n) || other.test(n);
        }
        
        default Filtro<T> negateFilter() {
            return n -> !this.test(n);
        }
    }


## MODEL VIEW CONTROLLER 
Separación de responsabilidades.
Nos permite cambiar la vista sin afectar la lógica, además, todas las vistas pueden usar el mismo controlador.

* Modelo:
    Clases de dominio: Gasto, Cuenta, Categoria, Alerta, Notificacion
    Lógica de negocio: Gestores y repositorios 
* Vista:
    Archivos FXML: GastosView.fxml, FormularioGasto.fxml, etc.
    No contienen lógica de negocio
* Controlador:
    ControladorApp: Lógica de negocio
    Controladores de vista: ControladorGastosView, FormularioCategoriaController...

## REPOSITORY 
Los repositorios y sus implementaciones JSON. Separa la lógica de acceso a datos del resto de la aplicación:

## DEPENDENCY INJECTION

(A raíz del proyecto TPV).
Configuración y su implementación. Se encarga de la creación y la inyección de dependencias. 

    public ConfiguracionImpl() {
        CategoriaRepository categoriaRepo = new CategoriaRepositoryJSONImpl(...);
        CuentaRepository cuentaRepo = new CuentaRepositoryJSONImpl(...);
        //
        //

        IAlertManager alertManager = new AlertManager(cuentaRepo, alertaRepo, notiRepo);
        
        this.controlador = new ControladorApp(
            cuentaRepo, categoriaRepo, notiRepo, alertaRepo, 
            alertManager, usuarioRepo, SceneManager.INSTANCE
        );
    }
