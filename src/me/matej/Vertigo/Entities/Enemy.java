package me.matej.Vertigo.Entities;

import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Enemy extends ColouredEntity {
	private int health = 100;
	private boolean living = true;

	public Enemy (Vector v, SizeVector s, Color c) {
		super(v, s, c);
	}
	public Enemy (Vector v, SizeVector s) {
		super(v, s, Color.black);
	}

	@Override
	public void draw() {
		if (!living) return;

		super.draw();
	}
	@Override
	public void drawBegin () {
		if (!living) return;

		super.drawBegin();
	}
	@Override
	public void drawEnd () {
		if (!living) return;

		super.drawEnd();
	}
	@Override
	public void drawVertices() {
		if (!living) return;

		super.drawVertices();
	}

	public void update(int delta) {

	}

	public void affectHealth (int amount) {
		health += amount;

		if (health <= 0) {
			living = false;
		}
	}
}
