# HISTORIAS DE USUARIO: APP GESTIÓN DE GASTOS

## Objetivo 1: Gestión de Gastos
Meta:
Permitir al usuario registrar, consultar, modificar y eliminar sus gastos personales de forma sencilla y organizada.

### Historia 1.1: Registrar un nuevo gasto
Como usuario, quiero registrar un gasto indicando su cantidad, fecha, categoría y descripción para llevar un control detallado de mis gastos personales.
Criterios de aceptación:
- El sistema solicita la cantidad, fecha, categoría y una breve descripción del gasto.
- Todos los campos obligatorios deben ser completados.
- El gasto se guarda correctamente en el sistema y aparece en la lista general de gastos.
- La información persiste tras cerrar y volver a abrir la aplicación.

### Historia 1.2: Editar un gasto existente
Como usuario, quiero editar un gasto previamente registrado para corregir o actualizar su información.
Criterios de aceptación:
- El usuario puede seleccionar un gasto de la lista y modificar cualquiera de sus atributos (cantidad, fecha, categoría o descripción).
- El sistema actualiza el gasto y muestra los cambios inmediatamente.
- Los datos modificados se guardan de forma persistente.

### Historia 1.3: Eliminar un gasto
Como usuario, quiero eliminar un gasto existente para mantener mi lista de gastos limpia y actualizada.
Criterios de aceptación:
- El usuario puede seleccionar un gasto y eliminarlo.
- El sistema solicita confirmación antes de borrar definitivamente el registro.
- El gasto desaparece de la lista y ya no figura en los datos persistidos.

### Historia 1.4: Consultar la lista de gastos
Como usuario, quiero ver una lista con todos mis gastos registrados para revisar mi historial económico.
Criterios de aceptación:
- La aplicación muestra los gastos en una tabla con columnas: fecha, categoría, descripción y cantidad.
- La lista puede ordenarse por fecha o cantidad.
- Se actualiza automáticamente al añadir, editar o eliminar gastos.

### Historia 1.5: Filtrar gastos
Como usuario, quiero filtrar mis gastos por fecha, categoría o combinación de ambas para analizar mejor mis hábitos de consumo.
Criterios de aceptación:
- El sistema permite seleccionar uno o varios filtros (por ejemplo: mes, rango de fechas, categorías).
- Los resultados se actualizan en tiempo real al aplicar el filtro.
- Es posible limpiar los filtros para ver todos los gastos nuevamente.

### Historia 1.6: Visualizar los gastos de forma gráfica
Como usuario, quiero ver mis gastos representados en gráficos de barras o circulares para comprender mejor la distribución por categorías o periodos.
Criterios de aceptación:
- El usuario puede elegir entre vista de tabla y vista gráfica.
- Los gráficos muestran correctamente la proporción de gastos por categoría o mes.
- Los gráficos se actualizan automáticamente al modificar los datos.


## Objetivo 2: Gestión de Categorías
Meta:
Permitir al usuario organizar sus gastos en diferentes categorías, pudiendo crear, modificar y eliminar categorías según sus necesidades.

### Historia 2.1: Consultar las categorías existentes
Como usuario, quiero ver la lista de categorías disponibles para poder seleccionar la adecuada al registrar o filtrar mis gastos.
Criterios de aceptación:
- La aplicación muestra una lista de categorías predefinidas (por ejemplo: alimentación, transporte, ocio, etc.).
- Las categorías aparecen ordenadas alfabéticamente o por fecha de creación.
- La lista se actualiza automáticamente al añadir o eliminar categorías.

### Historia 2.2: Crear una nueva categoría
Como usuario, quiero poder crear nuevas categorías para adaptar la aplicación a mis propios tipos de gasto.
Criterios de aceptación:
- El sistema solicita el nombre de la nueva categoría.
- No se permite crear dos categorías con el mismo nombre.
- Una vez creada, la categoría aparece disponible en el listado general y puede ser usada para registrar gastos.
- La categoría se guarda de forma persistente.

### Historia 2.3: Modificar una categoría existente
Como usuario, quiero editar el nombre de una categoría existente para corregir errores o ajustar su denominación.
Criterios de aceptación:
- El usuario puede seleccionar una categoría y modificar su nombre.
- El sistema impide asignar un nombre ya existente.
- Todos los gastos asociados a esa categoría se actualizan automáticamente.
- Los cambios se guardan de manera persistente.

