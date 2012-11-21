package me.matej.Vertigo.GameStates;

import org.lwjgl.opengl.DisplayMode;

/**
 * @author matejkramny
 */
public abstract class GameStateClass {
	public boolean active = false;
	public boolean wantsToResignActive = false; // Loop automatically clears this before update()
	public boolean wantsToBeActive = false; // ^
	public boolean didInit = false; // Prevents init() twice

	public abstract void draw();

	public abstract void update(int delta);

	public abstract void keyPressed(int key);

	public abstract void mouseButtonPressed(int index);

	public abstract void init();

	public abstract void displayModeChanged(DisplayMode newDisplayMode);
}
