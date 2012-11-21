package me.matej.Vertigo.GameStates;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import me.matej.Vertigo.SoundManager;
import me.matej.Vertigo.World.World;
import me.matej.Vertigo.World.WorldLoader;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;

/**
 * @author matejkramny
 */

public class GameState extends GameStateClass {

	private World world;

	public double xOffset;

	private boolean paused = false;

	@Override
	public void draw() {
		world.draw();

		if (bullets != null) {
			for (Bullet b : bullets) {
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
			world.getMario().update(delta);

			for (Obstacle o : world.getObstacles()) {
				o.xOffset = xOffset;
			}

			if (bullets != null) {
				ArrayList<Bullet> removeList = new ArrayList<Bullet>();
				for (Bullet b : bullets) {
					b.update(delta);
					DisplayMode dm = OpenGL.getDisplayMode();
					int w = dm.getWidth(), h = dm.getHeight();
					if (b.loc.x + b.size.w > w || b.loc.x < -b.size.w || b.loc.y + b.size.h > h || b.loc.y < -b.size.h) {
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
			WorldLoader.saveWorld(world);
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
				world.getMario().keyPressed(key);
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

				b.loc = new Vector(dm.getWidth() / 2 - 5, dm.getHeight() / 2 - 5);
				Color c = b.color;
				c.r = (float) Math.random();
				c.g = (float) Math.random();
				c.b = (float) Math.random();
				c.a = 1;
				b.size = new SizeVector(10, 10);
				double x = Math.random();
				double y = Math.random();
				b.dir = new Vector(x < 0.5 ? x : -x, y < 0.5 ? y : -y);

				getBullets().add(b);
			} else {
				int mx = Mouse.getX() - 10, my = dm.getHeight() - Mouse.getY() - 10;
				int x = dm.getWidth() / 2 - 10, y = dm.getHeight() / 2 - 10;

				double xDiff = mx - x, yDiff = my - y;

				float actual = (float) Math.abs(Math.toDegrees(Math.atan(yDiff / xDiff)));

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
		this.didInit = true;

		world = WorldLoader.loadWorld(Main.getSaveDir() + "MatejWorld.vertigo.world.json");

		/*obstacles = new ArrayList<Obstacle>();

		DisplayMode dm = OpenGL.getDisplayMode();

		Obstacle[] ents = { new Obstacle(new Vector (0.0, dm.getHeight()-200), new SizeVector(300.0, 20.0), Color.red),
						new Obstacle(new Vector(300.0, dm.getHeight()-100), new SizeVector(50.0, 20.0), Color.yellow),
						new Obstacle(new Vector(300.0, OpenGL.getDisplayMode().getHeight()-50), new SizeVector(350.0, 20.0), Color.red),
						new Obstacle(new Vector(0.0, -1.0), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), Color.transparent, true),
						new Obstacle(new Vector(0.0, OpenGL.getDisplayMode().getHeight()), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), Color.transparent, true)
		};
		obstacles.addAll(Arrays.asList(ents));*/
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		this.init();
		world.getBackground().displayModeChanged(newDisplayMode);
	}

	public void setPaused(boolean newPaused) {
		paused = newPaused;
	}

	/**
	 * @return the mario
	 */
	public Mario getMario() {
		return world.getMario();
	}

	/**
	 * @return the obstacles
	 */
	public ArrayList<Obstacle> getObstacles() {
		assert world != null : "World is null!";

		return world.getObstacles();
	}

	/**
	 * @return the bullets
	 */
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
