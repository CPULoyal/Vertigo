package me.matej.Vertigo.WorldCreator.Commands;

/**
 * @author matejkramny
 */
abstract public class AbstractCommand {
	protected String description;

	public abstract void execute(String[] args);

	public String getDescription() {
		return description;
	}
}
