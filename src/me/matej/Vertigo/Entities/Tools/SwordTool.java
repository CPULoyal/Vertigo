package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.Vector;

/**
 * @author matejkramny
 */
public class SwordTool extends AbstractTool {

	public static final String textureLocation = ""; //TODO

	public SwordTool () {
		this(null);
	}
	public SwordTool (Entity parent) {
		super(parent, 10000, 10, new Vector(parent.loc.x+parent.size.w, parent.loc.y), new SizeVector(100, 30), textureLocation);
	}

}
