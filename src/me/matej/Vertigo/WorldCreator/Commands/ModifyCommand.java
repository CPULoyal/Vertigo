package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.Entities.Background;
import me.matej.Vertigo.Entities.Entity;
import me.matej.Vertigo.Entities.Mario;
import me.matej.Vertigo.Entities.Obstacle;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

import java.util.ArrayList;

/**
 * @author matejkramny
 */
public class ModifyCommand extends AbstractCommand {
	{
		description = "Modify property of an obstacle. Arguments 'property(string) newValue(double|float|boolean) [id(int|\"mario\"|\"background\")] [relative(boolean)=false]'";
	}

	public void execute(String[] args) {
		WorldState worldState = (WorldState) GameStateEnum.World.getStateInstance();
		ArrayList<Obstacle> os = worldState.getObstacles();

		if (worldState.getWorld() == null) {
			System.out.println("No World selected");
			return;
		}

		// Modifies an attribute of an obstacle
		if (args.length < 3 || args.length > 5) {
			System.err.println(description);
			return;
		}

		try {
			String id;
			String property = args[1];
			String val = args[2];

			Entity o;

			if (args.length == 3 || "true".equals(args[3]) || "false".equals(args[3])) {
				// no ID.. use SelectedEntity from WorldState
				o = worldState.getSelectedEntity();
				if (o == null) {
					System.err.println("Select an object first or specify object id.");
					System.err.println(description);
					return;
				}
				id = "selected";
			} else if ("mario".equals(args[3])) {
				// Modify mario,,
				o = worldState.getWorld().getMario();
				id = args[3];
			} else if ("background".equals(args[3])) {
				o = worldState.getWorld().getBackground();
				id = args[3];
			} else {
				o = os.get(Integer.parseInt(args[3]));
				id = args[3];
			}

			boolean relative = false;
			if (args.length > 3 && ("false".equals(args[3]) || "true".equals(args[3])))
				relative = Boolean.parseBoolean(args[args.length - 1]);

			if (property.equals("x") && !(o instanceof Background))
				o.loc.x = getValueDouble(o.loc.x, Double.parseDouble(val), relative);
			else if (property.equals("y") && !(o instanceof Background))
				o.loc.y = getValueDouble(o.loc.y, Double.parseDouble(val), relative);
			else if (property.equals("r"))
				o.color.r = getValueFloat(o.color.r, Float.parseFloat(val), relative, true);
			else if (property.equals("g"))
				o.color.g = getValueFloat(o.color.g, Float.parseFloat(val), relative, true);
			else if (property.equals("b"))
				o.color.b = getValueFloat(o.color.b, Float.parseFloat(val), relative, true);
			else if (property.equals("a"))
				o.color.a = getValueFloat(o.color.a, Float.parseFloat(val), relative);
			else if (property.equals("sticky") && o instanceof Obstacle)
				((Obstacle) o).sticky = Boolean.parseBoolean(val);
			else if (property.equals("w") && !(o instanceof Background))
				o.size.w = getValueDouble(o.size.w, Double.parseDouble(val), relative);
			else if (property.equals("h") && !(o instanceof Background))
				o.size.h = getValueDouble(o.size.h, Double.parseDouble(val), relative);
			else
				System.err.println("Unknown property " + property);

			System.out.println("Modified property " + property + " to " + (relative ? "relative " : "") + "value " + args[2] + " of obstacle ID " + id);
		} catch (NumberFormatException ex) {
			System.err.println("Cannot parse argument.");
			return;
		}
	}

	private double getValueDouble(double orig, double newValue, boolean relative) {
		if (relative)
			orig += newValue;
		else
			orig = newValue;

		return orig;
	}

	private float getValueFloat(float orig, float newValue, boolean relative) {
		if (relative)
			orig += newValue;
		else
			orig = newValue;

		return orig;
	}
	private float getValueFloat(float orig, float newValue, boolean relative, boolean ratio) {
		if (ratio && newValue > 1.0f) {
			newValue = newValue / 255;
		}

		return getValueFloat(orig, newValue, relative);
	}
}
