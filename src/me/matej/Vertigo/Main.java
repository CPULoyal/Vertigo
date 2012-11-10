package me.matej.Vertigo;

import java.awt.Font;
import java.io.IOException;
import me.matej.Vertigo.GameStates.GameStateClass;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
public class Main {

	private static Main singleton; // Singleton instance

	private static OpenGL openGL;

	public static TrueTypeFont buttonFont;
	public static TrueTypeFont headerFont;

	protected Main () { } // Prevents instantiation

	public static Main getInstance () {
		if (singleton == null)
			singleton = new Main();

		return singleton;
	}

	private void run () {
		openGL = new OpenGL();

		Font awtFont = new Font("Arial", Font.BOLD, 20);
		buttonFont = new TrueTypeFont (awtFont, true);
		awtFont = new Font("Arial", Font.BOLD, 25);
		headerFont = new TrueTypeFont (awtFont, true);

		try {
			SoundManager.getSingleton().loadExplosion();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		activateState(GameStateEnum.MainMenu);

		openGL.startLoop();
	}

	public void activateState (GameStateEnum state) {
		GameStateClass stateClass = state.getStateInstance();

		stateClass.wantsToBeActive = true;
		stateClass.wantsToResignActive = false;

		if (!stateClass.didInit)
			stateClass.init();
	}
	public void deactivateState (GameStateEnum state) {
		if (!state.getStateInstance().wantsToResignActive) {
			state.getStateInstance().wantsToResignActive = true;
		}
	}
	public void changeState (GameStateEnum newState, GameStateEnum oldState) {
		activateState(newState);
		deactivateState(oldState);
	}

	public void draw () {
		for (int i = 0; i < GameStateEnum.values().length; i++) {
			GameStateEnum state = GameStateEnum.values()[i];
			if (state.getStateInstance().active) {
				state.getStateInstance().draw();
				break;
			}
		}
	}

	public void preUpdate () {
		for (GameStateEnum state : GameStateEnum.values()) {
			GameStateClass stateClass = state.getStateInstance();

			if (stateClass.wantsToBeActive) {
				stateClass.active = true;
				stateClass.wantsToBeActive = false;
			}

			if (stateClass.wantsToResignActive) {
				stateClass.wantsToResignActive = false;
				stateClass.active = false;
			}
		}
	}

	public void update (int delta) {
		for (GameStateEnum state : GameStateEnum.values()) {
			GameStateClass stateClass = state.getStateInstance();

			if (stateClass.active) {
				stateClass.update(delta);
				break;
			}
		}
	}

	public void displayModeChanged (DisplayMode newDisplayMode) {
		for (GameStateEnum state : GameStateEnum.values()) {
			if (state.getStateInstance().active) {
				state.getStateInstance().displayModeChanged(newDisplayMode);
			}
		}
	}

	public static OpenGL getOpenGL () {
		return openGL;
	}

	public static void main (String[] args) {
		Main.getInstance().run(); // Creates singleton and starts
	}

}
