package me.matej.Vertigo;

import me.matej.Vertigo.GameStates.GameStateClass;
import org.lwjgl.opengl.DisplayMode;

import java.util.HashMap;

/**
 * @author matejkramny
 */
public interface OpenGLDelegate {
	public void activateState(GameStateClass state);

	public void deactivateState(GameStateClass state);

	public void changeState(GameStateClass newState, GameStateClass oldState);

	public void draw();

	public void update(int delta);

	public void preUpdate();

	public void displayModeChanged(DisplayMode newDisplayMode);

	public void keyPressed(int key);

	public HashMap<String, GameStateClass> getStates ();
}
