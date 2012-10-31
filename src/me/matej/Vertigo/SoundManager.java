package me.matej.Vertigo;

import java.io.IOException;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;

/**
 *
 * @author matejkramny
 */
public class SoundManager {
	private Audio explosion;
	private static SoundManager singleton;
	
	public static SoundManager getSingleton() {
		if (singleton == null)
			singleton = new SoundManager();
		
		return singleton;
	}
	
	public Audio getExplosion () {
		return explosion;
	}
	
	public void loadExplosion () throws IOException {
		explosion = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("me/matej/Vertigo/resources/explosion.wav"));
	}
	
	public void update (int delta) {
		SoundStore.get().poll(0);
	}
}
