package de.sengerts.tictactoe.model.board;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.sengerts.tictactoe.logic.GameLogic;
import de.sengerts.tictactoe.model.players.Player;
import de.sengerts.tictactoe.model.settings.Dimension;

/**
 * Class representing the territory of a tic tac toe game.
 * 
 * A territory is a rectangular area, composed of multiple tiles, with equal
 * width and height specified by a given positive size.
 * 
 * @author Tobias Senger
 */
public class Territory {

	/**
	 * Instance variable that stores the tic tac toe game of this player.
	 */
	private final GameLogic ticTacToeGame;

	/**
	 * Instance variable that stores a list of Tile objects for this territory
	 * object.
	 */
	private final List<Tile> tiles;

	/**
	 * Another constructor for class Territory.
	 * 
	 * Creates a new object of type Territory with a given not null tic tac toe game
	 * that this territory is associated with containing the size for this
	 * territory.
	 * 
	 * @param ticTacToeGame the tic tac toe game this territory is associated with
	 * @throws IllegalArgumentException if given size is not positive
	 */
	public Territory(final GameLogic ticTacToeGame) {
		if (ticTacToeGame == null) {
			throw new IllegalArgumentException("Given Tic Tac Toe game object can not be null!");
		}
		this.ticTacToeGame = ticTacToeGame;
		this.tiles = new LinkedList<Tile>();

		initTerritory();
	}

	/**
	 * Initializes the territory.
	 * 
	 * Initializes the territory by creating the size*size tile objects making up
	 * this square tic tac toe territory and adding them to the tiles list.
	 */
	private void initTerritory() {
		for (int row = 0; row < getSize().getRowsCount(); row++) {
			for (int column = 0; column < getSize().getColumnsCount(); column++) {
				Location tileLocation = new Location(row, column);
				Tile tile = new Tile(tileLocation);

				tiles.add(tile);
			}
		}
	}

	/**
	 * Gets if all tiles in this territory are marked.
	 * 
	 * Gets if all tiles in this territory are marked by filtering the stream of the
	 * list of tiles by not marked tiles and checking if the count of the filtered
	 * stream is zero.
	 * 
	 * @return true if all tiles in this territory are marked, otherwise false
	 */
	public boolean isEveryTileMarked() {
		return tiles.stream().filter(tile -> !tile.isMarked()).count() == 0;
	}

	/**
	 * Gets if a given row in this territory is completely marked by one player.
	 * 
	 * Gets if a given row in this territory is completely marked by one player by
	 * checking if all elements of the stream of all tiles in this territory
	 * filtered by the given row are marked and all tiles have been marked by the
	 * same player. {@see isStreamOfTilesCompletelyMarkedByOnePlayer(Stream<Tile>
	 * tilesStream}
	 * 
	 * @param row the row of the tiles to check
	 * @return true if all tiles in the row are marked by the same player, otherwise
	 *         false
	 */
	public boolean isRowCompletelyMarkedByOnePlayer(int row) {
		Supplier<Stream<Tile>> rowTilesStreamSupplier = getRowTilesStreamSupplier(row);
		return isStreamOfTilesCompletelyMarkedByOnePlayer(rowTilesStreamSupplier);
	}

	/**
	 * Gets if a given column in this territory is completely marked by one player.
	 * 
	 * Gets if a given column in this territory is completely marked by one player
	 * by checking if all elements of the stream of all tiles in this territory
	 * filtered by the given column are marked and all tiles have been marked by the
	 * same player. {@see isStreamOfTilesCompletelyMarkedByOnePlayer(Stream<Tile>
	 * tilesStream}
	 * 
	 * @param column the column of the tiles to check
	 * @return true if all tiles in the column are marked by the same player,
	 *         otherwise false
	 */
	public boolean isColumnCompletelyMarkedByOnePlayer(int column) {
		Supplier<Stream<Tile>> columnTilesStreamSupplier = getColumnTilesStreamSupplier(column);
		return isStreamOfTilesCompletelyMarkedByOnePlayer(columnTilesStreamSupplier);
	}

