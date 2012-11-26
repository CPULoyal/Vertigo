package me.matej.Vertigo.WebService;

import com.google.gson.Gson;
import me.matej.Vertigo.World.World;

import java.util.ArrayList;

/**
 * @author matejkramny
 */

public class ConnectionWrapper {
	public String[] getListingIDs () {
		SocketConnection sc = new SocketConnection("localhost", 3000, true);
		if (sc.connect()) {
			SocketConnection.writeHTTPHead(sc);
			sc.writeHeader("host", "localhost");
			sc.write();
			sc.read();

			String[] ids = sc.getData().toString().split("\n");
			return ids;
		}

		assert false : "Not connected";
		return null;
	}

	public WorldListing[] getListings (String[] ids) {
		if (ids.length == 0) {
			assert false : "Empty ids";
			return null;
		}

		Gson gson = new Gson();
		ArrayList<WorldListing> listings = new ArrayList<WorldListing>();

		for (String id : ids) {
			SocketConnection sc = new SocketConnection("localhost", 3000, true);
			if (sc.connect()) {
				SocketConnection.writeHTTPHead("/"+id, sc);
				sc.writeHeader("host", "localhost");
				sc.write();
				sc.read();
				WorldListing listing = gson.fromJson(sc.getData().toString(), WorldListing.class);
				listings.add(listing);
			} else {
				assert false : "Not connected";
			}
		}

		WorldListing[] arr = listings.toArray(new WorldListing[] {});
		return arr;
	}

	public World getWorld (WorldListing listing) {
		SocketConnection sc = new SocketConnection("localhost", 3000, true);
		if (sc.connect()) {
			SocketConnection.writeHTTPHead("/download/"+listing.getId(), sc);
			sc.writeHeader("host", "localhost");
			sc.write();
			sc.read();

			Gson gson = new Gson();
			World world = gson.fromJson(sc.getData().toString(), World.class);

			return world;
		}

		assert false : "Connection failed";

		return null;
	}
}
