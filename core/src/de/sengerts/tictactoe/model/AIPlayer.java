package de.sengerts.tictactoe.model;

import java.util.List;

import de.sengerts.tictactoe.exceptions.TileAreadyMarkedException;
import de.sengerts.tictactoe.logic.GameLogic;

/**
 * Class representing a AI player of a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class AIPlayer extends Player {

	/**
	 * Another constructor for class AIPlayer.
	 * 
	 * Creates a new AIPlayer object extending the Player class with the given tic
	 * tac toe game object and player sign object.
	 * 
	 * @param ticTacToeGame the tic tac toe game this player is associated with
	 * @param playerSign    the sign of this AI player
	 */
	public AIPlayer(final GameLogic ticTacToeGame, final PlayerSign playerSign) {
		super(ticTacToeGame, playerSign);
	}

	/**
	 * Lets the AI player make an automated move.
	 * 
	 * Here, the AI player calculates a fitting next field which he then marks
	 * trying to win the tic tac toe game.
	 */
	@Override
	public void makeMove() {
		// TODO Not just randomly select a tile to mark
		
		while (true) {
			try {
				List<Tile> tiles = getGameLogic().getTerritory().getTiles();
				Tile tile = tiles.get(GameLogic.RANDOM.nextInt(tiles.size()));
				tile.setMarkedPlayer(this);
				
				getGameLogic().endMove();
				return;
			} catch (TileAreadyMarkedException e) {
			}
		}
	}
	
	/**
	 * Returns true indicating that this player object is an AI Player.
	 * @return true
	 */
	@Override
	public boolean isAIPlayer() {
		return true;
	}

}
