package me.matej.Vertigo.GameStates;

import java.awt.Font;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI.GUIBorder;
import me.matej.Vertigo.GUI.GUIButton;
import me.matej.Vertigo.GUI.GUIEventInterface;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
public class MainMenuState extends GameStateClass implements GUIEventInterface {

	// TODO GUI
	private GUIButton startButton;
	private TrueTypeFont font;

	private static final String startText = "Start Game";

	private boolean continueToGame = false; // Flags whether mainMenu sets game to active mode

	@Override
	public void draw() {
		startButton.draw();
	}

	private Entity mouse = new Entity(new Vector(), new SizeVector(5,5), Color.transparent);
	@Override
	public void update(int delta) {
		if (continueToGame) {
			this.active = false;
			GameStateEnum.Game.getStateInstance().init(); GameStateEnum.Game.getStateInstance().active = true; // Switch to next scene..
			return;
		}

		startButton.update(delta);
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void mouseButtonPressed(int index) {
		startButton.mouseButtonPressed(index);
	}

	// Implements from GUIEventInterface
	@Override
	public void mouseClicked (Entity o, int index) {
		if (o.equals(startButton)) {
			this.continueToGame = true;
		}
	}

	@Override
	public void init() {
		Font awtFont = new Font("Arial", Font.BOLD, 20);
		font = new TrueTypeFont (awtFont, true);

		DisplayMode dm = OpenGL.getDisplayMode();
		startButton = new GUIButton();
		startButton.setText(startText);
		startButton.setFont(font);
		startButton.setFontColor(Color.black);
		startButton.size = new SizeVector(200, 40);
		startButton.loc = new Vector(dm.getWidth()/2-startButton.size.w/2, dm.getHeight()/2-startButton.size.h/2 - startButton.size.h - 100);
		startButton.setColor(Color.green);
		startButton.setHoverColor(new Color(0.5f, 1f, 0f));
		startButton.rot = 0;
		startButton.delegate = this;

		GUIBorder border = new GUIBorder();
		border.size = new SizeVector(startButton.size.w+10, startButton.size.h+10);
		border.loc = new Vector(startButton.loc.x-5, startButton.loc.y-5);
		border.color = new Color(0.2f, 0.8f, 0f);
		startButton.setBorder(border);
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		init();
	}

}
