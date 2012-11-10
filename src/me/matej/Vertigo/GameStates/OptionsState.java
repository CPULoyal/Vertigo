package me.matej.Vertigo.GameStates;

import java.util.HashMap;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
public class OptionsState extends GameStateClass implements GUIEventInterface {

	private HashMap<String, GUIButton> buttons;

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
			Main.getInstance().changeState(GameStateEnum.MainMenu, GameStateEnum.Options);
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

		GUIButton close = new GUIButton(closeText, Color.black, font, new Vector(dm, SizeVector.buttonSize), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this);
		close.getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize), SizeVector.buttonBorderSize, Color.black);
		buttons.put("close", close);
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
	}

}
