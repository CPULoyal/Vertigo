package me.matej.Vertigo;

import java.io.IOException;

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

/**
 * @author matejkramny
 */
public class SoundManager {
	private Audio explosion;
	private Audio click;
	private static SoundManager singleton;

	public static SoundManager getSingleton() {
		if (singleton == null)
			singleton = new SoundManager();

		return singleton;
	}

	public Audio getExplosion() {
		if (explosion == null) {
			try {
				this.loadExplosion();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}

		return explosion;
	}

	public Audio getClick() {
		if (click == null) {
			try {
				this.loadClick();
			} catch (IOException e) {
				e.printStackTrace(System.err);
			}
		}

		return click;
	}

	public void loadExplosion() throws IOException {
		if (explosion == null)
			explosion = getAudio("WAV", "me/matej/Vertigo/resources/explosion.wav");
	}

	public void loadClick() throws IOException {
		if (click == null)
			click = getAudio("WAV", "me/matej/Vertigo/resources/click.wav");
	}

	private Audio getAudio(String type, String path) throws IOException {
		return AudioLoader.getAudio(type, ResourceLoader.getResourceAsStream(path));
	}

	public void update(int delta) {
		SoundStore.get().poll(0);
	}
}
