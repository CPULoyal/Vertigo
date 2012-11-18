package me.matej.Vertigo;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import me.matej.Vertigo.GameStates.GameStateClass;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
public class Main implements OpenGLDelegate, Runnable {

	private static Main singleton; // Singleton instance

	private static OpenGL openGL;

	public static TrueTypeFont buttonFont;
	public static TrueTypeFont headerFont;

	public static final String appName = "vertigo";
	public static final String displayName = "Vertigo";
	public static final String version = "0.0.01";

	private String[] args;

	protected Main () { } // Prevents instantiation

	public static Main getInstance () {
		if (singleton == null)
			singleton = new Main();

		return singleton;
	}

	public static String getSaveDir () {
		String saveDir = System.getProperty("user.home") + File.separator + ".vertigo" + File.separator;

		File dirFile = new File(saveDir);
		if (!dirFile.exists()) {
			// Does not exist -- create
			dirFile.mkdir();
		}

		return saveDir;
	}

	@Override
	public void run () {
		openGL = new OpenGL(this);

		Font awtFont = new Font("Arial", Font.BOLD, 20);
		buttonFont = new TrueTypeFont (awtFont, true);
		awtFont = new Font("Arial", Font.BOLD, 25);
		headerFont = new TrueTypeFont (awtFont, true);

		try {
			SoundManager.getSingleton().loadExplosion();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		boolean noSplash = false, noMainMenu = false;
		if (args != null) {
			for (String arg : args) {
				if ("--nosplash".equals(arg))
					noSplash = true;
				else if ("--nomenu".equals(arg))
					noMainMenu = true;
			}
		}

		if (noMainMenu) {
			activateState(GameStateEnum.Game);
			System.out.println("Skipping MainMenu");
		} else if (noSplash) {
			activateState(GameStateEnum.MainMenu);
			System.out.println("Skipping Splash screen");
		} else {
			activateState(GameStateEnum.Splash);
		}

		openGL.startLoop();
	}

	@Override
	public void activateState (GameStateEnum state) {
		GameStateClass stateClass = state.getStateInstance();

		stateClass.wantsToBeActive = true;
		stateClass.wantsToResignActive = false;

		if (!stateClass.didInit)
			stateClass.init();
	}
	@Override
	public void deactivateState (GameStateEnum state) {
		if (!state.getStateInstance().wantsToResignActive) {
			state.getStateInstance().wantsToResignActive = true;
		}
	}
	@Override
	public void changeState (GameStateEnum newState, GameStateEnum oldState) {
		activateState(newState);
		deactivateState(oldState);
	}

	@Override
	public void draw () {
		for (int i = 0; i < GameStateEnum.values().length; i++) {
			GameStateEnum state = GameStateEnum.values()[i];
			if (state.getStateInstance().active) {
				state.getStateInstance().draw();
				break;
			}
		}
	}

	@Override
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

	@Override
	public void update (int delta) {
		for (GameStateEnum state : GameStateEnum.values()) {
			GameStateClass stateClass = state.getStateInstance();

			if (stateClass.active) {
				stateClass.update(delta);
				break;
			}
		}
	}

	@Override
	public void displayModeChanged (DisplayMode newDisplayMode) {
		for (GameStateEnum state : GameStateEnum.values()) {
			state.getStateInstance().displayModeChanged(newDisplayMode);
		}
	}

	public static OpenGL getOpenGL () {
		return openGL;
	}

	public static void main (String[] args) {
		Main main = Main.getInstance();
		main.args = args;
		main.run(); // Creates singleton and starts
	}

	@Override
	public void keyPressed(int key) {
		for (int i = 0; i < GameStateEnum.values().length; i++) {
			GameStateEnum state = GameStateEnum.values()[i];
			if (state.getStateInstance().active) {
				state.getStateInstance().keyPressed(key);
			}
		}
	}

}
