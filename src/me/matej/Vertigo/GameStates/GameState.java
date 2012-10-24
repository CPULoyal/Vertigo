package me.matej.Vertigo.GameStates;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

/**
 *
 * @author matejkramny
 */
public class GameState extends GameStateClass {

	private Mario mario;
	private ArrayList<Obstacle> obstacles;

	public double xOffset;

	@Override
	public void draw() {
		Font f = Main.getInstance().getOpenGL().getFont();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		f.drawString(10, 10, "World Translated by X axis:"+xOffset, Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		mario.draw();
		if (obstacles != null) {
			for(Obstacle e : obstacles) {
				if (e != null)
					e.draw();
			}
		}
	}

	// Gravity pulls mario down (+y) by gravity * delta
	private final double gravity = 0.25f;
	//private double jumpVelocity; // Speed at which mario falls (or rises)
	//private final double terminalJumpVelocity = 0.4; // Max velocity mario can ever achieve
	//private final double jumpCooldownPeriod = 1.0;
	//private double jumpYOffset;

	@Override
	public void update(int delta) {
		Vector oldLoc = new Vector(mario.loc.x, mario.loc.y);

		boolean marioAttractedByGravity = true;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			mario.loc.y -= 0.2d * delta;
			marioAttractedByGravity = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			//mario.loc.y += 0.5d * delta;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			mario.loc.x -= 0.2d * delta;
			if (mario.loc.x < 15) {
				mario.loc.x = 15;
				xOffset += 0.2d * delta;
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			mario.loc.x += 0.2d * delta;
			double dw = OpenGL.getDisplayMode().getWidth();
			if (mario.loc.x+mario.size.w+15 > dw) {
				mario.loc.x = dw - 15 - mario.size.w;
				xOffset -= 0.2d * delta;
			}
		}

		if (marioAttractedByGravity) {
			mario.loc.y += gravity * delta;
		}

		for (Obstacle o : obstacles) {
			o.xOffset = xOffset;

			if (o.basicCollide(mario)) {
				// Get collision info
				// Reset to last-known non collision position

				mario.getNonCollisionVector(o);
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F3) {
		    try {
				this.saveObstacles();
		    } catch (Exception e) {
				e.printStackTrace(System.err);
		    } finally {
				System.out.println("Saved obstacles..");
		    }
		}
		if (key == Keyboard.KEY_R) {
			this.init();
			this.xOffset = 0;
		}
	}

	@Override
	public void mouseButtonPressed(int index) {

	}

	@Override
	public void init() {
		// Load mario
		mario = new Mario(new Vector(10, 10), new SizeVector(100, 133));

		// Load obstacles..
		try {
			loadObstacles();
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			e.printStackTrace(System.err);
		} finally {
			System.out.println("Obstacles loaded successfully.");
		}

		obstacles = new ArrayList<>();
		double obsY = OpenGL.getDisplayMode().getHeight() - 10;
		Obstacle[] ents = { new Obstacle(new Vector (0.0, obsY), new SizeVector(1000.0, 10.0), 1, 0, 0),
						new Obstacle(new Vector(40.0, obsY), new SizeVector(1000.0, 10.0), 1, 1, 0),
						new Obstacle(new Vector(80.0, obsY), new SizeVector(1000.0, 10.0), 1, 1, 1),
						new Obstacle(new Vector(160.0, obsY), new SizeVector(1000.0, 10.0), 1, 0, 1),
						new Obstacle(new Vector(320.0, obsY), new SizeVector(1000.0, 10.0), 0, 1, 1),
						new Obstacle(new Vector(640.0, obsY), new SizeVector(1000.0, 10.0), 0, 0, 1),
						new Obstacle(new Vector(1280.0, obsY), new SizeVector(1000.0, 10.0), 0.5f, 1, 0),
		};
		for (Obstacle e : ents) {
			obstacles.add(e);
		}
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		for (Obstacle o : obstacles) {
			o.loc.y = newDisplayMode.getHeight() - o.size.h;
		}
	}

	private static String relObstaclesLoc = "/me/matej/Vertigo/resources/Obstacles.json";

	private void loadObstacles () throws JsonIOException, JsonSyntaxException, IOException {
		Type obstaclesType = new TypeToken<ArrayList<Entity>>(){}.getType();

		URL url = getClass().getResource(relObstaclesLoc);
		String path = url.getFile();
		System.out.println("Obstacles loading from "+path);
		FileReader fr = new FileReader(path);
		JsonReader reader = new JsonReader(fr);
		obstacles = new Gson().fromJson(reader, obstaclesType);
		fr.close();
	}

	private void saveObstacles () throws IOException {
		URL url = getClass().getResource(relObstaclesLoc);
		String path = url.getFile();

		PrintWriter pw = new PrintWriter (new FileWriter(new File(path)));
		pw.print(new Gson().toJson(obstacles));
		pw.close();
	}
}
