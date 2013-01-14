package me.matej.Vertigo.GameStates;

import java.util.HashMap;

import me.matej.Vertigo.Entities.ColouredEntity;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameMain;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * @author matejkramny
 */
public class MainMenuState extends GameStateClass implements GUIEventInterface {

	private HashMap<String, GUIButton> buttons;
	private TrueTypeFont font;

	private ColouredEntity background;

	@Override
	public void draw() {
		background.draw();
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
	public void mouseButtonPressed(int index) {
		for (GUIButton button : buttons.values())
			button.mouseButtonPressed(index);
	}

	// Implements from GUIEventInterface
	@Override
	public void mouseClicked(Entity o, int index) {
		if (o.equals(buttons.get("worlds"))) {
			GameMain.instance().changeState(GameMain.states.get("worlds"), GameMain.states.get("mainMenu"));
		} else if (o.equals(buttons.get("options"))) {
			// Activate Options screen
			active = false;
			GameMain.states.get("options").active = true;
			GameMain.states.get("options").init();
		} else if (o.equals(buttons.get("quit"))) {
			System.exit(0);
		}
	}

	@Override
	public void init() {
		this.didInit = true;

		font = GameMain.buttonFont;

		DisplayMode dm = OpenGL.getDisplayMode();
		buttons = new HashMap<String, GUIButton>();

		background = new ColouredEntity(new Vector(0, 0), new SizeVector(dm.getWidth(), dm.getHeight()), new Color(0f, 0f, 1f));

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
