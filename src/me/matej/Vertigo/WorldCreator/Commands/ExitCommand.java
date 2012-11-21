package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.io.IOException;

/**
 * @author matejkramny
 */
public class ExitCommand extends AbstractCommand {
	{
		description = "Saves world then quits";
	}

	public void execute(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of commands");
			return;
		}

		WorldLoader.saveWorld(((WorldState) GameStateEnum.World.getStateInstance()).getWorld());
		System.exit(0);
	}
}