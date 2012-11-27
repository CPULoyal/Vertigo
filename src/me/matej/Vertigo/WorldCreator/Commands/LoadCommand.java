package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.World.World;
import me.matej.Vertigo.World.WorldGenerator;
import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.Main;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class LoadCommand extends AbstractCommand {
	{
		description = "Load obstacles. Arguments: 'path(string|\"new\")'. Use 'list worlds' to get list of worlds.";
	}

	public void execute(String[] args) {
		if (args.length != 2) {
			System.out.println(description);
			return;
		}

		if (args[1].equals("new")) {
			World w = WorldGenerator.generateWorld();
			((WorldState)GameStateEnum.World.getStateInstance()).setWorld(w);

			return;
		}

		// TODO Load world from file.
		String argPath = args[1];
		String[] paths = WorldLoader.getWorlds();
		World world = null;

		for (String path : paths) {
			if (path.equals(argPath)) {
				// Found match.. World should load
				world = WorldLoader.loadWorld(Main.getSaveDir()+path);
				break;
			}
		}

		if (world == null) {
			System.out.println("No world found");
		} else {
			((WorldState)GameStateEnum.World.getStateInstance()).setWorld(world);
		}

	}
}
