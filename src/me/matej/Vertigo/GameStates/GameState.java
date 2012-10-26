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
import java.util.Arrays;
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
	public boolean marioCollided = false;

	@Override
	public void draw() {
		Font f = Main.getOpenGL().getFont();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		f.drawString(10, 10, "World Translated about X by "+(int)xOffset+"px", Color.black);
		f.drawString(10, 40, Main.getOpenGL().getFps()+" FPS", Color.black);
		f.drawString(10, 70, Main.getOpenGL().isVsync() ? "VSync ON" : "VSync OFF", Color.black);
		if (bullets != null)
			f.drawString(10, 100, "Bullets: "+bullets.size(), Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);

		if (marioCollided) {
			// Draw a red rectangle
			GL11.glLoadIdentity();
			GL11.glTranslated(10, 130, 0);
			GL11.glColor3f(1, 0, 0);
			GL11.glBegin(GL11.GL_QUADS);
			{
				GL11.glVertex2f(0, 0);
				GL11.glVertex2f(20, 0);
				GL11.glVertex2f(20, 20);
				GL11.glVertex2f(0, 20);
			}
			GL11.glEnd();
		}

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
	}

	// Gravity pulls mario down (+y) by gravity * delta
	private final double gravity = 0.25f;
	//private double jumpVelocity; // Speed at which mario falls (or rises)
	//private final double terminalJumpVelocity = 0.4; // Max velocity mario can ever achieve
	//private final double jumpCooldownPeriod = 1.0;
	//private double jumpYOffset;
	private boolean gravityEnabled = true;

	@Override
	public void update(int delta) {
		Vector oldLoc = new Vector(mario.loc.x, mario.loc.y);

		boolean marioAttractedByGravity = true;
		marioCollided = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			mario.loc.y -= 0.2d * delta;
			marioAttractedByGravity = false;

			for (Obstacle o : obstacles) {
				o.checkAndFixTopCollision(mario);
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			mario.loc.x -= 0.2d * delta;
			if (mario.loc.x < 50) {
				mario.loc.x = 50;
				xOffset += 0.2d * delta;
			}

			for (Obstacle o : obstacles) {
				o.checkAndFixLeftCollision(mario);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			mario.loc.x += 0.2d * delta;
			double dw = OpenGL.getDisplayMode().getWidth();
			if (mario.loc.x+mario.size.w+50 > dw) {
				mario.loc.x = dw - 50 - mario.size.w;
				xOffset -= 0.2d * delta;
			}

			for (Obstacle o : obstacles) {
				o.checkAndFixRightCollision(mario);
			}
		}

		if (marioAttractedByGravity && gravityEnabled) {
			mario.loc.y += gravity * delta;
			for (Obstacle o : obstacles) {
				o.checkAndFixBottomCollision(mario);
			}
		}

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
			bullets.removeAll(removeList);
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
		if (key == Keyboard.KEY_G)
			this.gravityEnabled = !this.gravityEnabled;
	}

	ArrayList<Bullet> bullets;

	@Override
	public void mouseButtonPressed(int index) {
		// Make a bullet go in a random direction..
		if (bullets == null) {
			bullets = new ArrayList<Bullet>();
		}
		Bullet b = new Bullet();

		b.loc = new Vector(mario.loc.x+mario.size.w/2, mario.loc.y+mario.size.h/2);
		b.r = (float)Math.random();
		b.g = (float)Math.random();
		b.b = (float)Math.random();
		b.a = 1;
		b.size = new SizeVector(10, 10);
		double x = Math.random();
		double y = Math.random();
		b.dir = new Vector(x < 0.5 ? x : -x, y < 0.5 ? y : -y);

		bullets.add(b);
	}

	@Override
	public void init() {
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

		obstacles = new ArrayList<Obstacle>();

		Obstacle[] ents = { new Obstacle(new Vector (0.0, OpenGL.getDisplayMode().getHeight()-100), new SizeVector(300.0, 100.0), 1, 0, 0),
						new Obstacle(new Vector(300.0, OpenGL.getDisplayMode().getHeight()-60-143), new SizeVector(90.0, 10.0), 1, 1, 0),
						new Obstacle(new Vector(300.0, OpenGL.getDisplayMode().getHeight()-50), new SizeVector(1000.0, 10.0), 1, 0, 0),
						new Obstacle(new Vector(0.0, 0.0), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), 1, 0, 0, true),
						new Obstacle(new Vector(0.0, OpenGL.getDisplayMode().getHeight()-1), new SizeVector(OpenGL.getDisplayMode().getWidth(), 1), 1, 0, 0, true)
		};
		obstacles.addAll(Arrays.asList(ents));
	}

	@Override
	public void displayModeChanged(DisplayMode newDisplayMode) {
		this.init();
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
