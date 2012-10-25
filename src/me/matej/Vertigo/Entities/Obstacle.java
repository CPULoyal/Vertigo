package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author matejkramny
 */
public class Obstacle extends Entity {
	public double xOffset;

	public Obstacle (Vector v, SizeVector s, float r, float g, float b) {
		super(v, s, r, g, b);
	}

	@Override
	public void draw () {
		super.drawBegin();
		GL11.glTranslated(xOffset, 0, 0);
		super.rotate();
		super.color();
		super.drawVerts();
		super.drawEnd();
	}

	@Override
	public boolean basicCollide (Entity other) {
		Vector origLoc = new Vector(loc.x, loc.y);
		loc.x += xOffset;
		boolean collide = super.basicCollide(other);
		loc = origLoc;
		return collide;
	}

	@Override
	public Vector getNonCollisionVector (Entity other, Vector d) {
		Vector origLoc = new Vector (loc.x, loc.y);
		loc.x += xOffset;
		Vector v = super.getNonCollisionVector(other, d);
		loc = origLoc;

		return v;
	}
}
