package me.matej.Vertigo.WorldCreator.Commands;

import me.matej.Vertigo.WebService.ConnectionWrapper;
import me.matej.Vertigo.WebService.WorldListing;
import me.matej.Vertigo.World.World;
import me.matej.Vertigo.WorldCreator.GameStateEnum;
import me.matej.Vertigo.WorldCreator.States.WorldState;

/**
 * @author matejkramny
 */
public class DownloadCommand extends AbstractCommand {
	@Override
	public void execute (String[] args) {
		if (args.length != 0) {
			if ("connect".equals(args[1])) {
				ConnectionWrapper cp = new ConnectionWrapper();
				String[] listings = cp.getListingIDs();

				for (String listing : listings) {
					System.out.println("Listing "+listing);
				}

				WorldListing[] worldListings = cp.getListings(listings);
				for (WorldListing listing : worldListings) {
					System.out.println("Name Of Listing "+listing.getName());
				}

				System.out.println("Downloading world");

				World world = cp.getWorld(worldListings[0]);
				if (world != null) {
					System.out.println("Congrats");

					((WorldState) GameStateEnum.World.getStateInstance()).setWorld(world);
				}
			}
		}
	}
}
