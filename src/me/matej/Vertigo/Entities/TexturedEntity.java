package me.matej.Vertigo.Entities;

import java.io.IOException;

import me.matej.Vertigo.Drawable;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * @author matejkramny
 */
public class TexturedEntity extends Entity implements Drawable {
	private Texture texture;

	public void loadTexture(String path) {
		try {
			texture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(path), GL11.GL_LINEAR);
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(-1);
		}
	}

	public TexturedEntity(Vector v, SizeVector s, String texturePath) {
		super(v, s);
		loadTexture(texturePath);
	}

	public Texture getTexture () {
		return texture;
	}
	public void setTexture (Texture texture) {
		this.texture = texture;
	}

	@Override
	public void draw() {
		drawBegin();
		drawVertices();
		drawEnd();
	}

	@Override
	public void drawBegin() {
		drawBegin();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void drawEnd() {
		drawEnd();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
	}

	@Override
	public void drawVertices() {
		GL11.glTranslated(loc.x, loc.y, 0);
		Color.white.bind();
		texture.bind();
		GL11.glBegin(GL11.GL_QUADS);
		{
			GL11.glTexCoord2d(0, 0);
			GL11.glVertex2d(0, 0);
			GL11.glTexCoord2d(0, texture.getHeight());
			GL11.glVertex2d(0, size.h);
			GL11.glTexCoord2d(texture.getWidth(), texture.getHeight());
			GL11.glVertex2d(size.w, size.h);
			GL11.glTexCoord2d(texture.getWidth(), 0);
			GL11.glVertex2d(size.w, 0);
		}
		GL11.glEnd();
	}
}
