package de.sengerts.tictactoe.exceptions;

public class TileAreadyMarkedException extends Exception {

	/**
	 * Serial version UID of this exception class.
	 */
	private static final long serialVersionUID = -612553262317812517L;
	
	public TileAreadyMarkedException() {
		super("Tile is already marked by a player.");
	}

}
