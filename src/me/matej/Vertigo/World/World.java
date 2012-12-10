package me.matej.Vertigo.World;

import me.matej.Vertigo.Entities.*;
import me.matej.Vertigo.GameMain;
import me.matej.Vertigo.OpenGL;
import org.newdawn.slick.Color;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;

/*
	TODO toggling fullscreen not working correctly
 */


/**
 * @author matejkramny
 */
public class World {
	protected Background background; // The background.
	protected ArrayList<Obstacle> obstacles; // Obstacles
	protected Mario mario; // Main character - position in-game
	protected Mario marioReset; // Mario's reset state..
	protected ArrayList<Enemy> enemies;
	protected String location; // Save file location
	protected String seed; // World seed
	protected long created; // Creation time in milliseconds.

	public World() {
		background = new Background(OpenGL.getDisplayMode(), new Color(200, 250, 100));
		obstacles = new ArrayList<Obstacle>();
		mario = new Mario(new Vector(0, 0), new SizeVector(100, 133));
		marioReset = new Mario(new Vector(0,0), new SizeVector(100, 133));
		location = GameMain.getSaveDir() + ((int)Math.random()*1000) + new Date().getTime() + ".vertigo.world.json";
		SecureRandom random = new SecureRandom();
		seed = new BigInteger(130, random).toString(32);
		created = new Date().getTime();
		enemies = new ArrayList<Enemy>();
	}

	public void draw() {
		assert obstacles != null : "Obstacles are null!";
		assert mario != null : "Mario is null";
		assert background != null : "Null Background";
		assert enemies != null : "Null enemies";

		background.draw();
		synchronized (obstacles) {
			for (Obstacle o : obstacles)
				o.draw();
		}
		mario.draw();
		for (Enemy e : enemies) {
			e.draw();
		}
	}

	public void reset () {
		mario.loc.x = marioReset.loc.x;
		mario.loc.y = marioReset.loc.y;
	}

	public void update(int delta) {
		assert mario != null : "Mario is null!";

		mario.update(delta);
		for (Enemy e : enemies) {
			e.update(delta);
		}
	}

	public ArrayList<Obstacle> getObstacles() {
		assert obstacles != null : "Obstacles is null!";

		return obstacles;
	}

	public ArrayList<Enemy> getEnemies () {
		assert enemies != null : "Enemies are null";

		return enemies;
	}

	public Mario getMario() {
		assert mario != null : "Mario is null!";

		return mario;
	}
	public void setMarioResetAsMario () {
		marioReset = mario;
	}
	public Mario getMarioReset() {
		assert marioReset != null : "Mario reset is null!";

		return marioReset;
	}

	public String getLocation() {
		assert location != null : "Location is null!";

		return location;
	}

	public String getSeed() {
		assert seed != null : "Seed is null!";

		return seed;
	}

	public long getCreated() {
		assert created != 0 : "Created is zero!";

		return created;
	}

	public Background getBackground() {
		assert background != null : "Null background!";

		return background;
	}
}
