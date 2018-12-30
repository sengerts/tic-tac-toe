package de.sengerts.tictactoe.model.board;

import de.sengerts.tictactoe.exceptions.TileAreadyMarkedException;
import de.sengerts.tictactoe.model.players.Player;

/**
 * Class representing one tile of the game territory at a specific location that
 * can be marked or not.
 * 
 * @author Tobias Senger
 */
public class Tile {

	/**
	 * Instance variable that stores the location of the tile.
	 */
	private final Location location;

	/**
	 * Instance variable that, when tile is marked, stores a player object
	 * representing the player who marked this tile during a game. Is null by
	 * default.
	 */
	private Player markedPlayer;

	/**
	 * Another constructor for class Tile.
	 * 
	 * Creates a new tile object with a specific given location.
	 * 
	 * @param location the location of this tile, can not be null
	 * @throws IllegalArgumentException if given location is null
	 */
	public Tile(final Location location) {
		super();
		if (location == null) {
			throw new IllegalArgumentException("Given location can not be null!");
		}
		// TODO Check if tile already exists
		this.location = location;
	}

	/**
	 * Getter for the location.
	 * 
	 * Returns the location object of this tile. Can not be null.
	 * 
	 * @return location of this tile object
	 */
	public /* @ pure @ */ Location getLocation() {
		return location;
	}

	/**
	 * Getter for the marked player.
	 * 
	 * Returns a player object of this tile if this tile is marked. Returns null by
	 * default as long as no player marked this tile in the game.
	 * 
	 * @return marked player object of this tile object
	 */
	public /* @ pure @ */ Player getMarkedPlayer() {
		return markedPlayer;
	}

	/**
	 * Returns whether this tile is marked.
	 * 
	 * Returns whether this tile is marked by returning true if the marked player
	 * object of this tile object is not null, otherwise it returns false.
	 * 
	 * @return true if marked player object is not null, otherwise false
	 */
	public /* @ pure @ */ boolean isMarked() {
		return markedPlayer != null;
	}

	/**
	 * Sets the marked player.
	 * 
	 * Sets a player representing the player who marked this tile by assigning the
	 * given player object to the instance variable markedPlayer.
	 * 
	 * @param markedPlayer the player to mark this tile by
	 * @throws TileAreadyMarkedException if tile has already been marked by a player
	 */
	public void setMarkedPlayer(Player markedPlayer) throws TileAreadyMarkedException {
		if (isMarked()) {
			throw new TileAreadyMarkedException();
		}
		this.markedPlayer = markedPlayer;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Tile)) {
			return false;
		}
		final Tile objectTile = (Tile) object;
		return objectTile.getLocation().equals(location);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tile(location=" + location.toString() + ", markedPlayer=" 
				+ (isMarked() ? markedPlayer.getName() : "-") + ")";
	}

}
