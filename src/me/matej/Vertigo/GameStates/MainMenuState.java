package me.matej.Vertigo.GameStates;

import java.util.HashMap;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import me.matej.Vertigo.Main;

/**
 * @author matejkramny
 */
public class MainMenuState extends GameStateClass implements GUIEventInterface {

	private HashMap<String, GUIButton> buttons;
	private TrueTypeFont font;

	private Entity background;

	@Override
	public void draw() {
		background.draw();
		for (GUIButton button : buttons.values())
			button.draw();
	}

	private Entity mouse = new Entity(new Vector(), new SizeVector(5, 5), Color.transparent);

	@Override
	public void update(int delta) {
		for (GUIButton button : buttons.values())
			button.update(delta);
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void mouseButtonPressed(int index) {
		for (GUIButton button : buttons.values())
			button.mouseButtonPressed(index);
	}

	// Implements from GUIEventInterface
	@Override
	public void mouseClicked(Entity o, int index) {
		if (o.equals(buttons.get("worlds"))) {
			Main.getInstance().changeState(GameStateEnum.Worlds, GameStateEnum.MainMenu);
		} else if (o.equals(buttons.get("options"))) {
			// Activate Options screen
			active = false;
			GameStateEnum.Options.getStateInstance().active = true;
			GameStateEnum.Options.getStateInstance().init();
		} else if (o.equals(buttons.get("quit"))) {
			System.exit(0);
		}
	}

	@Override
	public void init() {
		this.didInit = true;

		font = Main.buttonFont;

		DisplayMode dm = OpenGL.getDisplayMode();
		buttons = new HashMap<String, GUIButton>();

		background = new Entity(new Vector(0, 0), new SizeVector(dm.getWidth(), dm.getHeight()), new Color(0f, 0f, 1f));

		GUIButton b = new GUIButton("Worlds", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, -100), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		buttons.put("worlds", b);
		b = new GUIButton("Options", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, -40), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		buttons.put("options", b);
		b = new GUIButton("Exit", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, 20), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		buttons.put("quit", b);

		buttons.get("worlds").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, -100), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));
		buttons.get("options").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, -40), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));
		buttons.get("quit").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, 20), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		init();
	}

}
