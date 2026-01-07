# Arquitectura y Diseño de la Aplicación - Gestión de Gastos

## 1. Visión General de la Arquitectura
La aplicación ha sido desarrollada siguiendo el patrón arquitectónico **Modelo-Vista-Controlador (MVC)**, adaptado para una aplicación de escritorio JavaFX. El objetivo principal del diseño ha sido mantener un **bajo acoplamiento** entre la interfaz de usuario y la lógica de negocio, facilitando la mantenibilidad y la escalabilidad del sistema.

El sistema se divide en capas claramente diferenciadas:
* **Capa de Vista (.fxml y controladores de vista):** Gestiona la interacción con el usuario.
* **Capa de Controlador (Lógica):** Dirige las operaciones y conecta la vista con el modelo.
* **Capa de Modelo (Dominio):** Contiene las entidades y reglas de negocio.
* **Capa de Persistencia:** Abstrae el almacenamiento de datos.

---

## 2. Organización del Código (Paquetes)

El código fuente (`src/main/java`) se estructura en paquetes semánticos que reflejan las capas de la arquitectura:

* **`umu.tds.gestion_gastos`**: Contiene la clase principal (`App`, `AppTerminal`) encargada de arrancar la aplicación JavaFX y configurar el entorno inicial.
* **`umu.tds.gestion_gastos.alerta`**: Contiene la lógica de las alertas, con todos los patrones usados (Builder, Estrategia, etc), además de la interfaz de su repositorio.
* **`umu.tds.gestion_gastos.categoria`**: Contiene la lógica de una categoría, su gestor (controlador propio) y la interfaz del repositorio.
* **`umu.tds.gestion_gastos.cuenta`**: Contiene la lógica de las cuentas implementada con el patrón estrategia, su gestor y la interfaz del repositorio.
* **`umu.tds.gestion_gastos.gasto`**: Contiene la lógica de un gasto, su gestor (controlador propio) y la interfaz del repositorio.
* **`umu.tds.gestion_gastos.notificacion`**: Contiene la lógica de una notificación, su implementación con el patrón Builder y la interfaz de su repositorio.
* **`umu.tds.gestion_gastos.usuario`**: Contiene la lógica de un usuario, su gestor (controlador propio) y la interfaz del repositorio.
* **`umu.tds.gestion_gastos.controlador`**: Contiene el controlador principal de la aplicación.
* **`umu.tds.gestion_gastos.vista`**: Contiene los controladores de las interfaces gráficas (`VentanaInicioController`, `FormularioGastoController`, etc.) que gestionan los eventos de los archivos FXML.
* **`umu.tds.gestion_gastos.repository.impl`**: Contiene las implementaciones concretas de los repositorios (en este caso, basadas en JSON).
* **`umu.tds.gestion_gastos.importacion`**: Contiene la lógica para importar datos externos (Patrón Adapter + Factoría).

**Recursos (`src/main/resources`):**
Se mantiene una estricta separación entre código y recursos estáticos:
* Carpeta gestion_gastos: Los archivos de vista (`.fxml`) definen la estructura visual.
* Carpeta data: Los archivos de persistencia (`.json`) almacenan los datos de la aplicación localmente.

---

## 3. Persistencia de Datos
La persistencia se ha diseñado orientada a interfaces (Patrón Repository).
* El dominio utiliza interfaces (`CuentaRepository`, `ICategoriaRepository`, etc.).
* La implementación concreta actual utiliza **ficheros JSON** (biblioteca Jackson/Gson) almacenados localmente.
* Esta decisión permite que, en el futuro, se pueda cambiar la tecnología de almacenamiento (por ejemplo, a una base de datos SQL) simplemente creando nuevas implementaciones de las interfaces, sin modificar ni una sola línea de la lógica de negocio ni de la vista.
