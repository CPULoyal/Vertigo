package me.matej.Vertigo.Entities;

import java.awt.Rectangle;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author matejkramny
 */
public class Entity {
	public Vector loc; // location
	public SizeVector size; // width and height
	public double rot; // Rotation angle
	public float r, g, b, a; // Colour

	public boolean centeredRot = true;
	private Rectangle me;
	private Rectangle him;

	public void draw () {
		this.drawBegin();
		this.rotate();
		this.color();
		this.drawVerts();
		this.drawEnd();
	}

	protected void drawBegin () {
		GL11.glLoadIdentity();
		GL11.glPushMatrix();
	}
	protected void drawEnd() {
		GL11.glPopMatrix();
	}
	protected void rotate () {
		// Centered rotation
		if (centeredRot) {
			GL11.glTranslated(loc.x+size.w/2, loc.y+size.h/2, 0);
			GL11.glRotated(rot, 0d, 0d, 1d);
			GL11.glTranslated(-size.w/2, -size.h/2, 0);
		} else {
			// Rotation from x
			GL11.glTranslated(loc.x, loc.y+size.h/2, 0);
			GL11.glRotated(rot, 0, 0, 1);
			GL11.glTranslated(0, -size.h/2, 0);
		}
	}
	protected void color () {
		GL11.glColor4f(r, g, b, a);
	}
	protected void drawVerts () {
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glVertex2d(0, 0);
			GL11.glVertex2d(0, size.h);
			GL11.glVertex2d(size.w, size.h);
			GL11.glVertex2d(size.w, 0);
		}
		GL11.glEnd();
	}
	public boolean basicCollide (Entity other) {
		me.setBounds((int)loc.x, (int)loc.y, (int)size.w, (int)size.h);
		him.setBounds((int)other.loc.x, (int)other.loc.y, (int)other.size.w, (int)other.size.h);

		if (me.intersects(him))
			return true;

		return false;
	}

	public Entity () {
		this(new Vector(0, 0), new SizeVector(0,0));
	}
	public Entity (Vector v, SizeVector s) {
		this(v, s, 0, 0, 0);
	}
	public Entity (Vector v, SizeVector s, float r, float g, float b) {
		this(v, s, r, g, b, 1);
	}
	public Entity (Vector v, SizeVector s, float r, float g, float b, float a) {
		loc = v;
		size = s;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;

		me = new Rectangle();
		him = new Rectangle();

		resetRot();
	}

	public void resetY () {
		loc.y = (int)((float)OpenGL.getDisplayMode().getHeight() / 2f - (float)size.h / 2f);
	}

	public void resetX () {
		loc.x = (int)((float)OpenGL.getDisplayMode().getWidth() / 2f - (float)size.w / 2f);
	}

	public void resetRot () {
		this.rot = 0f;
	}

}
