package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;

/**
 * @author matejkramny
 */
public class SwordTool extends AbstractTool {

	public SwordTool () {
		this(null);
	}
	public SwordTool (Entity parent) {
		super(parent, 10000, 10);
	}

}
