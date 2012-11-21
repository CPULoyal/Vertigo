package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class LoadCommand extends AbstractCommand {
	{
		description = "Load obstacles";
	}

	public void execute(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of commands");
			return;
		}
		System.out.println("does nothing");
	}
}