### Historia 2.4: Eliminar una categoría
Como usuario, quiero eliminar una categoría que ya no utilizo para mantener organizada la lista de categorías.
Criterios de aceptación:
- El usuario puede seleccionar una categoría y eliminarla.
- El sistema solicita confirmación antes de eliminarla definitivamente.
- Si existen gastos asociados a la categoría, el sistema avisa y ofrece opciones (reasignar o eliminar los gastos relacionados).
- Los datos se actualizan y persisten correctamente.

## Objetivo 3: Alertas y Notificaciones 
Meta:
Permitir al usuario configurar alertas personalizadas que avisen cuando se superen determinados límites de gasto, así como consultar el historial de notificaciones generadas por dichas alertas. Aquí se usará el patrón estrategia.

### Historia 3.1: Configurar una alerta de gasto
Como usuario, quiero definir una alerta con un límite de gasto para recibir una notificación cuando supere ese límite.
Criterios de aceptación:
- El usuario puede crear una nueva alerta indicando el tipo de límite (semanal o mensual) y el importe máximo.
- Opcionalmente, el usuario puede asociar la alerta a una categoría específica.
- No se permite crear dos alertas con los mismos parámetros (tipo y categoría).
- La alerta se guarda de forma persistente y queda activa hasta que el usuario la elimine.

### Historia 3.2: Recibir notificación al superar el límite
Como usuario, quiero recibir una notificación automática cuando mis gastos superen el límite establecido para poder controlar mis finanzas.
Criterios de aceptación:
- El sistema evalúa los límites configurados cada vez que se registra o modifica un gasto.
- Si se supera el límite, se genera una notificación visible en la interfaz (por ejemplo, un mensaje emergente o una sección de alertas).
- La notificación incluye el importe total y la categoría afectada (si aplica).
- La notificación queda registrada en el historial para su consulta posterior.

### Historia 3.3: Consultar historial de notificaciones
Como usuario, quiero poder consultar las notificaciones generadas en el pasado para revisar mis alertas anteriores.
Criterios de aceptación:
- La aplicación muestra una lista con las notificaciones ordenadas por fecha.
- Cada notificación incluye información del límite, la categoría (si existe) y la fecha en que fue superado.
- El historial se mantiene de forma persistente y puede filtrarse por periodo o categoría.

### Historia 3.4: Eliminar o desactivar alertas
Como usuario, quiero eliminar o desactivar alertas que ya no necesito para mantener mi configuración actualizada.
Criterios de aceptación:
- El usuario puede seleccionar una alerta y eliminarla o desactivarla temporalmente.
- El sistema solicita confirmación antes de eliminar definitivamente una alerta.
- Si una alerta está desactivada, no se generan notificaciones hasta su reactivación.
- Las modificaciones se guardan de forma persistente.

### Requisito no funcional: Extensibilidad
Como desarrollador del sistema, quiero que el diseño de las alertas sea extensible mediante estrategias diferentes para poder añadir nuevos tipos de alerta sin modificar el código existente.
Criterios de aceptación:
- El sistema implementa un mecanismo que permita definir estrategias de cálculo de alertas (por ejemplo, semanal, mensual o por categoría).
- Cada tipo de alerta debe implementar una interfaz común.
- La adición de nuevos tipos de alerta no debe afectar al funcionamiento del sistema existente.

## Objetivo 4: Cuentas Compartidas
Meta:
Permitir al usuario crear cuentas de gasto compartidas con otras personas, registrar gastos dentro de dichas cuentas y consultar los saldos pendientes entre los miembros.

### Historia 4.1: Crear una cuenta de gasto compartida
Como usuario, quiero crear una nueva cuenta de gasto compartida para registrar gastos comunes con otras personas y repartir los importes entre todos los participantes.
Criterios de aceptación:
- El usuario indica el nombre de la cuenta y añade una lista de personas (al menos dos).
- Cada persona se identifica por un nombre simple (no requiere autenticación).
- Por defecto, los gastos se reparten equitativamente entre los miembros.
- La cuenta se guarda de forma persistente.
- Una vez creada, la lista de miembros no puede modificarse.

