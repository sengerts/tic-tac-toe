package de.sengerts.tictactoe.model.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Scaling;

import de.sengerts.tictactoe.exceptions.TileAreadyMarkedException;
import de.sengerts.tictactoe.logic.GameLogic;
import de.sengerts.tictactoe.model.board.Location;
import de.sengerts.tictactoe.model.board.Tile;

/**
 * Class representing a human player of a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class HumanPlayer extends Player {

	/**
	 * Another constructor for class HumanPlayer.
	 * 
	 * Creates a new HumanPlayer object extending the Player class with the given
	 * tic tac toe game object and player sign object.
	 * 
	 * @param ticTacToeGame the tic tac toe game this player is associated with
	 * @param playerSign    the sign of this human player
	 */
	public HumanPlayer(final GameLogic gameLogic, final PlayerSign playerSign) {
		super(gameLogic, playerSign);
	}

	/**
	 * Lets the human player mark a tile.
	 * 
	 * Here, the human player marks a tile at his current mouse position if
	 * possible.
	 */
	@Override
	public void makeMove() {
		Location tileLocation = null;
		try {
			tileLocation = getClickedTileLocation();
		} catch (IllegalArgumentException ex) {
			return;
		}

		if (tileLocation == null || !getGameLogic().getTerritory().isLocationInTerritory(tileLocation)) {
			return;
		}

		Tile tile = getGameLogic().getTerritory().getTile(tileLocation);
		markTile(tile);
	}

	/**
	 * Returns false indicating that this player object is a Human Player.
	 * 
	 * @return false
	 */
	@Override
	public boolean isAIPlayer() {
		return false;
	}
	
	/**
	 * Gets a territory location from the user's mouse position.
	 * 
	 * Gets a territory location from the users mouse position by 
	 * calculating the difference between the mouse position and
	 * the viewport position and finally calculating which location has been clicked. Throws
	 * IllegalArgumentException or returns null if clicked outside of the territory.
	 * 
	 * @return location at user's mouse position
	 */
	private Location getClickedTileLocation() {
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		int tileWidth = height / getGameLogic().getSize().getRowsCount();

		Vector2 size = Scaling.fit.apply(800, 800, width, height);
		int viewportX = (int) (width - size.x) / 2;

		int mouseX = Gdx.input.getX();
		int mouseY = Gdx.input.getY();
		
		if (viewportX > mouseX) {
			return null;
		}

		int tileRow = mouseY / tileWidth;
		int tileColumn = (mouseX - viewportX) / tileWidth;

		// TODO Fix column (is one too big if clicked in right half of a tile)
		
		return new Location(tileRow, tileColumn);
	}

	/**
	 * Lets the human player mark a given tile.
	 * 
	 * Lets the human player mark a given tile if it is not marked yet by another
	 * player.
	 * 
	 * @param tile the tile to mark by this human player
	 */
	private void markTile(Tile tile) {
		try {
			tile.setMarkedPlayer(this);
		} catch (TileAreadyMarkedException e) {
			// TODO Alert player
			return;
		}
		getGameLogic().endMove();
	}

}
