package me.matej.Vertigo.GameStates;

import me.matej.Vertigo.Entities.ColouredEntity;
import me.matej.Vertigo.GUI.GUIText;

import java.util.HashMap;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameMain;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * @author matejkramny
 */
public class GameMenuState extends GameStateClass implements GUIEventInterface {

	private ColouredEntity background;
	private ColouredEntity buttonsBackground;
	private HashMap<String, GUIButton> buttons;
	private GUIText gamePausedText;

	@Override
	public void draw() {
		background.draw();
		buttonsBackground.draw();
		gamePausedText.draw();
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
		if (key == Keyboard.KEY_ESCAPE) {
			((GameState) GameMain.states.get("game")).setPaused(false);
		}
	}

	@Override
	public void mouseButtonPressed(int index) {
		for (GUIButton button : buttons.values())
			button.mouseButtonPressed(index);
	}

	@Override
	public void init() {
		this.didInit = true;

		TrueTypeFont font = GameMain.buttonFont;

		DisplayMode dm = OpenGL.getDisplayMode();
		background = new ColouredEntity(new Vector(0, 0), new SizeVector(dm), new Color(0.1f, 0.1f, 0.1f, 0.4f));
		buttonsBackground = new ColouredEntity(new Vector(dm, new SizeVector(SizeVector.buttonBorderSize.w + 70, SizeVector.buttonBorderSize.h * 4 + 20)), new SizeVector(SizeVector.buttonBorderSize.w + 70, SizeVector.buttonBorderSize.h * 4 + 70), new Color(0.1f, 0.1f, 0.1f));

		buttons = new HashMap<String, GUIButton>();
		buttons.put("exit", new GUIButton("Main menu", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, 100.0), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this));
		buttons.get("exit").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, 100), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));
		buttons.put("close", new GUIButton("Continue", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, -20), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this));
		buttons.get("close").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, -20), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));
		buttons.put("options", new GUIButton("Options", Color.black, font, new Vector(dm, SizeVector.buttonSize, 0.0, 40), SizeVector.buttonSize, Color.green, new Color(0.5f, 1f, 0f), this));
		buttons.get("options").getBorder().configure(new Vector(dm, SizeVector.buttonBorderSize, 0, 40), SizeVector.buttonBorderSize, new Color(0.2f, 0.8f, 0f));

		gamePausedText = new GUIText(new Vector(dm, 0, -70), "Game Paused", GameMain.headerFont, Color.white);
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		this.init();
	}

	@Override
	public void mouseClicked(Entity element, int index) {
		if (element.equals(buttons.get("exit")) && index == 0) {
			GameMain.instance().changeState(GameMain.states.get("mainMenu"), GameMain.states.get("game"));
			((GameState) GameMain.states.get("game")).setPaused(false);
		} else if (element.equals(buttons.get("close")) && index == 0) {
			((GameState) GameMain.states.get("game")).setPaused(false);
		} else if (element.equals(buttons.get("options")) && index == 0) {
			// Display options state
		}
	}

}
