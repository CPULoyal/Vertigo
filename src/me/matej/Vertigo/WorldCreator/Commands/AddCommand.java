package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;
import org.newdawn.slick.Color;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class AddCommand extends AbstractCommand {
	{
		description = "Add an obstacle. Arguments: 'red(float) green(float) blue(float) alpha(float) x(double) y(double) w(double) h(double) sticky(bool)'";
	}

	public void execute(String[] args) {
		ArrayList<Obstacle> os = ((WorldState) GameStateEnum.World.getStateInstance()).getObstacles();

		// Adding an element.. Check argument count
		// Valid: add red green blue x y w h
		if (args.length != 10) {
			System.err.println(description);
			return;
		}

		try {
			float red = Float.parseFloat(args[1]), green = Float.parseFloat(args[2]), blue = Float.parseFloat(args[3]), alpha = Float.parseFloat(args[4]);
			double x = Double.parseDouble(args[5]), y = Double.parseDouble(args[6]);
			double w = Double.parseDouble(args[7]), h = Double.parseDouble(args[8]);
			boolean sticky = Boolean.parseBoolean(args[9]);

			synchronized (os) {
				os.add(new Obstacle(new Vector(x, y), new SizeVector(w, h), new Color(red, green, blue, alpha), sticky));
			}
		} catch (Exception e) {
			System.err.println("Error parsing arguments for command 'add'");
			return;
		}
	}
}
