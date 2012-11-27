package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Background extends ColouredEntity {
	public Background(SizeVector size, Color c) {
		super(new Vector(0, 0), size, c);
	}

	public Background(DisplayMode dm, Color c) {
		super(new Vector(0, 0), new SizeVector(dm.getWidth(), dm.getHeight()), c);
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public boolean touchesWith(Entity o) {
		return false;
	}

	@Override
	public boolean collidesWith(Entity other) {
		return false;
	}

	@Override
	public boolean isCollidingBottom(Entity o) {
		return false;
	}

	@Override
	public boolean isCollidingTop(Entity o) {
		return false;
	}

	@Override
	public boolean isCollidingLeft(Entity o) {
		return false;
	}

	@Override
	public boolean isCollidingRight(Entity o) {
		return false;
	}

	public void displayModeChanged(DisplayMode newDisplayMode) {
		size.w = newDisplayMode.getWidth();
		size.h = newDisplayMode.getHeight();
	}

}
