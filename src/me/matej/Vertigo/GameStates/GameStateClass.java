package me.matej.Vertigo.GameStates;

import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author matejkramny
 */
public abstract class GameStateClass {
	public boolean active;
	public abstract void draw ();
	public abstract void update (int delta);
	public abstract void keyPressed (int key);
	public abstract void mouseButtonPressed (int index);
	public abstract void init ();
	public abstract void displayModeChanged (DisplayMode newDisplayMode);
}
