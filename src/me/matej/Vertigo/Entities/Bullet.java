package me.matej.Vertigo.Entities;

import java.util.ArrayList;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.GameStates.GameState;
import me.matej.Vertigo.SoundManager;

/**
 *
 * @author matejkramny
 */
public class Bullet extends Entity {
	public Vector dir; // Direction
	private boolean didCollide = false;
	
	public void update (int delta) {
		loc.x += dir.x * delta / 10;
		loc.y += dir.y * delta / 10;
		
		ArrayList<Obstacle> obstacles = ((GameState)(GameStateEnum.Game.getStateInstance())).getObstacles();
		Mario mario = ((GameState)(GameStateEnum.Game.getStateInstance())).getMario();
		
		if (!didCollide)
			for (Obstacle o : obstacles) {
				if (o.basicCollide(this)) {
					didCollide = true;
					SoundManager sm = SoundManager.getSingleton();
					sm.getExplosion().playAsSoundEffect((float)Math.random()*2, 1f, false);
					break;
				}
			}
		else {
			// Visible Explooooosion and Audible BOOM (at the same time)
			r = (float)Math.random();
			g = (float)Math.random();
			b = (float)Math.random();
			a -= 0.003 * delta;
			
			double explSize = 0.08 * delta;
			size.w += explSize;
			size.h += explSize;
			loc.x -= explSize/2;
			loc.y -= explSize/2;
			
			dir.x = 0;
			dir.y = 0;
			
			if (a <= 0.0) {
				// Remove from screen
				loc.x = -size.w*2;
				loc.y = -size.h*2;
			}
		}
		
		if (mario.basicCollide(this)) {
			this.loc.x = -this.size.w*2;
			this.loc.y = -this.size.h*2;
		}
	}
}
