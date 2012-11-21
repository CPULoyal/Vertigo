package me.matej.Vertigo;

import org.lwjgl.opengl.DisplayMode;

/**
 * @author matejkramny
 */
public interface OpenGLDelegate {
	public void activateState(GameStateEnum state);

	public void deactivateState(GameStateEnum state);

	public void changeState(GameStateEnum newState, GameStateEnum oldState);

	public void draw();

	public void update(int delta);

	public void preUpdate();

	public void displayModeChanged(DisplayMode newDisplayMode);

	public void keyPressed(int key);
}
