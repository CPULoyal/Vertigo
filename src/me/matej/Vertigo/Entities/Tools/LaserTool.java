package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;

/**
 * @author matejkramny
 */
public class LaserTool extends AbstractTool {

	public static final String textureLocation = ""; //TODO

	public LaserTool () {
		this (null);
	}
	public LaserTool (Entity parent) {
		super (parent, 100, 100, new Vector(parent.loc.x+parent.size.w, parent.loc.y), new SizeVector(100, 30), textureLocation);
	}

}
