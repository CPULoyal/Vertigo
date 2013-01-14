package me.matej.Vertigo.World;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import me.matej.Vertigo.GameMain;

import java.io.*;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public abstract class WorldLoader {
	// Loads and saves world objects to file

	public static World loadWorld(String location) {
		try {
			System.out.println("Loading world from " + location);

			File worldFile = new File(location);
			if (!worldFile.exists()) {
				worldFile.createNewFile();
				World world = WorldGenerator.generateWorld();
				WorldLoader.saveWorld(world);
				return world;
			}

			FileReader fr = new FileReader(worldFile);
			JsonReader reader = new JsonReader(fr);
			World world = new Gson().fromJson(reader, World.class);
			fr.close();

			// Seems like a bug? world == null, always.
			if (world == null) {
				world = WorldGenerator.generateWorld();
				WorldLoader.saveWorld(world);

				System.err.println("No world in file.. Regenerated!");
			}

			return world;
		} catch (Exception e) {
			e.printStackTrace(System.err);
			return null;
		}
	}

	public static void saveWorld(World world) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(new File(world.location)));
			pw.print(new Gson().toJson(world));
			pw.close();

			System.out.println("Saved world to " + world.location);
		} catch (Exception e) {
			return;
		}
	}

	private static String worldsFileLoc = GameMain.getSaveDir() + "worlds.list";

	public static String getWorldsLoc() {
		return worldsFileLoc;
	}

	public static String[] getWorlds() {
		File worldsFile = new File(worldsFileLoc);
		if (!worldsFile.exists()) {
			try {
				worldsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}

		try {
			BufferedReader worldsReader = new BufferedReader(new FileReader(worldsFile));
			ArrayList<String> worlds = new ArrayList<String>();
			String line;
			while ((line = worldsReader.readLine()) != null) {
				if (line.startsWith("#")) continue;

				worlds.add(line);
			}

			worldsReader.close();


			String[] worldsString;
			worldsString = worlds.toArray(new String[]{});

			return worldsString;
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		return new String[]{};
	}
	public static World[] loadWorlds (String[] worlds) {
		World[] worldsArray = new World[worlds.length];
		for (int i = 0; i < worlds.length; i++) {
			worldsArray[i] = WorldLoader.loadWorld(GameMain.getSaveDir()+worlds[i]);
		}

		return worldsArray;
	}

	public static void addWorld(String loc) {
		File worldsFile = new File(worldsFileLoc);
		if (!worldsFile.exists()) {
			return;
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(worldsFile));
			bw.append(loc);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			return;
		}
	}
}
