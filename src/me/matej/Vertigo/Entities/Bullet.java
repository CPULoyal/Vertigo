package me.matej.Vertigo.Entities;

import java.util.ArrayList;

import me.matej.Vertigo.GameMain;
import me.matej.Vertigo.GameStates.GameState;
import me.matej.Vertigo.SoundManager;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class Bullet extends ColouredEntity {
	public Vector dir; // Direction
	private boolean didCollide = false;

	public Bullet() {
		super(new Color(0,0,0,0));
	}

	public void update(int delta) {
		loc.x += dir.x * delta / 10;
		loc.y += dir.y * delta / 10;

		ArrayList<Obstacle> obstacles = ((GameState)GameMain.states.get("game")).getObstacles();
		Mario mario = ((GameState)GameMain.states.get("game")).getMario();

		if (!didCollide)
			for (Obstacle o : obstacles) {
				if (o.collidesWith(this)) {
					didCollide = true;
					SoundManager sm = SoundManager.getSingleton();
					sm.getExplosion().playAsSoundEffect((float) Math.random() * 2, 1f, false);
					break;
				}
			}
		else {
			// Visible Explooooosion and Audible BOOM (at the same time)
			color.r = (float) Math.random();
			color.g = (float) Math.random();
			color.b = (float) Math.random();
			color.a -= 0.003 * delta;

			double explSize = 0.08 * delta;
			size.w += explSize;
			size.h += explSize;
			loc.x -= explSize / 2;
			loc.y -= explSize / 2;

			dir.x = 0;
			dir.y = 0;

			if (color.a <= 0.0) {
				// Remove from screen
				loc.x = -size.w * 2;
				loc.y = -size.h * 2;
			}
		}

		if (mario.collidesWith(this)) {
			// Transform location so system removes this automatically
			this.loc.x = -this.size.w * 2;
			this.loc.y = -this.size.h * 2;
			mario.affectHealth(-10);
		}
	}
}
