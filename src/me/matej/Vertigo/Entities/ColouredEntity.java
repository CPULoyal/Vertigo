package me.matej.Vertigo.Entities;

import me.matej.Vertigo.Drawable;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class ColouredEntity extends Entity implements Drawable {
	public Color color;

	public ColouredEntity () {
		super();
		color = Color.black;
	}

	public ColouredEntity (Color color) {
		super();
		this.color = color;
	}

	public ColouredEntity (Vector loc, SizeVector size, Color color) {
		super(loc, size);
		this.color = color;
	}

	public void draw() {
		this.drawBegin();
		this.drawVertices();
		this.drawEnd();
	}

	public void drawBegin() {
		GL11.glLoadIdentity();
		GL11.glPushMatrix();
		GL11.glColor4f(color.r, color.g, color.b, color.a);
	}

	public void drawEnd() {
		GL11.glPopMatrix();
	}

	public void drawVertices() {
		GL11.glTranslated(loc.x, loc.y, 0);
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(0, size.h);
			GL11.glVertex2d(size.w, size.h);
			GL11.glVertex2d(size.w, 0);
		}
		GL11.glEnd();
	}

	@Override
	public String toString() {
		return String.format("%s Red:%.3f Green:%.3f Blue:%.3f Alpha:%.3f", super.toString(), color.r, color.g, color.b, color.a);
	}
}
