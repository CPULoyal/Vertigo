package me.matej.Vertigo.WorldCreator;

import java.awt.Font;
import java.io.File;

import me.matej.Vertigo.GameStates.GameStateClass;
import me.matej.Vertigo.WebService.ConnectionWrapper;
import me.matej.Vertigo.WebService.SocketConnection;
import me.matej.Vertigo.WebService.WorldListing;
import me.matej.Vertigo.World.World;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.TrueTypeFont;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.OpenGLDelegate;

/**
 * @author matejkramny
 */
public class Main implements OpenGLDelegate, Runnable {

	private static Main singleton; // Singleton instance

	private static OpenGL openGL;

	public static TrueTypeFont buttonFont;
	public static TrueTypeFont headerFont;

	public static final String appName = "vertigoWorldCreator";
	public static final String displayName = "Vertigo World Creator";
	public static final String version = "0.0.01";

	private me.matej.Vertigo.Main gameThread; // At some point the worldCreator starts the game..

	protected Main() {
	} // Prevents instantiation

	public static Main getInstance() {
		if (singleton == null)
			singleton = new Main();

		return singleton;
	}

	public me.matej.Vertigo.Main getGameThread() {
		return gameThread;
	}

	public void startGameThread() {
		// TODO start this thread as new JVM

		gameThread = me.matej.Vertigo.Main.getInstance(); // The singleton
		(new Thread(gameThread)).start();
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
		openGL = new OpenGL(this);
		openGL.setIgnoresDefaultKeypress(true);

		Font awtFont = new Font("Arial", Font.BOLD, 20);
		buttonFont = new TrueTypeFont(awtFont, true);
		awtFont = new Font("Arial", Font.BOLD, 25);
		headerFont = new TrueTypeFont(awtFont, true);

		activateState(GameStateEnum.World);

		openGL.startLoop();
	}

	public void activateState(GameStateEnum state) {
		GameStateClass stateClass = state.getStateInstance();

		stateClass.wantsToBeActive = true;
		stateClass.wantsToResignActive = false;

		if (!stateClass.didInit)
			stateClass.init();
	}

	public void deactivateState(GameStateEnum state) {
		if (!state.getStateInstance().wantsToResignActive) {
			state.getStateInstance().wantsToResignActive = true;
		}
	}

	public void changeState(GameStateEnum newState, GameStateEnum oldState) {
		activateState(newState);
		deactivateState(oldState);
	}

	@Override
	public void draw() {
		for (int i = 0; i < GameStateEnum.values().length; i++) {
			GameStateEnum state = GameStateEnum.values()[i];
			if (state.getStateInstance().active) {
				state.getStateInstance().draw();
				break;
			}
		}
	}

	@Override
	public void preUpdate() {
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
	public void update(int delta) {
		for (GameStateEnum state : GameStateEnum.values()) {
			GameStateClass stateClass = state.getStateInstance();

			if (stateClass.active) {
				stateClass.update(delta);
				break;
			}
		}
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		for (GameStateEnum state : GameStateEnum.values()) {
			state.getStateInstance().displayModeChanged(newDisplayMode);
		}
	}

	public static OpenGL getOpenGL() {
		return openGL;
	}

	public static void main(String[] args) {
		Main.getInstance().run(); // Creates singleton and starts
	}

	@Override
	public void activateState(me.matej.Vertigo.GameStateEnum state) {
		GameStateClass stateClass = state.getStateInstance();

		stateClass.wantsToBeActive = true;
		stateClass.wantsToResignActive = false;

		if (!stateClass.didInit)
			stateClass.init();
	}

	@Override
	public void deactivateState(me.matej.Vertigo.GameStateEnum state) {
		if (!state.getStateInstance().wantsToResignActive) {
			state.getStateInstance().wantsToResignActive = true;
		}
	}

	@Override
	public void changeState(me.matej.Vertigo.GameStateEnum newState, me.matej.Vertigo.GameStateEnum oldState) {
		activateState(newState);
		deactivateState(oldState);
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
