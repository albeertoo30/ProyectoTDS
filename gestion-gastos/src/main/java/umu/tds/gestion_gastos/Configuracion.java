package umu.tds.gestion_gastos;

public abstract class Configuracion {

	private static Configuracion instancia;
	private final SceneManager sceneManager = new SceneManager();
	
	/**
	 * Solo debe ser invocado desde App
	 */
	static void setInstancia(Configuracion impl) {
		Configuracion.instancia = impl; 
	}
	
	public static Configuracion getInstancia() {
		return Configuracion.instancia;
	}

	public SceneManager getSceneManager() {
		return sceneManager;
	}
	
	public abstract ControladorGestor getControladorGestor();

	public abstract String getRutaGestor();
	public abstract Gestor getGestor();
	
}
