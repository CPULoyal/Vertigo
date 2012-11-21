package me.matej.Vertigo.WorldCreator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import me.matej.Vertigo.WorldCreator.States.WorldState;

/**
 * @author matejkramny
 */
public class CLIThread implements Runnable {

	private WorldState worldState;
	public boolean shouldTerminate = false;

	public void setWorldState(WorldState worldState) {
		this.worldState = worldState;
	}

	@Override
	public void run() {
		BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

		String line;
		while (!shouldTerminate) {
			try {
				line = r.readLine();
				worldState.despatchCommand(line);
			} catch (IOException ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

}
