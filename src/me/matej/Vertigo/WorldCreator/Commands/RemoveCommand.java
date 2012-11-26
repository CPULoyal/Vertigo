package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class RemoveCommand extends AbstractCommand {
	{
		description = "Remove an obstacle. Arguments 'removeID(int)'";
	}

	public void execute(String[] args) {
		ArrayList<Obstacle> os = ((WorldState) GameStateEnum.World.getStateInstance()).getObstacles();

		if (os == null) {
			System.out.println("No World selected");
			return;
		}

		if (args.length != 2) {
			System.err.println(description);
			return;
		}

		if (args[1] instanceof String && "all".equals(args[1])) {
			// Remove all obstacles
			synchronized (os) {
				os = new ArrayList<Obstacle>();
			}

			System.out.println("All obstacles removed!");
		}

		try {
			int id = Integer.parseInt(args[1]);
			if (id < os.size()) {
				synchronized (os) {
					os.remove(id);
				}

				System.out.println("Obstacle ID " + id + " removed");
			} else {
				System.err.println("Invalid obstacle ID");
			}
		} catch (Exception e) {
			System.err.println("Error parsing argument 1");
		}
	}
}
