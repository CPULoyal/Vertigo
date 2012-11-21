package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.OpenGL;

/**
 * @author matejkramny
 */
public class DMCommand extends AbstractCommand {
	{
		description = "Returns DisplayMode dimensions";
	}

	public void execute(String[] args) {
		if (args.length != 1) {
			System.err.println(description);
		}

		System.out.format("Current dimensions: " + OpenGL.getDisplayMode().getWidth() + "x" + OpenGL.getDisplayMode().getHeight() + "%n");
	}
}
