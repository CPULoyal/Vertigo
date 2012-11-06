package me.matej.Vertigo.GameStates;

import java.awt.Font;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.GUI;
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
public class MainMenuState extends GameStateClass {

	// TODO GUI
	private GUI startButton;
	private GUI startButtonBg;
	private TrueTypeFont font;

	private static final String startText = "Start Game";
	private static int width;

	@Override
	public void draw() {
		startButtonBg.draw();
		startButton.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font.drawString((float)(startButton.loc.x+startButton.size.w/2 - width/2), (float)(startButton.loc.y+startButton.size.h/2 - font.getLineHeight()/2), startText, Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private Entity mouse = new Entity(new Vector(), new SizeVector(5,5), 0, 0, 0, 0);
	@Override
	public void update(int delta) {
		mouse.loc.x = Mouse.getX(); mouse.loc.y = Mouse.getY();
		if (mouse.basicCollide(startButton)) {
			// Mouse is hovering over the start button
			startButton.r = 0.3f;
		} else {
			startButton.r = 0;
		}
	}

	@Override
	public void keyPressed(int key) {

	}

	@Override
	public void mouseButtonPressed(int index) {
		if (index != 0) return;

		mouse.loc.x = Mouse.getX(); mouse.loc.y = Mouse.getY();
		if (mouse.basicCollide(startButton)) {
			this.active = false;
			GameStateEnum.Game.getStateInstance().init(); GameStateEnum.Game.getStateInstance().active = true; // Switch to next scene..
		}
	}

	@Override
	public void init() {
		Font awtFont = new Font("Arial", Font.BOLD, 20);
		font = new TrueTypeFont (awtFont, true);
		width = font.getWidth(startText);

		DisplayMode dm = OpenGL.getDisplayMode();
		startButton = new GUI();
		startButton.size = new SizeVector(200, 30);
		startButton.loc = new Vector(dm.getWidth()/2-startButton.size.w/2, dm.getHeight()/2-startButton.size.h/2);
		startButton.r = 0; startButton.g = 1; startButton.b = 0; startButton.a = 1;
		startButton.rot = 0;

		startButtonBg = new GUI();
		startButtonBg.size = new SizeVector(210, 40);
		startButtonBg.loc = new Vector(dm.getWidth()/2-startButtonBg.size.w/2, dm.getHeight()/2-startButtonBg.size.h/2);
		startButtonBg.r = 0; startButtonBg.g = 0.8f; startButtonBg.b = 0.2f; startButtonBg.a = 1;
		startButtonBg.rot = 0;
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		init();
	}

}
