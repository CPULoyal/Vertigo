package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

/**
 * @author matejkramny
 */
public class SelectCommand extends AbstractCommand {
	{
		description = "Select an entity. After selected, use arrow keys and 'jkil' keys to move/resize the entity. Arguments 'ID(int|\"mario\")";
	}

	public void execute(String[] args) {
		if (args.length != 2) {
			System.err.println(description);
			return;
		}

		WorldState worldState = ((WorldState) GameStateEnum.World.getStateInstance());
		String idString = args[1];
		if ("mario".equals(idString)) {
			// Select mario
			worldState.setSelectedEntity(worldState.getWorld().getMario());
			System.out.println("Selected mario");
		} else if ("background".equals(idString)) {
			worldState.setSelectedEntity(worldState.getWorld().getBackground());
			System.out.println("Selected background");
		} else {
			try {
				int id = Integer.parseInt(idString);
				if (id > worldState.getObstacles().size()) {
					throw new ArrayIndexOutOfBoundsException("Invalid ID");
				}
				worldState.setSelectedEntity(worldState.getObstacles().get(id));
				System.out.println("Selected obstacle id " + id);
			} catch (NumberFormatException nfx) {
				System.out.println("Cannot parse argument");
			}
		}
	}
}
