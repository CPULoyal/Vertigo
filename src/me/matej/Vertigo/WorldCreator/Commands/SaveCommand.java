package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class SaveCommand extends AbstractCommand {
	{
		description = "Save world to file.";
	}

	public void execute(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of commands");
			return;
		}

		WorldLoader.saveWorld(((WorldState) GameStateEnum.World.getStateInstance()).getWorld());
	}
}
