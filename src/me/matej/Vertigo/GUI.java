package me.matej.Vertigo;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;

/**
 *
 * @author matejkramny
 */

// GUI Class providing buttons and text-input elements using OpenGL drawings

public class GUI extends Entity {

	public GUI () {
		this(new Vector(0, 0), new SizeVector(0,0));
	}
	public GUI (Vector v, SizeVector s) {
		this(v, s, 0, 0, 0);
	}
	public GUI (Vector v, SizeVector s, float r, float g, float b) {
		this(v, s, r, g, b, 1);
	}
	public GUI (Vector v, SizeVector s, float r, float g, float b, float a) {
		super(v, s, r, g, b, a);
	}

	public void update (int delta) {

	}

	@Override
	public void draw () {
		super.draw();
	}
}
