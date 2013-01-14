package me.matej.Vertigo.Entities.Tools;

import me.matej.Vertigo.Entities.Entity;

/**
 * @author matejkramny
 */
public abstract class AbstractTool {
	protected Entity parent;
	protected int durability;
	protected double doesDamagePerHit;

	protected AbstractTool (int durability, double doesDamagePerHit) {
		this(null, durability, doesDamagePerHit);
	}
	protected AbstractTool (Entity parent, int durability, double doesDamagePerHit) {
		super();
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
