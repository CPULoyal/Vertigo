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
import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.GameStates.GameStateClass;
import me.matej.Vertigo.WorldCreator.CLIThread;
import me.matej.Vertigo.WorldCreator.Main;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.Color;

/**
 *
 * @author matejkramny
 */
public class WorldState extends GameStateClass {

	private ArrayList<Obstacle> obstacles;
	private CLIThread cliThread;

	@Override
	public void draw() {
		synchronized (obstacles) {
			for (Obstacle o : obstacles)
				o.draw();
		}
	}

	@Override
	public void update(int delta) {
	}

	public void despatchCommand (String command) {
		String[] args = command.split(" ");
		if ("list".equals(args[0])) {
			// Lists all obstacles or obstacle ID
			if (args.length == 1) {
				System.out.println("Obstacles list:");

				synchronized (obstacles) {
					for (int i = 0; i < obstacles.size(); i++) {
						Obstacle o = obstacles.get(i); // TODO add descibe() or description() (.toString?) method to Obstacle..
						System.out.format("Obstacle ID #%d X:%.3f Y:%.3f W:%.3f H:%.3f Red:%.3f Green:%.3f Blue:%.3f Alpha:%.3f %n", i, o.loc.x, o.loc.y, o.size.w, o.size.h, o.color.r, o.color.g, o.color.b, o.color.a);
					}
				}
			} else {
				try {
					int id = Integer.parseInt(args[1]);
					Obstacle o = obstacles.get(id);
					System.out.format("Obstacle ID #%d X:%.3f Y:%.3f W:%.3f H:%.3f Red:%.3f Green:%.3f Blue:%.3f Alpha:%.3f %n", id, o.loc.x, o.loc.y, o.size.w, o.size.h, o.color.r, o.color.g, o.color.b, o.color.a);
				} catch (NumberFormatException e) {
					System.err.println("Cannot parse argument 2");
				}
			}
		} else if ("add".equals(args[0])) {
			// Adding an element.. Check argument count
			// Valid: add red green blue x y w h
			if (args.length != 10) {
				System.err.println("Argument length does not match. Syntax is 'add red green blue alpha x y w h sticky'");
				return;
			}

			try {
				float red = Float.parseFloat(args[1]), green = Float.parseFloat(args[2]), blue = Float.parseFloat(args[3]), alpha = Float.parseFloat(args[4]);
				double x = Double.parseDouble(args[5]), y = Double.parseDouble(args[6]);
				double w = Double.parseDouble(args[7]), h = Double.parseDouble(args[8]);
				boolean sticky = Boolean.parseBoolean(args[9]);

				synchronized (obstacles) {
					obstacles.add(new Obstacle(new Vector(x,y), new SizeVector(w, h), new Color(red, green, blue, alpha), sticky));
				}
			} catch (Exception e) {
				System.err.println("Error parsing arguments for command 'add'");
				return;
			}
		} else if (args.length == 1 && "save".equals(args[0])) {
			try {
				this.saveObstacles();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			} finally {
				System.out.println("Done saving data");
			}
		} else if (args.length == 1 && "quit".equals(args[0])) {
			try {
				saveObstacles();
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			} finally {
				System.exit(0);
			}
		} else if ("remove".equals(args[0])) {
			if (args.length != 2) {
				System.err.println("Invalid argument count for command 'remove'. Syntax: 'remove ID/all'.");
				return;
			}

			if (args[1] instanceof String && "all".equals(args[1])) {
				// Remove all obstacles
				synchronized (obstacles) {
					obstacles = new ArrayList<Obstacle>();
				}

				System.out.println("All obstacles removed!");
			}

			try {
				int id = Integer.parseInt(args[1]);
				if (id < obstacles.size()) {
					synchronized (obstacles) {
						obstacles.remove(id);
					}

					System.out.println("Obstacle ID "+id+" removed");
				} else {
					System.err.println("Invalid obstacle ID");
				}
			} catch (Exception e) {
				System.err.println("Error parsing argument 1");
			}
		} else if (args.length == 1 && "load".equals(args[0])) {
			try {
				this.loadObstacles();
			} catch (Exception e) {
				e.printStackTrace(System.err);
			}
		} else if ("move".equals(args[0])) {
			// Moves the order of the obstacle
			if (args.length != 3) {
				System.err.println("Invalid argument count for command 'move'. Syntax: 'move ID newID'.");
				return;
			}

			try {
				int existingID = Integer.parseInt(args[1]), newID = Integer.parseInt(args[2]);
				synchronized (obstacles) {
					Obstacle exist = obstacles.get(existingID);
					obstacles.remove(existingID);
					obstacles.add(newID, exist);
				}

				System.out.println("Moved obstacle from ID "+existingID+" to ID "+newID);
			} catch (Exception e) {
				System.err.println("Invalid arguments 1 and 2");
				return;
			}
		} else if ("modify".equals(args[0])) {
			// Modifies an attribute of an obstacle
			if (args.length != 4) {
				System.err.println("Invalid argument count for command 'modify'. Syntax: 'modify ID property newValue");
				return;
			}

			try {
				Obstacle o = obstacles.get(Integer.parseInt(args[1]));
				String property = args[2];
				if (property == "x")
					o.loc.x = Double.parseDouble(args[3]);
				else if (property == "y")
					o.loc.y = Double.parseDouble(args[3]);
				else if (property == "r")
					o.color.r = Float.parseFloat(args[3]);
				else if (property == "g")
					o.color.g = Float.parseFloat(args[3]);
				else if (property == "b")
					o.color.b = Float.parseFloat(args[3]);
				else if (property == "a")
					o.color.a = Float.parseFloat(args[3]);
				else if (property == "sticky")
					o.sticky = Boolean.parseBoolean(args[3]);
				else if (property == "w")
					o.size.w = Double.parseDouble(args[3]);
				else if (property == "h")
					o.size.h = Double.parseDouble(args[3]);
				else
					System.err.println("Unknown property "+property);

				System.out.println("Modified property "+property+" to value "+args[3]+" of obstacle ID "+args[1]);
			} catch (NumberFormatException ex) {
				System.err.println("Cannot parse number.");
				return;
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F3) {
			try{
				saveObstacles();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		} else if (key == Keyboard.KEY_ESCAPE) {
			System.exit(0);
		} else if (key == Keyboard.KEY_R) {
			Main.getInstance().startGameThread();
		}
	}

	@Override
	public void mouseButtonPressed(int index) {
	}

	@Override
	public void init() {
		try {
			this.loadObstacles();
		} catch (IOException e) {
			System.err.println(e.getMessage());

			synchronized (obstacles) {
				obstacles = new ArrayList<Obstacle>();
			}
		}

		cliThread = new CLIThread();
		cliThread.setWorldState(this);
		(new Thread(cliThread)).start();
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		try {
			this.saveObstacles();
		} catch (IOException e) { e.printStackTrace(System.err); }

		this.init();
	}

	private static final String worldLoc = me.matej.Vertigo.Main.getSaveDir() + "World.json";
	private void loadObstacles () throws JsonIOException, JsonSyntaxException, IOException {
		Type obstaclesType = new TypeToken<ArrayList<Obstacle>>(){}.getType();

		System.out.println("Path to world is: "+worldLoc);

		File worldFile = new File(worldLoc);
		if (!worldFile.exists()) {
			worldFile.createNewFile();
		}

		FileReader fr = new FileReader(worldFile);
		JsonReader reader = new JsonReader(fr);
		obstacles = new Gson().fromJson(reader, obstaclesType);
		fr.close();

		System.out.println("Loaded obstacles");

		if (obstacles == null)
			obstacles = new ArrayList<Obstacle>();
	}

	private void saveObstacles () throws IOException {
		PrintWriter pw = new PrintWriter (new FileWriter(new File(worldLoc)));
		pw.print(new Gson().toJson(obstacles));
		pw.close();

		System.out.println("Saved obstacles");
	}
}
