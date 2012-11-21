package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class MoveCommand extends AbstractCommand {
	{
		description = "Moves position (effective Z index) of the obstacle. Arguments 'existingID(int) toID(int)'";
	}

	public void execute(String[] args) {
		ArrayList<Obstacle> os = ((WorldState) GameStateEnum.World.getStateInstance()).getObstacles();

		// Moves the order of the obstacle
		if (args.length != 3) {
			System.err.println(description);
			return;
		}

		try {
			int existingID = Integer.parseInt(args[1]), newID = Integer.parseInt(args[2]);
			synchronized (os) {
				Obstacle exist = os.get(existingID);
				os.remove(existingID);
				os.add(newID, exist);
			}

			System.out.println("Moved obstacle from ID " + existingID + " to ID " + newID);
		} catch (Exception e) {
			System.err.println("Invalid arguments 1 and 2");
			return;
		}
	}
}
