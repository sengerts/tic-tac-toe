package de.sengerts.tictactoe.model.ai;

import java.util.List;

import de.sengerts.tictactoe.exceptions.TileAreadyMarkedException;
import de.sengerts.tictactoe.logic.GameLogic;
import de.sengerts.tictactoe.model.AIPlayer;
import de.sengerts.tictactoe.model.PlayerSign;
import de.sengerts.tictactoe.model.Tile;

public class HardAIPlayer extends AIPlayer {

	/**
	 * Another constructor for class HardAIPlayer.
	 * 
	 * Creates a new HardAIPlayer object extending the Player class with the given
	 * game logic object and player sign object.
	 * 
	 * @param gameLogic the game logic of the tic tac toe game this player is associated with
	 * @param playerSign the sign of this AI player
	 */
	public HardAIPlayer(GameLogic gameLogic, PlayerSign playerSign) {
		super(gameLogic, playerSign);
	}
	
	/**
	 * Lets the AI player make an automated move.
	 * 
	 * Here, the AI player marks a randomly chosen and not yet marked
	 * tile and then ends his move.
	 */
	@Override
	public void makeMove() {
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
	
}