### Historia 4.2: Definir porcentajes personalizados de reparto
Como usuario, quiero poder establecer un porcentaje de reparto diferente para cada persona para reflejar contribuciones no equitativas dentro de una cuenta compartida.
Criterios de aceptación:
- El sistema permite asignar un porcentaje de gasto a cada miembro.
- La suma total de los porcentajes debe ser igual a 100%.
- Los porcentajes se guardan junto con la cuenta compartida.
- Si no se definen porcentajes, se aplica el reparto equitativo por defecto.

### Historia 4.3: Registrar un gasto en una cuenta compartida
Como usuario, quiero registrar un gasto dentro de una cuenta compartida indicando quién lo ha pagado para actualizar los saldos de los miembros.
Criterios de aceptación:
- El usuario selecciona una cuenta compartida existente.
- Se indica el pagador, el importe, la fecha, la categoría y una descripción del gasto.
- El sistema recalcula automáticamente el saldo de cada miembro según el reparto definido.
- Los cambios se guardan de forma persistente.

### Historia 4.4: Consultar los saldos de una cuenta compartida
Como usuario, quiero ver cuánto debe o le deben cada persona del grupo para conocer el balance económico entre los miembros.
Criterios de aceptación:
- La aplicación muestra una tabla con los saldos individuales de todos los miembros.
- Los saldos positivos indican dinero que se les debe, y los negativos, dinero que deben pagar.
- El saldo total del grupo debe ser igual a 0.
- Los datos se actualizan automáticamente al añadir o eliminar gastos.

### Historia 4.5: Eliminar una cuenta compartida
Como usuario, quiero eliminar una cuenta compartida que ya no utilizo para mantener la aplicación ordenada.
Criterios de aceptación:
- El usuario puede eliminar una cuenta compartida seleccionada.
- El sistema solicita confirmación antes de eliminarla.
- Se eliminan también los gastos asociados a la cuenta.
- La información desaparece de manera persistente del sistema.

### Historia 4.6: Visualizar el historial de gastos de una cuenta compartida
Como usuario, quiero ver todos los gastos asociados a una cuenta compartida para revisar quién pagó y cuándo.
Criterios de aceptación:
- Se muestra una lista de gastos con fecha, pagador, categoría, descripción e importe.
- Es posible filtrar los gastos por persona o categoría.
- Los datos son coherentes con los saldos calculados.


## Objetivo 5: Visualización y Estadísticas
Meta:
Permitir al usuario analizar sus gastos mediante visualizaciones interactivas, incluyendo gráficos, tablas filtrables y vistas en calendario, para facilitar la comprensión de su distribución y evolución en el tiempo.

### Historia 5.1: Visualizar gastos en tabla o lista
Como usuario, quiero ver mis gastos en formato de tabla para revisar de forma clara los detalles de cada gasto registrado.
Criterios de aceptación:
- La tabla muestra columnas con fecha, categoría, descripción, importe y tipo de cuenta (personal o compartida).
- El usuario puede ordenar la tabla por cualquier columna.
- Se actualiza automáticamente cuando se añaden, editan o eliminan gastos.
- Los filtros aplicados (fecha, categoría, etc.) se reflejan en la tabla.

### Historia 5.2: Visualizar gastos mediante gráficos
Como usuario, quiero ver mis gastos representados gráficamente (en barras o círculos) para comprender mejor la distribución de mis gastos.
Criterios de aceptación:
- El usuario puede seleccionar el tipo de gráfico (barras o circular).
- Los gráficos muestran la proporción o el total de gastos agrupados por categoría o por mes.
- Los datos del gráfico se actualizan automáticamente cuando cambian los gastos.
- El usuario puede guardar o exportar el gráfico como imagen (opcional).

### Historia 5.3: Visualizar gastos en calendario (CalendarFX)
Como usuario, quiero ver mis gastos representados en un calendario para identificar fácilmente los días en los que he realizado gastos.
Criterios de aceptación:
- Se utiliza una vista de calendario (por ejemplo, Full Day View o Month View de CalendarFX).
- Cada gasto aparece como un evento con su categoría, descripción e importe.
- El usuario puede hacer clic en un día para ver los gastos correspondientes.
- La vista del calendario se sincroniza con los datos registrados en la aplicación.


