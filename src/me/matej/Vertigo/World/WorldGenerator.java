package me.matej.Vertigo.World;

import me.matej.Vertigo.Entities.Enemy;
import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.OpenGL;
import org.newdawn.slick.Color;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class WorldGenerator {
	// Generates new random worlds..

	public static World generateWorld() {
		World world = new World();

		ArrayList<Obstacle> os = world.getObstacles();
		ArrayList<Enemy> enemies = world.getEnemies();

		int minY = 300;
		int maxY = 500;

		for (int i = 0; i < 50; i++) {
			double rand = Math.random();
			double w = 100;
			double h = 100;

			for (int rows = 0; rows < 2; rows++) {
				double y = rows * h + minY;
				double x = i * w;

				Color c = new Color((float)rand/2 * i, (float)rand * i * rows, (float)rand/4 * rows);
				os.add(new Obstacle(new Vector(x, y), new SizeVector(w, h), c));
			}
		}

		enemies.add(new Enemy(new Vector(300, 200), new SizeVector(100, 133)));

		return world;
	}
}
