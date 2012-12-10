package me.matej.Vertigo.WorldCreator.States;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.GameStates.GameStateClass;
import me.matej.Vertigo.World.World;
import me.matej.Vertigo.World.WorldLoader;
import me.matej.Vertigo.WorldCreator.CLIThread;
import me.matej.Vertigo.WorldCreator.Commands.*;
import me.matej.Vertigo.WorldCreator.Main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class WorldState extends GameStateClass {

	private CLIThread cliThread;
	private HashMap<String, AbstractCommand> commands;
	private World world;
	private Entity selectedEntity;

	@Override
	public void draw() {
		if (world != null)
			world.draw();
	}

	@Override
	public void update(int delta) {
		//world.update(delta);
	}

	public ArrayList<Obstacle> getObstacles() {
		if (world != null)
			return world.getObstacles();
		return null;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		assert world != null : "New world null";

		System.out.println("Using world from path "+world.getLocation());
		this.world = world;

		world.setMarioResetAsMario();
	}

	public Entity getSelectedEntity() {
		return selectedEntity;
	}

	public void setSelectedEntity(Entity e) {
		selectedEntity = e;
	}

	public void despatchCommand(String command) {
		String[] args = command.split(" ");

		if (args.length > 0) {
			AbstractCommand cmd = commands.get(args[0]);

			if ("help".equals(args[0]) || "?".equals(args[0])) {
				if (args.length == 1) {
					System.out.print("Available commands:");
					for (String name : commands.keySet()) {
						System.out.format(" %s", name);
					}
					System.out.println();
				} else {
					// Get command name
					String cmdName = args[1];
					AbstractCommand abstractCommand = commands.get(cmdName);
					if (abstractCommand != null)
						System.out.format("Description of %s %s%n", cmdName, abstractCommand.getDescription());
					else
						System.out.format("Command %s not found%n", cmdName);
				}
			} else if (cmd == null) {
				System.out.format("Unknown command %s%n", args[0]);
			} else {
				cmd.execute(args);
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F3) {
			WorldLoader.saveWorld(world);
		} else if (key == Keyboard.KEY_ESCAPE) {
			System.exit(0);
		} else if (key == Keyboard.KEY_R) {
			//GameMain.instance().startGameThread();   // Not working
		} else if (key == Keyboard.KEY_LEFT) {
			if (selectedEntity != null) {
				selectedEntity.loc.x -= 1;
			}
		} else if (key == Keyboard.KEY_RIGHT) {
			if (selectedEntity != null) {
				selectedEntity.loc.x += 1;
			}
		} else if (key == Keyboard.KEY_UP) {
			if (selectedEntity != null) {
				selectedEntity.loc.y -= 1;
			}
		} else if (key == Keyboard.KEY_DOWN) {
			if (selectedEntity != null) {
				selectedEntity.loc.y += 1;
			}
		} else if (key == Keyboard.KEY_L) {
			if (selectedEntity != null) {
				selectedEntity.size.w += 1;
			}
		} else if (key == Keyboard.KEY_J) {
			if (selectedEntity != null) {
				selectedEntity.size.w -= 1;
			}
		} else if (key == Keyboard.KEY_K) {
			if (selectedEntity != null) {
				selectedEntity.size.h += 1;
			}
		} else if (key == Keyboard.KEY_I) {
			if (selectedEntity != null) {
				selectedEntity.size.h -= 1;
			}
		} else if (key == Keyboard.KEY_N) {
			commands.get("add").execute(new String[]{"add", "1", "0", "0", "1", "100", "100", "10", "10", "false"});
			commands.get("select").execute(new String[]{"select", getObstacles().size() - 1 + ""});
		}
	}

	@Override
	public void mouseButtonPressed(int index) {
	}

	@Override
	public void init() {
		this.didInit = true;

		commands = new HashMap<String, AbstractCommand>();
		commands.put("list", new ListCommand());
		commands.put("add", new AddCommand());
		commands.put("remove", new RemoveCommand());
		commands.put("load", new LoadCommand());
		commands.put("modify", new ModifyCommand());
		commands.put("move", new MoveCommand());
		commands.put("save", new SaveCommand());
		commands.put("quit", new QuitCommand());
		commands.put("exit", new ExitCommand());
		commands.put("dm", new DMCommand());
		commands.put("select", new SelectCommand());
		commands.put("download", new DownloadCommand());

		System.out.println("No world loaded. Listing worlds: ");
		commands.get("list").execute(new String[] { "list", "worlds" });

		world = null;

		cliThread = new CLIThread();
		cliThread.setWorldState(this);
		(new Thread(cliThread)).start();
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		WorldLoader.saveWorld(world);
		this.init();
	}
}
