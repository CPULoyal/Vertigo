package me.matej.Vertigo.Entities;

import me.matej.Vertigo.GameStateEnum;
import me.matej.Vertigo.GameStates.GameState;
import me.matej.Vertigo.Main;
import me.matej.Vertigo.OpenGL;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author matejkramny
 */
// Alias Mario
public class Mario extends TexturedEntity {

	public static String relMarioLoc = "me/matej/Vertigo/resources/Mario.png";

	private GameState game;
	// Gravity pulls mario down (+y) by gravity * delta
	private final double gravity = 0.0015d;
	private final double terminalVelocity = 0.8d;
	private double jumpAngle = 90.0; // Angle used for smoothing
	private double jumpVelocity = 0.0; // Speed at which mario falls (or rises)
	private final double startJumpVelocity = 0.8; // Max velocity mario can ever achieve
	private final double jumpCooldownPeriod = 50.0;
	private double lastJumpEnd = System.currentTimeMillis() - jumpCooldownPeriod;
	private boolean gravityEnabled = true;
	private double health; // Mario's health - starts at 100

	public Mario (Vector v, SizeVector size) {
		super(v, size, relMarioLoc);
		game = (GameState)GameStateEnum.Game.getStateInstance();
		health = 100;
	}

	public void keyPressed (int key) {
		if (key == Keyboard.KEY_G)
			this.gravityEnabled = !this.gravityEnabled;
	}

	@Override
	public void draw () {
		super.draw();

		GL11.glLoadIdentity();
		GL11.glColor3d(1, 0, 0);
		GL11.glTranslated(OpenGL.getDisplayMode().getWidth() - 110, 10, 0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(0, 0);
		GL11.glVertex2d(health, 0);
		GL11.glVertex2d(health, 20);
		GL11.glVertex2d(0, 20);
		GL11.glEnd();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		TrueTypeFont font = Main.getOpenGL().getFont();
		String velocityText = "Health: "+(int)health;
		GL11.glTranslated(-font.getWidth(velocityText), -10, 0);
		font.drawString(-10, font.getHeight(velocityText)/2-7, velocityText, Color.black);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	private boolean isMidAir () {
		for (Obstacle o : game.getObstacles()) {
			if (o.touchesEntity(this))
				return false;
		}

		return true;
	}

	private boolean holdsForwardKeyDuringJump = false;
	private boolean falling = true;
	private boolean wasJumping = false;

	public void update (int delta) {
		Vector oldLoc = new Vector(loc.x, loc.y);
		boolean marioAttractedByGravity = true;

		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !falling && System.currentTimeMillis() > lastJumpEnd+jumpCooldownPeriod) {
			boolean midAir = isMidAir();

			if (!midAir && !wasJumping && !falling) { // First loop jump
				holdsForwardKeyDuringJump = true;
			}

			if (holdsForwardKeyDuringJump) {
				if (jumpAngle > 180) {
					jumpAngle = 90;
					// Start falling
					falling = true;
					marioAttractedByGravity = true;
				} else {
					wasJumping = true;
					jumpAngle += 0.3 * delta; // .3 good? .1 slow
					jumpVelocity = Math.sin(Math.toRadians(jumpAngle)) / 1.5;
					marioAttractedByGravity = false;
				}
			}
		} else {
			falling = true;
			holdsForwardKeyDuringJump = false;
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			loc.x -= 0.2d * delta;
			if (loc.x < 50) {
				loc.x = 50;
				game.xOffset += 0.2d * delta;
			}

			for (Obstacle o : game.getObstacles()) {
				o.checkAndFixLeftCollision(this);
			}
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			loc.x += 0.2d * delta;
			double dw = OpenGL.getDisplayMode().getWidth();
			if (loc.x+size.w+50 > dw) {
				loc.x = dw - 50 - size.w;
				game.xOffset -= 0.2d * delta;
			}

			for (Obstacle o : game.getObstacles()) {
				o.checkAndFixRightCollision(this);
			}
		}

		if (marioAttractedByGravity && gravityEnabled && falling) {
			if (jumpVelocity > 0)
				jumpVelocity -= gravity * delta;
			jumpVelocity -= gravity * delta;

			if (jumpVelocity > terminalVelocity) {
				jumpVelocity = terminalVelocity;
			}
		}

		if (jumpVelocity != 0.0) {
			loc.y -= jumpVelocity * delta;

			if (jumpVelocity < 0.0) {
				falling = true;
				for (Obstacle o : game.getObstacles()) {
					if (o.checkAndFixBottomCollision(this)) {
						if (wasJumping) {
							wasJumping = false;
							lastJumpEnd = System.currentTimeMillis();
							if (jumpVelocity <= -terminalVelocity) {
								affectHealth(-10);
							}
						}
						falling = false;
						jumpVelocity = 0;
						jumpAngle = 90;
					}
				}
			}
			else {
				for (Obstacle o : game.getObstacles()) {
					if (o.checkAndFixTopCollision(this)) {
						jumpAngle = 90;
						falling = true;
						marioAttractedByGravity = true;
						jumpVelocity = 0;
					}
				}
			}
		}
	}

	public void affectHealth (double diff) {
		health += diff;

		if (health <= 0) {
			game.keyPressed(Keyboard.KEY_R); // Reset
		}
	}
}
