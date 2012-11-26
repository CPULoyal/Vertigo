package me.matej.Vertigo.WebService;

/**
 * @author matejkramny
 */
public class WorldListing {
	private String id;
	private String name;
	private String description;
	private int created;
	private int downloads;
	private String version;

	public String getId () {
		return id;
	}
	public String getName () {
		return name;
	}
	public String getDescription () {
		return description;
	}
	public int getCreated () {
		return created;
	}
	public int getDownloads () {
		return downloads;
	}
	public String getVersion () {
		return version;
	}
}
