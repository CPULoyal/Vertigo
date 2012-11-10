package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author matejkramny
 */
public class Vector {
	public double x, y;

	public Vector () {
	}

	// Centers the location if size is passed
	public Vector (double centerX, double centerY, SizeVector size) {
		if (size == null) {
			this.x = centerX;
			this.y = centerY;
		} else {
			this.x = centerX - size.w/2;
			this.y = centerY - size.h/2;
		}
	}

	public Vector (DisplayMode dm, double xOffset, double yOffset) {
		this.x = dm.getWidth()/2 + xOffset;
		this.y = dm.getHeight()/2 + yOffset;
	}
	// Exactly in the center
	public Vector (DisplayMode dm, SizeVector size) {
		this.x = dm.getWidth()/2 - size.w/2;
		this.y = dm.getHeight()/2 - size.h/2;
	}
	// Same as above, but offset x (-/+) and/or y (-/+)
	public Vector (DisplayMode dm, SizeVector size, double xOffset, double yOffset) {
		this (dm, size);
		this.x += xOffset;
		this.y += yOffset;
	}

	public Vector (double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Vector scale(double scale) {
		return new Vector(x * scale, y * scale);
	}

	public Vector multiply(Vector other) {
		return new Vector(x * other.x, y * other.y);
	}

	public Vector perp() {
		Vector vect = new Vector(-y, x);
		return vect;// y = -y;
	}

	public double dot(Vector axis) { //dot product
		return x * axis.x + y * axis.y;
	}
}
