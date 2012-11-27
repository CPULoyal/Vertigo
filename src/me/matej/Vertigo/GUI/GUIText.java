package me.matej.Vertigo.GUI;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.Main;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 * @author matejkramny
 */
public class GUIText extends Entity {
	private String text;
	private TrueTypeFont font;
	public Color fontColor;

	public GUIText(Vector loc, String text) {
		this(loc, text, Main.getOpenGL().getFont());
	}

	public GUIText(Vector loc, String text, TrueTypeFont font) {
		this(loc, text, font, Color.black);
	}

	public GUIText(Vector loc, String text, TrueTypeFont font, Color color) {
		super();
		this.loc = loc;
		this.size = new SizeVector(font.getWidth(text), font.getHeight(text));
		this.loc.x -= this.size.w / 2;
		this.loc.y -= this.size.h / 2;
		this.fontColor = color;
		this.font = font;
		this.text = text;
	}

	public void draw() {
		GL11.glLoadIdentity();
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if (this.fontColor != null)
			font.drawString(0, 0, text, fontColor);
		else
			font.drawString(0, 0, text, Color.black);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}
}
