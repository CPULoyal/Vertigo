package me.matej.Vertigo.GameStates;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.SoundManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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

	private boolean paused = false;

	@Override
	public void draw() {
		Font f = Main.getOpenGL().getFont();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		f.drawString(10, 10, "World Translated about X by "+(int)xOffset+"px", Color.black);
		f.drawString(10, 40, Main.getOpenGL().getFps()+" FPS", Color.black);
		if (getBullets() != null)
			f.drawString(10, 70, "Bullets: "+getBullets().size(), Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		mario.draw();
		if (obstacles != null) {
			for(Obstacle e : obstacles) {
				if (e != null)
					e.draw();
			}
		}
		if (bullets != null) {
			for(Bullet b : bullets) {
				if (b != null)
					b.draw();
			}
		}

		if (paused) {
			GameStateEnum.GameMenu.getStateInstance().draw();
		}
	}

	@Override
	public void update(int delta) {
		if (!paused) {
			mario.update(delta);

			for (Obstacle o : obstacles) {
				o.xOffset = xOffset;
			}

			if (bullets != null) {
				ArrayList<Bullet> removeList = new ArrayList<Bullet>();
				for (Bullet b : bullets) {
					b.update(delta);
					DisplayMode dm = OpenGL.getDisplayMode();
					int w = dm.getWidth(), h = dm.getHeight();
					if (b.loc.x+b.size.w > w || b.loc.x < -b.size.w || b.loc.y+b.size.h > h || b.loc.y < -b.size.h) {
						// Remove bullet from stack
						removeList.add(b);
					}
				}
				getBullets().removeAll(removeList);
			}
		} else {
			GameStateEnum.GameMenu.getStateInstance().update(delta);
		}
	}

	@Override
	public void keyPressed(int key) {
		if (key == Keyboard.KEY_F3) {
		    try {
				this.saveObstacles();
				System.out.println("Saved obstacles..");
		    } catch (Exception e) {
				e.printStackTrace(System.err);
		    }
		} else if (key == Keyboard.KEY_ESCAPE && !paused) {
			GameStateEnum.GameMenu.getStateInstance().init();
			paused = true;
		} else if (key == Keyboard.KEY_R) {
			this.init();
			this.xOffset = 0;
		} else if (key == Keyboard.KEY_Q) {
			SoundManager sm = SoundManager.getSingleton();
			sm.getExplosion().playAsSoundEffect(1.0f, 1.0f, false);
		} else {
			if (paused)
				GameStateEnum.GameMenu.getStateInstance().keyPressed(key);
			else
				mario.keyPressed(key);
		}
	}

	private ArrayList<Bullet> bullets;

	@Override
	public void mouseButtonPressed(int index) {
		if (!paused) {
			// Make a bullet go in a random direction..
			if (getBullets() == null) {
				bullets = new ArrayList<Bullet>();
			}

			DisplayMode dm = OpenGL.getDisplayMode();

			if (index == 0) {
				Bullet b = new Bullet();

				b.loc = new Vector(dm.getWidth()/2-5, dm.getHeight()/2-5);
				Color c = b.color;
				c.r = (float)Math.random();
				c.g = (float)Math.random();
				c.b = (float)Math.random();
				c.a = 1;
				b.size = new SizeVector(10, 10);
				double x = Math.random();
				double y = Math.random();
				b.dir = new Vector(x < 0.5 ? x : -x, y < 0.5 ? y : -y);

				getBullets().add(b);
			} else {
				int mx = Mouse.getX()-10, my = dm.getHeight() - Mouse.getY()-10;
				int x = dm.getWidth()/2-10, y = dm.getHeight()/2-10;

				double xDiff = mx - x, yDiff = my - y;

				float actual = (float)Math.abs(Math.toDegrees(Math.atan(yDiff / xDiff)));

				if (xDiff > 0f && yDiff < 0f)
					actual = 360f - actual;
				else if (xDiff < 0f && yDiff < 0f)
					actual += 180f;
				else if (xDiff < 0f && yDiff > 0f)
					actual = 90f + (90f - actual);

				double dx = Math.cos(Math.toRadians(actual)), dy = Math.sin(Math.toRadians(actual));

				Bullet b = new Bullet();

				b.loc = new Vector(x, y);
				b.color.r = 0f;
				b.color.g = 1f;
				b.color.b = 0f;
				b.color.a = 1;
				b.size = new SizeVector(20, 20);
				b.dir = new Vector(dx, dy);

				getBullets().add(b);
			}
		} else {
			GameStateEnum.GameMenu.getStateInstance().mouseButtonPressed(index);
		}
	}

	@Override
	public void init() {
		// Uncomment line below to avoid side-effect when switching DM (world resets when dm changes)
		//this.didInit = true; // We want to get init() again when we start the game for second time..

		// Load mario
		mario = new Mario(new Vector(50, 10), new SizeVector(100, 133));

		// Load obstacles..
		try {
			loadObstacles();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		} finally {
			System.out.println("Obstacles loaded successfully.");
		}

		//obstacles = new ArrayList<Obstacle>();

		DisplayMode dm = OpenGL.getDisplayMode();

		Obstacle[] ents = { new Obstacle(new Vector (0.0, dm.getHeight()-200), new SizeVector(300.0, 20.0), Color.red),
						new Obstacle(new Vector(300.0, dm.getHeight()-100), new SizeVector(50.0, 20.0), Color.yellow),
						new Obstacle(new Vector(300.0, OpenGL.getDisplayMode().getHeight()-50), new SizeVector(350.0, 20.0), Color.red),
						new Obstacle(new Vector(0.0, -1.0), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), Color.transparent, true),
						new Obstacle(new Vector(0.0, OpenGL.getDisplayMode().getHeight()), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), Color.transparent, true)
		};
		//obstacles.addAll(Arrays.asList(ents));
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		this.init();
	}

	public void setPaused (boolean newPaused) {
		paused = newPaused;
	}

	private static String worldLoc = Main.getSaveDir() + "World.json";

	private void loadObstacles () throws JsonIOException, JsonSyntaxException, IOException {
		Type obstaclesType = new TypeToken<ArrayList<Obstacle>>(){}.getType();

		System.out.println("Path to world is: "+worldLoc);

		File worldFile = new File(worldLoc);
		if (!worldFile.exists()) {
			worldFile.createNewFile();
			obstacles = new ArrayList<Obstacle>();
			return;
		}

		FileReader fr = new FileReader(worldFile);
		JsonReader reader = new JsonReader(fr);
		obstacles = new Gson().fromJson(reader, obstaclesType);
		fr.close();
	}

	private void saveObstacles () throws IOException {
		PrintWriter pw = new PrintWriter (new FileWriter(new File(worldLoc)));
		pw.print(new Gson().toJson(getObstacles()));
		pw.close();
	}

	/**
	 * @return the mario
	 */
	public Mario getMario() {
		return mario;
	}

	/**
	 * @return the obstacles
	 */
	public ArrayList<Obstacle> getObstacles() {
		if (obstacles == null)
			obstacles = new ArrayList<Obstacle>();
		return obstacles;
	}

	/**
	 * @return the bullets
	 */
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
