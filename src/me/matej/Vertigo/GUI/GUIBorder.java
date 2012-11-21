package me.matej.Vertigo.GUI;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;
import org.newdawn.slick.Color;

/**
 * @author matejkramny
 */
public class GUIBorder extends Entity {
	public void configure(Vector loc, SizeVector size, Color color) {
		this.loc = loc;
		this.size = size;
		this.color = color;
	}
}
