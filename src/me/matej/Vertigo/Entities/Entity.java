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
	public boolean basicCollide (Entity o) {
		if ((o.loc.x > loc.x && o.loc.x < loc.x+size.w && o.loc.y > loc.y && o.loc.y < loc.y+size.h) ||
			(o.loc.x+o.size.w > loc.x && o.loc.x+o.size.w < loc.x+size.w && o.loc.y > loc.y && o.loc.y < loc.y+size.h) ||
			(o.loc.x > loc.x && o.loc.x < loc.x+size.w && o.loc.y+o.size.h > loc.y && o.loc.y+o.size.h < loc.y+size.h) ||
			(o.loc.x+o.size.w > loc.x && o.loc.x+o.size.w < loc.x+size.w && o.loc.y+o.size.w > loc.y && o.loc.y+o.size.h < loc.y+size.h) ||
			(loc.x > o.loc.x && loc.x < o.loc.x+o.size.w && loc.y > o.loc.y && loc.y < o.loc.y+o.size.h) ||
			(loc.x+size.w > o.loc.x && loc.x+size.w < o.loc.x+o.size.w && loc.y > o.loc.y && loc.y < o.loc.y+o.size.h) ||
			(loc.x > o.loc.x && loc.x < o.loc.x+o.size.w && loc.y+size.h > o.loc.y && loc.y+size.h < o.loc.y+o.size.h) ||
			(loc.x+size.w > o.loc.x && loc.x+size.w < o.loc.x+o.size.w && loc.y+size.h > o.loc.y && loc.y+size.h < o.loc.y+o.size.h)) {
			return true;
		}

		return false;
	}

	public Vector getNonCollisionVector (Entity o) {
		Vector v = new Vector();

		double noOffsetX = o.loc.x;
		Vector oldLoc = new Vector (loc.x, loc.y);

		if (o instanceof Obstacle)
			o.loc.x += ((Obstacle)o).xOffset;

		boolean colTop = false, colBottom = false, colLeft = false, colRight = false;

		if (((loc.x > o.loc.x && loc.x < o.loc.x+o.size.w) || (loc.x+size.w > o.loc.x && loc.x+size.w < o.loc.x+o.size.w)) && loc.y+size.h > o.loc.y && loc.y+size.h < o.loc.y+o.size.h) {
			loc.y = o.loc.y - size.h; // Bottom
			colBottom = true;
		} else if (((loc.x > o.loc.x && loc.x < o.loc.x+o.size.w) || (loc.x+size.w > o.loc.x && loc.x+size.w < o.loc.x+o.size.w)) && loc.y > o.loc.y && loc.y < o.loc.y+o.size.h) {
			loc.y = o.loc.y + o.size.h; // Top

			colTop = true;
		}

		if (loc.x+size.w > o.loc.x && loc.x+size.w < o.loc.x+o.size.w && ((loc.y+size.h > o.loc.y && loc.y < o.loc.y) || (loc.y+size.h > o.loc.y+o.size.h && loc.y < o.loc.y+o.size.h))) {
			loc.x = o.loc.x - size.w; // Right
			colRight = true;
		}

		if (loc.x > o.loc.x && loc.x < o.loc.x+o.size.w && ((loc.y+size.h > o.loc.y && loc.y < o.loc.y) || (loc.y+size.h > o.loc.y+o.size.h && loc.y < o.loc.y+o.size.h))) {
			loc.x = o.loc.x+o.size.w; // Left
			colLeft = true;
		}

		if (o instanceof Obstacle) {
			o.loc.x = noOffsetX;
		}

		return v;
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
