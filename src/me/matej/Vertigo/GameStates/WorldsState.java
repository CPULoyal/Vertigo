package me.matej.Vertigo.GameStates;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.WebService.ConnectionWrapper;
import me.matej.Vertigo.WebService.WorldListing;
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
	private HashMap<String, GUIButton> serverButtons;
	private boolean showsServerWorlds = false;

	@Override
	public void draw() {
		if (showsServerWorlds)
			for (GUIButton button : serverButtons.values())
				button.draw();
		else
			for (GUIButton button : buttons.values())
				button.draw();
	}

	@Override
	public void update(int delta) {
		if (showsServerWorlds)
			for (GUIButton button : serverButtons.values())
				button.update(delta);
		else
			for (GUIButton button : buttons.values())
				button.update(delta);
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void mouseClicked(Entity o, int index) {
		if (showsServerWorlds) {
			if (o.equals(serverButtons.get("close"))) {
				showsServerWorlds = false;
			} else {
				for (String key : serverButtons.keySet()) {
					if (key.startsWith("serverWorld") && o.equals(serverButtons.get(key))) {
						try {
							int id = Integer.parseInt(key.substring("serverWorld".length()));
							if (id < worlds.length) { // indexed < non-indexed == indexed <= indexed
								String worldPath = worlds[id];

								GUIButton button = (GUIButton)o;
								ConnectionWrapper wrapper = new ConnectionWrapper();
								WorldListing listing = wrapper.getListing(button.getText());

								clickedWorld = wrapper.getWorld(listing);

								Main.getInstance().changeState(GameStateEnum.Game, GameStateEnum.Worlds);
								((GameState)GameStateEnum.Game.getStateInstance()).setPaused(true);
							}
						} catch (NumberFormatException nfx) {
							nfx.printStackTrace(System.err);
						}
					}
				}
			}
		} else {
			if (o.equals(buttons.get("close"))) {
				Main.getInstance().changeState(GameStateEnum.MainMenu, GameStateEnum.Worlds);
			} else if (o.equals(buttons.get("default"))) {
				Main.getInstance().changeState(GameStateEnum.Game, GameStateEnum.Worlds);
			} else if (o.equals(buttons.get("serverWorlds"))) {
				showsServerWorlds = true;
				if (serverButtons == null) {
					loadServerButtons();
				}
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
								((GameState)GameStateEnum.Game.getStateInstance()).setPaused(true); // If not paused, delta gets too big from blocking IO (loading world) and mario falls through the ground.. TODO Loading screen.. Progress bar?
							}
						} catch (NumberFormatException nfx) {
							nfx.printStackTrace(System.err);
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseButtonPressed(int index) {
		if (showsServerWorlds)
			for (GUIButton button : serverButtons.values())
				button.mouseButtonPressed(index);
		else
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
			GUIButton btn = new GUIButton(worlds[i], Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 55 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
			btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 55 * i), SizeVector.buttonBorderSize, Color.black);
			buttons.put("worldButton"+i, btn);
		}

		GUIButton btn = new GUIButton("Server Worlds", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 70 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 70 * i), SizeVector.buttonBorderSize, Color.black);
		buttons.put("serverWorlds", btn);

		i++;

		btn = new GUIButton("Close", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 65 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 65 * i), SizeVector.buttonBorderSize, Color.black);
		buttons.put("close", btn);
	}

	public void loadServerButtons () {
		ConnectionWrapper wrapper = new ConnectionWrapper();
		String[] listings = wrapper.getListingIDs();
		DisplayMode dm = OpenGL.getDisplayMode();
		serverButtons = new HashMap<String, GUIButton>();

		int baseY = -100;
		int i;
		for (i = 0; i < listings.length; i++) {
			GUIButton btn = new GUIButton(listings[i], Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 70 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
			btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 70 * i), SizeVector.buttonBorderSize, Color.black);
			serverButtons.put("serverWorld"+i, btn);
		}

		GUIButton btn = new GUIButton("Local worlds", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0, baseY + 65 * i), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		btn.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, baseY + 65 * i), SizeVector.buttonBorderSize, Color.black);
		serverButtons.put("close", btn);
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
	}

	public World getWorld () {
		assert clickedWorld != null : "Clicked world null!";

		return clickedWorld;
	}

}