## Objetivo 6: Importación de Datos Externos
Meta:
Permitir al usuario importar gastos desde ficheros externos (por ejemplo, extractos bancarios) para integrarlos automáticamente en la aplicación, soportando distintos formatos de datos mediante un diseño extensible.

### Historia 6.1: Seleccionar un archivo externo para importar
Como usuario, quiero seleccionar un archivo de datos de gastos desde mi ordenador para cargar automáticamente la información en la aplicación.
Criterios de aceptación:
- El usuario puede abrir un cuadro de diálogo para seleccionar un fichero de texto o CSV.
- El sistema valida el formato del archivo antes de procesarlo.
- Si el archivo es válido, se inicia el proceso de importación.
- Si el formato no es compatible, el sistema muestra un mensaje de error.

### Historia 6.2: Importar gastos desde un formato estándar (CSV o TXT)
Como usuario, quiero importar gastos desde un archivo de texto plano o CSV para no tener que introducirlos manualmente.
Criterios de aceptación:
- Cada línea del archivo representa un gasto con los campos: fecha, categoría, descripción e importe.
- El sistema lee el archivo y genera automáticamente los registros de gasto correspondientes.
- Validar y evitar duplicados.
- Los datos importados se añaden a la lista general de gastos.
- Los gastos importados se guardan de forma persistente.

## Objetivo 7: Acceso desde Línea de Comandos
Meta:
Permitir al usuario realizar las operaciones básicas de gestión de gastos, categorías y alertas mediante una línea de comandos, sin necesidad de utilizar la interfaz gráfica.

### Historia 7.1: Iniciar la aplicación en modo línea de comandos
Como usuario, quiero poder iniciar la aplicación en modo texto para interactuar con ella desde la terminal o consola.
Criterios de aceptación:
- El usuario puede iniciar la aplicación con un comando o argumento específico (por ejemplo, java -jar gestiongastos.jar --cli).
- El sistema muestra un menú textual con las operaciones disponibles.
- El modo CLI y el modo GUI son mutuamente excluyentes pero comparten los mismos datos persistidos.

### Historia 7.2: Registrar un gasto desde la línea de comandos
Como usuario, quiero añadir un nuevo gasto introduciendo sus datos desde la consola para registrar rápidamente mis gastos sin abrir la interfaz gráfica.
Criterios de aceptación:
- El sistema solicita los datos del gasto: cantidad, fecha, categoría y descripción.
- Los datos introducidos se validan antes de guardarse.
- El gasto se almacena de forma persistente igual que en la versión gráfica.
- El sistema confirma la creación del gasto mostrando un mensaje de éxito.

### Historia 7.3: Consultar y filtrar gastos desde la línea de comandos
Como usuario, quiero listar mis gastos en la consola para visualizar mis registros sin interfaz gráfica.
Criterios de aceptación:
- El usuario puede ver todos los gastos registrados en formato tabular o listado.
- Se pueden aplicar filtros básicos (por categoría, fecha o rango de fechas).
- El sistema muestra los resultados en la consola de forma legible.
- Los filtros pueden combinarse con opciones de ordenación.

### Historia 7.4: Editar o eliminar gastos desde la línea de comandos
Como usuario, quiero poder editar o borrar un gasto existente mediante comandos para mantener mis registros actualizados desde el terminal.
Criterios de aceptación:
- El usuario puede seleccionar un gasto por su identificador o número de lista.
- Se solicitan los nuevos datos (en caso de edición) o confirmación (en caso de borrado).
- Los cambios se guardan de forma persistente.
- El sistema muestra mensajes claros de confirmación o error.

### Historia 7.6: Salir del modo línea de comandos
Como usuario, quiero poder cerrar la aplicación de forma segura desde la consola para finalizar mi sesión sin perder datos.
Criterios de aceptación:
- El usuario puede salir escribiendo el comando exit o quit.
- Antes de cerrar, el sistema guarda los datos pendientes.

Se uestra un mensaje de despedida o confirmación de cierre.

