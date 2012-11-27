package me.matej.Vertigo.Entities;

import me.matej.Vertigo.Drawable;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Entity {
	public static final Entity mouse = new Entity();

	public Vector loc; // location
	public SizeVector size; // width and height

	public boolean touchesWith(Entity o) {
		if ((o.loc.x >= loc.x && o.loc.x <= loc.x + size.w && o.loc.y >= loc.y && o.loc.y <= loc.y + size.h) ||
				(o.loc.x + o.size.w >= loc.x && o.loc.x + o.size.w <= loc.x + size.w && o.loc.y >= loc.y && o.loc.y <= loc.y + size.h) ||
				(o.loc.x >= loc.x && o.loc.x <= loc.x + size.w && o.loc.y + o.size.h >= loc.y && o.loc.y + o.size.h <= loc.y + size.h) ||
				(o.loc.x + o.size.w >= loc.x && o.loc.x + o.size.w <= loc.x + size.w && o.loc.y + o.size.w >= loc.y && o.loc.y + o.size.h <= loc.y + size.h) ||
				(loc.x >= o.loc.x && loc.x <= o.loc.x + o.size.w && loc.y >= o.loc.y && loc.y <= o.loc.y + o.size.h) ||
				(loc.x + size.w >= o.loc.x && loc.x + size.w <= o.loc.x + o.size.w && loc.y >= o.loc.y && loc.y <= o.loc.y + o.size.h) ||
				(loc.x >= o.loc.x && loc.x <= o.loc.x + o.size.w && loc.y + size.h >= o.loc.y && loc.y + size.h <= o.loc.y + o.size.h) ||
				(loc.x + size.w >= o.loc.x && loc.x + size.w <= o.loc.x + o.size.w && loc.y + size.h >= o.loc.y && loc.y + size.h <= o.loc.y + o.size.h)) {
			return true;
		}

		return false;
	}

	public boolean collidesWith(Entity o) {
		if ((o.loc.x > loc.x && o.loc.x < loc.x + size.w && o.loc.y > loc.y && o.loc.y < loc.y + size.h) ||
				(o.loc.x + o.size.w > loc.x && o.loc.x + o.size.w < loc.x + size.w && o.loc.y > loc.y && o.loc.y < loc.y + size.h) ||
				(o.loc.x > loc.x && o.loc.x < loc.x + size.w && o.loc.y + o.size.h > loc.y && o.loc.y + o.size.h < loc.y + size.h) ||
				(o.loc.x + o.size.w > loc.x && o.loc.x + o.size.w < loc.x + size.w && o.loc.y + o.size.w > loc.y && o.loc.y + o.size.h < loc.y + size.h) ||
				(loc.x > o.loc.x && loc.x < o.loc.x + o.size.w && loc.y > o.loc.y && loc.y < o.loc.y + o.size.h) ||
				(loc.x + size.w > o.loc.x && loc.x + size.w < o.loc.x + o.size.w && loc.y > o.loc.y && loc.y < o.loc.y + o.size.h) ||
				(loc.x > o.loc.x && loc.x < o.loc.x + o.size.w && loc.y + size.h > o.loc.y && loc.y + size.h < o.loc.y + o.size.h) ||
				(loc.x + size.w > o.loc.x && loc.x + size.w < o.loc.x + o.size.w && loc.y + size.h > o.loc.y && loc.y + size.h < o.loc.y + o.size.h)) {
			return true;
		}

		return false;
	}



	public boolean isCollidingBottom(Entity o) {
		if (((loc.x > o.loc.x && loc.x < o.loc.x + o.size.w) || (loc.x + size.w > o.loc.x && loc.x + size.w < o.loc.x + o.size.w) ||
				(o.loc.x > loc.x && o.loc.x < loc.x + size.w) || (o.loc.x + o.size.w > loc.x && o.loc.x + o.size.w < loc.x + size.w)) &&
				((loc.y + size.h > o.loc.y && loc.y + size.h < o.loc.y + o.size.h) || (o.loc.y > loc.y && o.loc.y < loc.y + size.h))) {
			//loc.y = o.loc.y - size.h; // Bottom

			return true;
		}

		return false;
	}

	public boolean isCollidingTop(Entity o) {
		if (((loc.x > o.loc.x && loc.x < o.loc.x + o.size.w) || (loc.x + size.w > o.loc.x && loc.x + size.w < o.loc.x + o.size.w) ||
				(o.loc.x > loc.x && o.loc.x < loc.x + size.w) || (o.loc.x + o.size.w > loc.x && o.loc.x + o.size.w < loc.x + size.w)) &&
				((loc.y > o.loc.y && loc.y < o.loc.y + o.size.h) || (o.loc.y + o.size.h > loc.y && o.loc.y + o.size.h < loc.y + size.h))) {
			//loc.y = o.loc.y + o.size.h; // Top

			return true;
		}

		return false;
	}

	public boolean isCollidingLeft(Entity o) {
		if (loc.x > o.loc.x && loc.x < o.loc.x + o.size.w &&
				((loc.y + size.h > o.loc.y && loc.y < o.loc.y) || (loc.y + size.h > o.loc.y + o.size.h && loc.y < o.loc.y + o.size.h) ||
						(o.loc.y + o.size.h > loc.y + size.h && o.loc.y < loc.y + size.h) || (o.loc.y + o.size.h > loc.y && o.loc.y < loc.y))) {
			//loc.x = o.loc.x + o.size.w; // Left

			return true;
		}

		return false;
	}

	public boolean isCollidingRight(Entity o) {
		if (loc.x + size.w > o.loc.x && loc.x + size.w < o.loc.x + o.size.w &&
				((loc.y + size.h > o.loc.y && loc.y < o.loc.y) || (loc.y + size.h > o.loc.y + o.size.h && loc.y < o.loc.y + o.size.h) ||
						(o.loc.y + o.size.h > loc.y + size.h && o.loc.y < loc.y + size.h) || (o.loc.y + o.size.h > loc.y && o.loc.y < loc.y))) {
			//loc.x = o.loc.x - size.w; // Right

			return true;
		}

		return false;
	}

	public Entity() {
		this(new Vector(0, 0), new SizeVector(0, 0));
	}

	public Entity(Vector v, SizeVector s) {
		super();
		loc = v;
		size = s;
	}

	@Override
	public String toString () {
		return String.format("X:%.3f Y:%.3f W:%.3f H:%.3f Red:%.3f Green:%.3f Blue:%.3f Alpha:%.3f", loc.x, loc.y, size.w, size.h);
	}

}
