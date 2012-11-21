package me.matej.Vertigo.World;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class WorldGenerator {
	// Generates new random worlds..

	public static World generateWorld() {
		World world = new World();

		world.getObstacles().add(new Obstacle(new Vector(10, 10), new SizeVector(100, 20), Color.cyan));

		return world;
	}
}
