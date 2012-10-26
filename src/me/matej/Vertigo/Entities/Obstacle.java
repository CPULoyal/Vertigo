package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author matejkramny
 */
public class Obstacle extends Entity {
	public double xOffset;
	public boolean sticky;
	
	public Obstacle (Vector v, SizeVector s, float r, float g, float b, boolean sticky) {
		this(v, s, r, g, b);
		this.sticky = sticky;
	}
	public Obstacle (Vector v, SizeVector s, float r, float g, float b) {
		super(v, s, r, g, b);
		sticky = false;
	}

	@Override
	public void draw () {
		super.drawBegin();
		if (!sticky)
			GL11.glTranslated(xOffset, 0, 0);
		super.rotate();
		super.color();
		super.drawVerts();
		super.drawEnd();
	}

	@Override
	public boolean basicCollide (Entity other) {
		if (sticky) {
			return super.basicCollide(other);
		} else {
			Vector origLoc = new Vector(loc.x, loc.y);
			loc.x += xOffset;
			boolean collide = super.basicCollide(other);
			loc = origLoc;
			return collide;
		}
	}
	
	@Override
	public void checkAndFixLeftCollision (Entity o) {
		if (sticky) {
			o.checkAndFixLeftCollision(this);
		} else {
			loc.x += xOffset;
			o.checkAndFixLeftCollision(this);
			loc.x -= xOffset;
		}
	}
	
	@Override
	public void checkAndFixRightCollision (Entity o) {
		if (sticky) {
			o.checkAndFixRightCollision(this);
		} else {
			loc.x += xOffset;
			o.checkAndFixRightCollision(this);
			loc.x -= xOffset;
		}
	}
	
	@Override
	public void checkAndFixTopCollision (Entity o) {
		if (sticky) {
			o.checkAndFixTopCollision(this);
		} else {
			loc.x += xOffset;
			o.checkAndFixTopCollision(this);
			loc.x -= xOffset;
		}
	}
	
	@Override
	public void checkAndFixBottomCollision (Entity o) {
		if (sticky) {
			o.checkAndFixBottomCollision(this);
		} else {
			loc.x += xOffset;
			o.checkAndFixBottomCollision(this);
			loc.x -= xOffset;
		}
	}
}
