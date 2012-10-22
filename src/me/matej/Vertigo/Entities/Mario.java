package me.matej.Vertigo.Entities;

/**
 *
 * @author matejkramny
 */
// Alias Mario
public class Mario extends TexturedEntity {
	
	public static String relMarioLoc = "me/matej/Vertigo/resources/Mario.png";
	
	public Mario (Vector v, SizeVector size) {
		super(v, size, relMarioLoc);
	}
	
}
