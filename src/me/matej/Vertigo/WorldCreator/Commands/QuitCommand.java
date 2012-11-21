package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class QuitCommand extends AbstractCommand {
	{
		description = "Quit without saving.";
	}

	public void execute(String[] args) {
		if (args.length != 1) {
			System.out.println("Invalid number of commands");
			return;
		}

		System.exit(0);
	}
}