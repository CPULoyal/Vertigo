package me.matej.Vertigo.Entities;

import org.lwjgl.opengl.DisplayMode;

/**
 *
 * @author matejkramny
 */
public class SizeVector {
	public static final SizeVector buttonSize = new SizeVector(200, 40);
	public static final SizeVector buttonBorderSize = new SizeVector(SizeVector.buttonSize.w+10, SizeVector.buttonSize.h+10);

	public double w, h;

	public SizeVector () {
		super();
	}

	public SizeVector (DisplayMode dm) {
		this(dm.getWidth(), dm.getHeight());
	}

	public SizeVector (double w, double h) {
		this();
		this.w = w;
		this.h = h;
	}
}
