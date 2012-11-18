package me.matej.Vertigo.WorldCreator;

import me.matej.Vertigo.GameStates.*;
import me.matej.Vertigo.WorldCreator.States.WorldState;

/**
 *
 * @author matejkramny
 */
public enum GameStateEnum {
	World (new WorldState());

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
