package de.sengerts.tictactoe.exceptions;

/**
 * Class representing an exception when a tile is already marked.
 * 
 * @author Tobias Senger
 */
public class TileAreadyMarkedException extends Exception {

	/**
	 * Serial version UID of this exception class.
	 */
	private static final long serialVersionUID = -612553262317812517L;
	
	/**
	 * Another Constructor for class TileAreadyMarkedException.
	 * 
	 * Here an exception is created with a message that the tile is
	 * already marked by a player.
	 */
	public TileAreadyMarkedException() {
		super("Tile is already marked by a player.");
	}

}
