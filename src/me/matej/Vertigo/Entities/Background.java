package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Background extends Entity {
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
	public boolean touchesEntity(Entity o) {
		return false;
	}

	@Override
	public boolean basicCollide(Entity other) {
		return false;
	}

	@Override
	public boolean checkAndFixLeftCollision(Entity o) {
		return false;
	}

	@Override
	public boolean checkAndFixRightCollision(Entity o) {
		return false;
	}

	@Override
	public boolean checkAndFixTopCollision(Entity o) {
		return false;
	}

	@Override
	public boolean checkAndFixBottomCollision(Entity o) {
		return false;
	}

	public void displayModeChanged(DisplayMode newDisplayMode) {
		size.w = newDisplayMode.getWidth();
		size.h = newDisplayMode.getHeight();
	}

	@Override
	public String toString() {
		return String.format("r:%.3f g:%.3f b:%.3f a:%.3f", color.r, color.g, color.b, color.a);
	}

}