	/**
	 * Gets if the left diag in this territory is completely marked by one player.
	 * 
	 * Gets if the left diag in this territory is completely marked by one player by
	 * checking if all tiles in the left diag of this territory are marked and
	 * marked by the same player.
	 * 
	 * @return true if left diag is completely marked by the same player, otherwise
	 *         false
	 */
	public boolean isLeftDiagCompletelyMarkedByOnePlayer() {
		Supplier<Stream<Tile>> leftDiagTilesStreamSupplier = getLeftDiagTilesStreamSupplier();
		return isStreamOfTilesCompletelyMarkedByOnePlayer(leftDiagTilesStreamSupplier);
	}

	/**
	 * Gets if the right diag in this territory is completely marked by one player.
	 * 
	 * Gets if the right diag in this territory is completely marked by one player
	 * by checking if all tiles in the right diag of this territory are marked and
	 * marked by the same player.
	 * 
	 * @return true if right diag is completely marked by the same player, otherwise
	 *         false
	 */
	public boolean isRightDiagCompletelyMarkedByOnePlayer() {
		Supplier<Stream<Tile>> rightDiagTilesStreamSupplier = getRightDiagTilesStreamSupplier();
		return isStreamOfTilesCompletelyMarkedByOnePlayer(rightDiagTilesStreamSupplier);
	}

	/**
	 * Gets whether the given stream of tiles is marked completely by one player.
	 * 
	 * Gets whether the given stream of tiles is marked completely by one player by
	 * checking if the first tile is marked and if all other tiles are marked by the
	 * same player as the first one.
	 * 
	 * @param tilesStream the stream of tiles to check
	 * @return true when all tiles in the stream of tiles are marked by the same
	 *         person, otherwise false
	 */
	private boolean isStreamOfTilesCompletelyMarkedByOnePlayer(Supplier<Stream<Tile>> tilesStreamSupplier) {
		Player firstMarkedPlayer = tilesStreamSupplier.get().findFirst().get().getMarkedPlayer();
		if (firstMarkedPlayer == null) {
			return false;
		}
		return tilesStreamSupplier.get().allMatch(tile -> (tile.isMarked()
				&& tile.getMarkedPlayer().getPlayerSign() == firstMarkedPlayer.getPlayerSign()));
	}
	
	/**
	 * Gets a supplier of the stream of all tiles in the left diag in this
	 * territory.
	 * 
	 * Gets a supplier of the stream of all tiles in the left diag in this territory
	 * by filtering the stream of all tiles in this territory by the ones that have
	 * the same id for row and column (e.g. (0, 0) or (1, 1)).
	 * 
	 * @return a supplier of the stream of all tiles in the left diag
	 */
	public Supplier<Stream<Tile>> getLeftDiagTilesStreamSupplier() {
		return () -> (tiles.stream().filter(tile -> tile.getLocation().getRow() == tile.getLocation().getColumn()));
	}

	/**
	 * Gets a supplier of the stream of all tiles in the right diag in this
	 * territory.
	 * 
	 * Gets a supplier of the stream of all tiles in the right diag in this
	 * territory by filtering the stream of all tiles in this territory by the ones
	 * for whom the column id equals the columns count of this territory minus the
	 * row id of the tile. (e.g. (0, 2) or (1, 1)).
	 * 
	 * @return a supplier of the stream of all tiles in the right diag
	 */
	public Supplier<Stream<Tile>> getRightDiagTilesStreamSupplier() {
		return () -> (tiles.stream().filter(
				tile -> (tile.getLocation().getRow() + tile.getLocation().getColumn() == getSize().getColumnsCount() - 1)));
	}

