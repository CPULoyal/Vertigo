package me.matej.Vertigo;

import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import me.matej.Vertigo.GameStates.*;
//import me.matej.Vertigo.WorldCreator.States.WorldState;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;

/**
 * @author matejkramny
 */
public class GameMain implements OpenGLDelegate, Runnable {

	private static GameMain singleton; // Singleton instance

	public static OpenGL openGL;

	public static TrueTypeFont buttonFont;
	public static TrueTypeFont headerFont;

	public static final String appName = "vertigo";
	public static final String displayName = "Vertigo";
	public static final String version = "0.0.01";

	public static final HashMap<String, GameStateClass> states = new HashMap<String, GameStateClass>();

	static {
		states.put("splashScreen", new SplashScreenState());
		states.put("mainMenu", new MainMenuState());
		states.put("game", new GameState());
		states.put("gameMenu", new GameMenuState());
		states.put("options", new OptionsState());
		states.put("worlds", new WorldsState());
//		states.put("creator", new WorldState()); // WorldCreator.States.WorldState
	}

	private String[] args;

	protected GameMain() {} // Prevents instantiation

	public static GameMain instance() {
		if (singleton == null)
			singleton = new GameMain();

		return singleton;
	}

	public static String getSaveDir() {
		String saveDir = System.getProperty("user.home") + File.separator + ".vertigo" + File.separator;

		File dirFile = new File(saveDir);
		if (!dirFile.exists()) {
			// Does not exist -- create
			dirFile.mkdir();
		}

		return saveDir;
	}

	@Override
	public void run() {
		openGL = new OpenGL();

		Font awtFont = new Font("Arial", Font.BOLD, 20);
		buttonFont = new TrueTypeFont(awtFont, true);
		awtFont = new Font("Arial", Font.BOLD, 25);
		headerFont = new TrueTypeFont(awtFont, true);

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
			activateState(states.get("game"));
			System.out.println("Skipping MainMenu");
		} else if (noSplash) {
			activateState(states.get("mainMenu"));
			System.out.println("Skipping Splash screen");
		} else {
			activateState(states.get("splashScreen"));
		}

		openGL.startLoop();
	}

	@Override
	public void activateState(GameStateClass state) {
		state.wantsToBeActive = true;
		state.wantsToResignActive = false;

		if (!state.didInit)
			state.init();
	}

	@Override
	public void deactivateState(GameStateClass state) {
		if (!state.wantsToResignActive) {
			state.wantsToResignActive = true;
		}
	}

	@Override
	public void changeState(GameStateClass newState, GameStateClass oldState) {
		activateState(newState);
		deactivateState(oldState);
	}

	@Override
	public void draw() {
		for (GameStateClass state : states.values()) {
			if (state.active) {
				state.draw();
				break;
			}
		}
	}

	@Override
	public void preUpdate() {
		for (GameStateClass state : states.values()) {

			if (state.wantsToBeActive) {
				state.active = true;
				state.wantsToBeActive = false;
			}

			if (state.wantsToResignActive) {
				state.wantsToResignActive = false;
				state.active = false;
			}
		}
	}

	@Override
	public void update(int delta) {
		for (GameStateClass state : states.values()) {

			if (state.active) {
				state.update(delta);
				break;
			}
		}
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		for (GameStateClass state : states.values()) {
			state.displayModeChanged(newDisplayMode);
		}
	}

	public static OpenGL getOpenGL() {
		return openGL;
	}

	public static void main(String[] args) {
		if (args.length != 0 && args[0].equals("--noloadnatives")) {
			System.out.println("Skip loading natives");
		} else {
			System.out.println("Loading natives");
			System.setProperty("org.lwjgl.librarypath", System.getProperty("user.dir") + "/libs/natives");
		}

		GameMain main = GameMain.instance();
		main.args = args;
		main.run(); // Creates singleton and starts
	}

	@Override
	public void keyPressed(int key) {
		for (GameStateClass state : states.values()) {
			if (state.active) {
				state.keyPressed(key);
			}
		}
	}

	@Override
	public HashMap<String, GameStateClass> getStates() {
		return states;
	}

}
