package me.matej.Vertigo;

import java.io.IOException;
import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author matejkramny
 */
public class Main {

	private static Main singleton; // Singleton instance

	private static OpenGL openGL;

	public boolean gamePaused = true;

	protected Main () { } // Prevents instantiation

	public static Main getInstance () {
		if (singleton == null)
			singleton = new Main();

		return singleton;
	}

	private void run () {
		openGL = new OpenGL();

		try {
			SoundManager.getSingleton().loadExplosion();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		GameStateEnum.MainMenu.getStateInstance().init();
		GameStateEnum.MainMenu.getStateInstance().active = true;

		//GameStateEnum.Game.getStateInstance().init();
		//GameStateEnum.Game.getStateInstance().active = true;

		openGL.startLoop();
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

	public void update (int delta) {
		for (int i = 0; i < GameStateEnum.values().length; i++) {
			GameStateEnum state = GameStateEnum.values()[i];
			if (state.getStateInstance().active) {
				state.getStateInstance().update(delta);
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
