package de.sengerts.tictactoe.model.players.ai;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import de.sengerts.tictactoe.exceptions.TileAreadyMarkedException;
import de.sengerts.tictactoe.logic.GameLogic;
import de.sengerts.tictactoe.model.board.Tile;
import de.sengerts.tictactoe.model.players.AIPlayer;
import de.sengerts.tictactoe.model.players.PlayerSign;
import de.sengerts.tictactoe.model.settings.Dimension;

/**
 * Class representing a medium AI player.
 * 
 * @author Tobias Senger
 */
public class MediumAIPlayer extends AIPlayer {

	/**
	 * Another constructor for class MediumAIPlayer.
	 * 
	 * Creates a new MediumAIPlayer object extending the Player class with the given
	 * game logic object and player sign object.
	 * 
	 * @param gameLogic  the game logic of the tic tac toe game this player is
	 *                   associated with
	 * @param playerSign the sign of this AI player
	 */
	public MediumAIPlayer(GameLogic gameLogic, PlayerSign playerSign) {
		super(gameLogic, playerSign);
	}

	/**
	 * Lets the AI player make an automated move.
	 * 
	 * Here, the AI player marks a last tile in a row/ column or diag in which all
	 * tiles but one are marked by the human opponent player, otherwise a randomly
	 * chosen tile which is not yet marked will be marked. After that, the AI player
	 * ends his move.
	 */
	@Override
	public void makeMove() {
		Tile currentTile = null;
		if ((currentTile = getLastUnmarkedRowOrColumnTile()) != null
				|| (currentTile = getLastUnmarkedDiagTile()) != null) {
			try {
				currentTile.setMarkedPlayer(this);
			} catch (TileAreadyMarkedException e) {
				// Not reachable
			}
		} else {
			markRandomTile();
		}
		getGameLogic().endMove();
	}

	/**
	 * Gets a last game-deciding unmarked row or column tile.
	 * 
	 * Gets a last game-deciding unmarked row or column tile in one row or column of
	 * the territory and returns it if there is one. Otherwise, it returns null.
	 * 
	 * @return a last game-deciding unmarked row or column tile or null if none
	 *         found
	 */
	private Tile getLastUnmarkedRowOrColumnTile() {
		Dimension territorySize = getGameLogic().getSize();
		for (int i = 0; i < territorySize.getRowsCount(); i++) {
			Supplier<Stream<Tile>> tileRowStreamSupplier = getGameLogic().getTerritory().getRowTilesStreamSupplier(i);
			if (hasHumanPlayerMarkedAllButOne(tileRowStreamSupplier)) {
				Tile lastUnmarkedRowTile = getUnmarkedTile(tileRowStreamSupplier);
				return lastUnmarkedRowTile;
			}

			Supplier<Stream<Tile>> tileColumnStreamSupplier = getGameLogic().getTerritory()
					.getColumnTilesStreamSupplier(i);
			if (hasHumanPlayerMarkedAllButOne(tileColumnStreamSupplier)) {
				Tile lastUnmarkedColumnTile = getUnmarkedTile(tileColumnStreamSupplier);
				return lastUnmarkedColumnTile;
			}
		}
		return null;
	}

	/**
	 * Gets a last game-deciding unmarked diagonal tile.
	 * 
	 * Gets a last game-deciding unmarked diagonal tile in the left or right diag
	 * and returns it if there is one. Otherwise, it returns null.
	 * 
	 * @return a last game-deciding unmarked diagonal tile or null if none found
	 */
	private Tile getLastUnmarkedDiagTile() {
		Supplier<Stream<Tile>> tileLeftDiagStreamSupplier = getGameLogic().getTerritory()
				.getLeftDiagTilesStreamSupplier();
		if (hasHumanPlayerMarkedAllButOne(tileLeftDiagStreamSupplier)) {
			Tile lastUnmarkedLeftDiagTile = getUnmarkedTile(tileLeftDiagStreamSupplier);
			return lastUnmarkedLeftDiagTile;
		}

		Supplier<Stream<Tile>> tileRightDiagStreamSupplier = getGameLogic().getTerritory()
				.getRightDiagTilesStreamSupplier();
		if (hasHumanPlayerMarkedAllButOne(tileRightDiagStreamSupplier)) {
			Tile lastUnmarkedRightDiagTile = getUnmarkedTile(tileRightDiagStreamSupplier);
			return lastUnmarkedRightDiagTile;
		}
		return null;
	}

	/**
	 * Gets an unmarked tile in the given stream of tiles.
	 * 
	 * Gets an unmarked tile in the given stream of tiles by filtering the stream of
	 * tiles by the one that are not marked and getting the first element of this
	 * filtered tiles stream.
	 * 
	 * @param tileStreamSupplier a supplier for the stream of tiles to get an
	 *                           unmarked one from
	 * @return an umarked tile from the given stream of tiles
	 */
	private Tile getUnmarkedTile(Supplier<Stream<Tile>> tileStreamSupplier) {
		return tileStreamSupplier.get().filter(tile -> !tile.isMarked()).findFirst().get();
	}

	/**
	 * Gets whether the human player has marked all but one tile in a given stream
	 * of tiles.
	 * 
	 * Gets whether the human player has marked all but one tile in a given stream
	 * of tiles by checking if there is exactly one unmarked tile left in the stream
	 * of tiles and by checking if the amount of tiles that are marked by the human
	 * player in the given stream of tiles is the row/ column count of the territory
	 * minus 1.
	 * 
	 * @param tileStreamSupplier a supplier for the stream of tiles to check
	 * @return true when the human player has marked all but one tile in the given
	 *         tiles stream, otherwise returns false
	 */
	private boolean hasHumanPlayerMarkedAllButOne(Supplier<Stream<Tile>> tileStreamSupplier) {
		boolean isExactlyOneTileUnmarked = tileStreamSupplier.get().filter(tile -> !tile.isMarked()).count() == 1;
		
		return isExactlyOneTileUnmarked && tileStreamSupplier.get()
				.filter(tile -> tile.isMarked() && !tile.getMarkedPlayer().isAIPlayer()).count()
					== getGameLogic().getSize().getRowsCount() - 1;
	}

	/**
	 * Lets the AI player mark a random tile.
	 * 
	 * Here, the AI player marks a randomly chosen and not yet marked tile.
	 */
	private void markRandomTile() {
		while (true) {
			try {
				List<Tile> tiles = getGameLogic().getTerritory().getTiles();
				Tile tile = tiles.get(GameLogic.RANDOM.nextInt(tiles.size()));
				tile.setMarkedPlayer(this);
				return;
			} catch (TileAreadyMarkedException e) {
			}
		}
	}

}
