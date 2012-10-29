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
	public boolean marioCollided = false;

	@Override
	public void draw() {
		Font f = Main.getOpenGL().getFont();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		f.drawString(10, 10, "World Translated about X by "+(int)xOffset+"px", Color.black);
		f.drawString(10, 40, Main.getOpenGL().getFps()+" FPS", Color.black);
		f.drawString(10, 70, "VSync is " + (Main.getOpenGL().isVsync() ? "ON" : "OFF"), Color.black);
		if (getBullets() != null)
			f.drawString(10, 100, "Bullets: "+getBullets().size(), Color.black);
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

		getMario().draw();
		if (getObstacles() != null) {
			for(Obstacle e : getObstacles()) {
				if (e != null)
					e.draw();
			}
		}
		if (getBullets() != null) {
			for(Bullet b : getBullets()) {
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
		Vector oldLoc = new Vector(getMario().loc.x, getMario().loc.y);

		boolean marioAttractedByGravity = true;
		marioCollided = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			mario.loc.y -= 0.2d * delta;
			marioAttractedByGravity = false;

			for (Obstacle o : getObstacles()) {
				o.checkAndFixTopCollision(getMario());
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			mario.loc.x -= 0.2d * delta;
			if (getMario().loc.x < 50) {
				mario.loc.x = 50;
				xOffset += 0.2d * delta;
			}

			for (Obstacle o : getObstacles()) {
				o.checkAndFixLeftCollision(getMario());
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			mario.loc.x += 0.2d * delta;
			double dw = OpenGL.getDisplayMode().getWidth();
			if (getMario().loc.x+getMario().size.w+50 > dw) {
				mario.loc.x = dw - 50 - getMario().size.w;
				xOffset -= 0.2d * delta;
			}

			for (Obstacle o : getObstacles()) {
				o.checkAndFixRightCollision(getMario());
			}
		}

		if (marioAttractedByGravity && gravityEnabled) {
			mario.loc.y += gravity * delta;
			for (Obstacle o : getObstacles()) {
				o.checkAndFixBottomCollision(getMario());
			}
		}

		for (Obstacle o : getObstacles()) {
			o.xOffset = xOffset;
		}
		
		if (getBullets() != null) {
			ArrayList<Bullet> removeList = new ArrayList<Bullet>();
			for (Bullet b : getBullets()) {
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

	private ArrayList<Bullet> bullets;

	@Override
	public void mouseButtonPressed(int index) {
		// Make a bullet go in a random direction..
		if (getBullets() == null) {
			bullets = new ArrayList<Bullet>();
		}
		
		DisplayMode dm = OpenGL.getDisplayMode();
		
		if (index == 0) {
			Bullet b = new Bullet();

			b.loc = new Vector(dm.getWidth()/2-5, dm.getHeight()/2-5);
			b.r = (float)Math.random();
			b.g = (float)Math.random();
			b.b = (float)Math.random();
			b.a = 1;
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
			b.r = 0f;
			b.g = 1f;
			b.b = 0f;
			b.a = 1;
			b.size = new SizeVector(20, 20);
			b.dir = new Vector(dx, dy);
			
			getBullets().add(b);
		}
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
		getObstacles().addAll(Arrays.asList(ents));
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
		return obstacles;
	}

	/**
	 * @return the bullets
	 */
	public ArrayList<Bullet> getBullets() {
		return bullets;
	}
}
