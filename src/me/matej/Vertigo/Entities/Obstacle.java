package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Obstacle extends ColouredEntity {
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
		super.drawVertices();
		super.drawEnd();
	}

	@Override
	public boolean touchesWith(Entity o) {
		if (sticky) {
			return super.touchesWith(o);
		} else {
			loc.x += xOffset;
			boolean touch = super.touchesWith(o);
			loc.x -= xOffset;

			return touch;
		}
	}

	@Override
	public boolean collidesWith(Entity other) {
		if (sticky) {
			return super.collidesWith(other);
		} else {
			Vector origLoc = new Vector(loc.x, loc.y);
			loc.x += xOffset;
			boolean collide = super.collidesWith(other);
			loc = origLoc;
			return collide;
		}
	}
	// TODO Implement fixCollisionLeft
	@Override
	public boolean isCollidingLeft(Entity o) {
		if (sticky) {
			return o.isCollidingLeft(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.isCollidingLeft(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean isCollidingRight(Entity o) {
		if (sticky) {
			return o.isCollidingRight(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.isCollidingRight(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean isCollidingTop(Entity o) {
		if (sticky) {
			return o.isCollidingTop(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.isCollidingTop(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public boolean isCollidingBottom(Entity o) {
		if (sticky) {
			return o.isCollidingBottom(this);
		} else {
			loc.x += xOffset;
			boolean collide = o.isCollidingBottom(this);
			loc.x -= xOffset;

			return collide;
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
