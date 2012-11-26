package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Background;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.Mario;
import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class ListCommand extends AbstractCommand {
	{
		description = "List obstacles and Mario. Arguments '[id(int|\"mario\"|\"background\"|\"worlds\")]";
	}

	public void execute(String[] args) {
		// Lists all obstacles or obstacle ID
		WorldState worldState = ((WorldState) GameStateEnum.World.getStateInstance());
		ArrayList<Obstacle> os = worldState.getObstacles();
		selectedEntity = worldState.getSelectedEntity();

		if (args.length == 1) {
			System.out.println("Listing objects:");

			if (os == null) {
				System.out.println("No World selected");
				return;
			}

			synchronized (os) {
				for (int i = 0; i < os.size(); i++) {
					Obstacle o = os.get(i);
					System.out.format("%sObstacle ID #%d %s%n", getPrefix(o), i, o.toString());
				}
			}

			Mario m = worldState.getWorld().getMario();
			System.out.format("%sMario %s%n", getPrefix(m), m.toString());

			Background b = worldState.getWorld().getBackground();
			System.out.format("%sBackground %s%n", getPrefix(b), b.toString());
		} else {
			try {
				String stringID = args[1];
				Entity o;
				if ("mario".equals(stringID) && os != null)
					o = worldState.getWorld().getMario();
				else if ("background".equals(stringID) && os != null)
					o = worldState.getWorld().getBackground();
				else if ("worlds".equals(stringID)) {
					// List worlds
					String[] paths = WorldLoader.getWorlds();
					for (String path : paths) {
						System.out.println("Listing World at "+path);
					}

					return;
				} else {
					if (os == null) {
						System.out.println("No World selected");
						return;
					}

					o = os.get(Integer.parseInt(args[1]));
				}

				System.out.format("Listing #%s %s%n", stringID, o.toString());
			} catch (NumberFormatException e) {
				System.err.println("Cannot parse argument 2");
			}
		}
	}

	private Entity selectedEntity;
	private static final String selectedPrefix = " + ";
	private static final String deselectedPrefix = "   ";

	private boolean isSelectedEntity (Entity o) {
		if (selectedEntity != null && selectedEntity.equals(o))
			return true;

		return false;
	}
	private String getPrefix (Entity o) {
		if (isSelectedEntity(o))
			return selectedPrefix;

		return deselectedPrefix;
	}
}
