package me.matej.Vertigo.GameStates;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.World.World;
import me.matej.Vertigo.World.WorldLoader;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author matejkramny
 */
public class WorldsState extends GameStateClass implements GUIEventInterface {

	private HashMap<String, GUIButton> buttons;
	private String[] worlds;
	private World clickedWorld;

	@Override
	public void draw() {
		for (GUIButton button : buttons.values())
			button.draw();
	}

	@Override
	public void update(int delta) {
		for (GUIButton button : buttons.values())
			button.update(delta);
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void mouseClicked(Entity o, int index) {
		if (o.equals(buttons.get("close"))) {
			Main.getInstance().changeState(GameStateEnum.MainMenu, GameStateEnum.Worlds);
		} else if (o.equals(buttons.get("default"))) {
			Main.getInstance().changeState(GameStateEnum.Game, GameStateEnum.Worlds);
		} else {
			for (String key : buttons.keySet()) {
				if (key.startsWith("worldButton") && o.equals(buttons.get(key))) {
					// Then get the id of the world..
					try {
						int id = Integer.parseInt(key.substring("worldButton".length()));
						if (id < worlds.length) { // indexed < non-indexed == indexed <= indexed
							String worldPath = worlds[id];

							// Load this world
							clickedWorld = WorldLoader.loadWorld(Main.getSaveDir()+worldPath);

							assert clickedWorld != null : "World not loaded";

							Main.getInstance().changeState(GameStateEnum.Game, GameStateEnum.Worlds);
						}
					} catch (NumberFormatException nfx) {
						nfx.printStackTrace(System.err);
					}
				}
			}
		}

	}

	@Override
	public void mouseButtonPressed(int index) {
		for (GUIButton button : buttons.values())
			button.mouseButtonPressed(index);
	}

	private final static String closeText = "Close";
	private TrueTypeFont font;

	@Override
	public void init() {
		this.didInit = true;

		font = Main.buttonFont;

		buttons = new HashMap<String, GUIButton>();
		DisplayMode dm = OpenGL.getDisplayMode();

		worlds = WorldLoader.getWorlds();
		double baseY = -100;
		int i;
		for (i = 0; i < worlds.length; i++) {
			GUIButton btn = new GUIButton(worlds[i], Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 60 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
			btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 60 * i), SizeVector.buttonBorderSize, Color.black);
			buttons.put("worldButton"+i, btn);
		}

		GUIButton btn = new GUIButton("Close", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 100 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 100 * i), SizeVector.buttonBorderSize, Color.black);
		buttons.put("close", btn);
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
	}

	public World getWorld () {
		assert clickedWorld != null : "Clicked world null!";

		return clickedWorld;
	}

}
