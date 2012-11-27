package me.matej.Vertigo.World;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import org.newdawn.slick.Color;
import sun.security.provider.MD5;

import java.security.CryptoPrimitive;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class WorldGenerator {
	// Generates new random worlds..

	public static World generateWorld() {
		World world = new World();

		ArrayList<Obstacle> os = world.getObstacles();

		int minY = 300;
		int maxY = 500;

		for (int i = 0; i < 50; i++) {
			double rand = Math.random();
			double y = rand * 10 + minY;
			double x = rand * 100;
			double w = 20;
			double h = 10;
			Color c = new Color((float)rand/2, (float)rand, (float)rand/4);
			os.add(new Obstacle(new Vector(x, y), new SizeVector(w, h), c));
		}

		return world;
	}
}
