package me.matej.Vertigo.World;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.matej.Vertigo.Main;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * @author matejkramny
 */
public abstract class WorldLoader {
	// Loads and saves world objects to file

	public static World loadWorld(String location) {
		try {
			System.out.println("Loading world from " + location);

			Type worldType = new TypeToken<World>() {
			}.getType();

			File worldFile = new File(location);
			if (!worldFile.exists()) {
				worldFile.createNewFile();
				World world = WorldGenerator.generateWorld();
				WorldLoader.saveWorld(world);
				return world;
			}

			FileReader fr = new FileReader(worldFile);
			JsonReader reader = new JsonReader(fr);
			World world = new Gson().fromJson(reader, worldType);
			fr.close();

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

	private static String worldsFileLoc = Main.getSaveDir() + "worlds.vertigo";

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
				worlds.add(line);
			}

			worldsReader.close();

			return (String[]) worlds.toArray();
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}

		return new String[]{};
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
