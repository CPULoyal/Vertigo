package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;

/**
 * @author matejkramny
 */
public class LaserTool extends AbstractTool {

	public LaserTool () {
		this (null);
	}
	public LaserTool (Entity parent) {
		super (parent, 100, 100);
	}

}
