package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.SizeVector;
import me.matej.Vertigo.Entities.TexturedEntity;
import me.matej.Vertigo.Entities.Vector;

/**
 * @author matejkramny
 */
public abstract class AbstractTool extends TexturedEntity {
	protected Entity parent;
	protected int durability;
	protected double doesDamagePerHit;

	protected AbstractTool (int durability, double doesDamagePerHit, Vector v, SizeVector s, String loc) {
		this(null, durability, doesDamagePerHit, v, s, loc);
	}
	protected AbstractTool (Entity parent, int durability, double doesDamagePerHit, Vector v, SizeVector s, String loc) {
		super(v, s, loc);
		this.parent = parent;
		this.durability = durability;
		this.doesDamagePerHit = doesDamagePerHit;
	}

	public Entity getParent () {
		return parent;
	}
	public double getDoesDamagePerHit () {
		return doesDamagePerHit;
	}
	public int getDurability () {
		return durability;
	}
}
