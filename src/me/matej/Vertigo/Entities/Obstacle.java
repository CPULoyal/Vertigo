package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Obstacle extends Entity {
	public double xOffset;
	public boolean sticky;
	//collidesWithOthers...

	public Obstacle(Vector v, SizeVector s, Color c, boolean sticky) {
		this(v, s, c);
		this.sticky = sticky;
	}

	public Obstacle(Vector v, SizeVector s, Color c) {
		super(v, s, c);
		sticky = false;
	}

	@Override
	public void draw() {
		super.drawBegin();
		if (!sticky)
			GL11.glTranslated(xOffset, 0, 0);
		super.rotate();
		super.color();
		super.drawVerts();
		super.drawEnd();
	}

	@Override
	public boolean touchesEntity(Entity o) {
		if (sticky) {
			return super.basicCollide(o);
		} else {
			loc.x += xOffset;
			boolean touch = super.touchesEntity(o);
			loc.x -= xOffset;

			return touch;
		}
	}

	@Override
	public boolean basicCollide(Entity other) {
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
	public boolean checkAndFixLeftCollision(Entity o) {
		if (sticky) {
			return o.checkAndFixLeftCollision(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.checkAndFixLeftCollision(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean checkAndFixRightCollision(Entity o) {
		if (sticky) {
			return o.checkAndFixRightCollision(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.checkAndFixRightCollision(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean checkAndFixTopCollision(Entity o) {
		if (sticky) {
			return o.checkAndFixTopCollision(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.checkAndFixTopCollision(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean checkAndFixBottomCollision(Entity o) {
		if (sticky) {
			return o.checkAndFixBottomCollision(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.checkAndFixBottomCollision(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
