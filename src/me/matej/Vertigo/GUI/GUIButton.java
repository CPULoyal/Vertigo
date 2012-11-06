package me.matej.Vertigo.GUI;

import java.io.IOException;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.SoundManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
public class GUIButton extends Entity {
	private GUIBorder border;
	private TrueTypeFont font;
	private Color fontColor;
	private String text;
	private Color hoverColor;
	private Entity mouse = new Entity(new Vector(0, 0), new SizeVector(3, 3));
	private boolean isHoverState = false;
	public GUIEventInterface delegate;

	@Override
	public void draw () {
		if (border != null)
			border.draw();

		if (isHoverState) {
			Color c = color; // Keeping the reference safe from garbage collector
			color = hoverColor;
			super.draw();
			color = c; // Restore the color
		} else
			super.draw();

		if (text != null && text.length() != 0) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			if (font == null)
				font = Main.getOpenGL().getFont();
			int fwidth = font.getWidth(text);
			font.drawString((float)(loc.x+size.w/2 - fwidth/2), (float)(loc.y+size.h/2 - font.getHeight(text)/2), text, fontColor);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}

	public void mouseButtonPressed (int index) {
		if (delegate != null) {
			mouse.loc.x = Mouse.getX();
			mouse.loc.y = OpenGL.getDisplayMode().getHeight() - Mouse.getY();

			if (index == 0 && mouse.basicCollide(this)) {
				delegate.mouseClicked(this, index);
				SoundManager.getSingleton().getClick().playAsSoundEffect(1f, 1f, false);
			}
		}

	}

	public void update (int delta) {
		mouse.loc.x = Mouse.getX();
		mouse.loc.y = OpenGL.getDisplayMode().getHeight() - Mouse.getY();

		if (mouse.basicCollide(this))
			isHoverState = true;
		else
			isHoverState = false;
	}

	public GUIButton () {
		super();
	}

	public void setColor (Color newColor) {
		this.color = newColor;
	}
	public void setHoverColor (Color newColor) {
		this.hoverColor = newColor;
	}

	public GUIBorder getBorder() {
		return border;
	}

	public void setBorder(GUIBorder border) {
		this.border = border;
	}

	public TrueTypeFont getFont() {
		return font;
	}

	public void setFont(TrueTypeFont font) {
		this.font = font;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public Color getHoverColor() {
		return hoverColor;
	}
}