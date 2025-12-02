package umu.tds.gestion_gastos;

public class ConfiguracionImpl extends Configuracion{
	

	private ControladorGestor controlador;
	private Gestor gestor;

	public ConfiguracionImpl() {
		this.controlador = new ControladorGestor(new NotificacionRepositoryImpl()); //Aqui le ponemos todos los repositorios
		this.gestor = new GestorImpl();
	}
	
	@Override
	public ControladorGestor getControladorGestor() {
		return controlador;
	}
		
	@Override
	public String getRutaGestor() {
		return "/data/gestor.json";
	}

	@Override
	public Gestor getGestor() {
		return gestor;	
	}

	
}

