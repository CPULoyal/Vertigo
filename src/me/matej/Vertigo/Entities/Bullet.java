package me.matej.Vertigo.Entities;

/**
 *
 * @author matejkramny
 */
public class Bullet extends Entity {
	public Vector dir; // Direction

	public void update (int delta) {
		loc.x += dir.x * delta / 100;
		loc.y += dir.y * delta / 100;
	}
}
