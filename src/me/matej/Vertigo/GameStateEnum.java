package me.matej.Vertigo;

import me.matej.Vertigo.GameStates.*;

/**
 *
 * @author matejkramny
 */
public enum GameStateEnum {
	Splash (new SplashScreenState()), MainMenu(new MainMenuState()), Game(new GameState()), GameMenu(new GameMenuState()), Options(new OptionsState());
	
	private GameStateClass stateInstance;
	
	GameStateEnum () { }
	GameStateEnum (GameStateClass stateInstance) {
		this.stateInstance = stateInstance;
	}
	
	public void setStateInstance (GameStateClass stateInstance) {
		this.stateInstance = stateInstance;
	}
	public GameStateClass getStateInstance () {
		return stateInstance;
	}
}