	/**
	 * Gets a supplier for the stream of all tiles in a given row in this territory.
	 * 
	 * Gets a supplier for the stream of all tiles in a given row in this territory
	 * by filtering the stream of all tiles in this territory by the ones in the
	 * given row.
	 * 
	 * @param row the row of the searched tiles
	 * @return a supplier for the stream of all tiles in the given row
	 */
	public Supplier<Stream<Tile>> getRowTilesStreamSupplier(int row) {
		return () -> (tiles.stream().filter(tile -> tile.getLocation().getRow() == row));
	}

	/**
	 * Gets a supplier for a stream of all tiles in a given column in this
	 * territory.
	 * 
	 * Gets a supplier for a stream of all tiles in a given column in this territory
	 * by filtering the stream of all tiles in this territory by the ones in the
	 * given column.
	 * 
	 * @param column the column of the searched tiles
	 * @return a supplier for the stream of all tiles in the given column
	 */
	public Supplier<Stream<Tile>> getColumnTilesStreamSupplier(int column) {
		return () -> (tiles.stream().filter(tile -> tile.getLocation().getColumn() == column));
	}

	/**
	 * Gets a tile at a location.
	 * 
	 * Gets a tile at a given location by caculating the tile index by given
	 * location ({@see getTileIndexByLocation(location)}) and returning the element
	 * with this index from the list in the instance variable tiles.
	 * 
	 * @param location the location to get the tile from
	 * @return the tile at the given location
	 * @throws IllegalArgumentException  if given location is null
	 * @throws TileDoesNotExistException if there is no tile at the given location
	 */
	public Tile getTile(final Location location) {
		if (location == null) {
			throw new IllegalArgumentException("Given location can not be null!");
		}
		if (!isLocationInTerritory(location)) {
			throw new IllegalArgumentException("Given location is not inside the territory!");
		}
		return tiles.get(getTileIndexByLocation(location));
	}

	/**
	 * Gets the number of tiles in this territory.
	 * 
	 * Gets the number of tiles in this territory by getting the size of the list of
	 * tiles in the instance variable tiles.
	 * 
	 * @return size of the list of tiles of this territory
	 */
	public int getTilesAmount() {
		return tiles.size();
	}

	/**
	 * Gets if a location is inside the territory.
	 * 
	 * Gets if a location is inside the territory by checking if its row and column
	 * are both less than the size of the territory whereas the row and column of
	 * the location are at least zero by default.
	 * 
	 * @param location the location to check
	 * @return true if the location is inside, otherwise returns false
	 */
	public boolean isLocationInTerritory(final Location location) {
		if (location == null) {
			throw new IllegalArgumentException("Given location can not be null!");
		}
		return location.getRow() < getSize().getRowsCount() && location.getColumn() < getSize().getColumnsCount();
	}

	/**
	 * Gets the index of a tile by its location.
	 * 
	 * Gets the index of a tile for the list in the instance tiles by multiplying
	 * the locations row with the width of the territory (column count) as an offset
	 * and adding the column of the location.
	 * 
	 * @param location the location to get the tiles list index of
	 * @return the index for a tile at a given location
	 */
	public int getTileIndexByLocation(final Location location) {
		if (location == null) {
			throw new IllegalArgumentException("Given location can not be null!");
		}
		return location.getRow() * getSize().getColumnsCount() + location.getColumn();
	}

	/**
	 * Gets this players tic tac toe game.
	 * 
	 * Returns the tic tac toe game object that this territory is associated with.
	 * 
	 * @return this territorys tic tac toe game
	 */
	public /* @ pure @ */ GameLogic getTicTacToeGame() {
		return ticTacToeGame;
	}

	/**
	 * Gets the tiles.
	 * 
	 * Returns all tiles of this territory object representing all fields of this
	 * square tic tac toe territory.
	 * 
	 * @return the list of tiles for this territory
	 */
	public /* @ pure @ */ List<Tile> getTiles() {
		return tiles;
	}

	/**
	 * Gets the size.
	 * 
	 * Returns the size of this territory object representing the rows and columns
	 * counts of this square territory.
	 * 
	 * @return the size of this territory
	 */
	public /* @ pure @ */ Dimension getSize() {
		return ticTacToeGame.getSize();
	}

}
