package de.sengerts.tictactoe.model.players;

import de.sengerts.tictactoe.logic.GameLogic;

/**
 * Class representing a player of a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public abstract class Player {
	
	/**
	 * Instance variable that stores the tic tac toe game of this player.
	 */
	private final GameLogic gameLogic;
	
	/**
	 * Instance variable that stores the player sign of this player.
	 */
	private final PlayerSign playerSign;

	/**
	 * Another constructor for class Player.
	 * 
	 * Creates a new object of type Player with
	 * the given tic tac toe game and player sign.
	 * 
	 * @param ticTacToeGame the tic tac toe game this player is associated with
	 * @param playerSign the sign of this player
	 * @throws IllegalArgumentException if given player sign is null
	 */
	public Player(final GameLogic gameLogic, final PlayerSign playerSign) {
		super();
		if(gameLogic == null) {
			throw new IllegalArgumentException("Given Game Logic object can not be null!");
		}
		if(playerSign == null) {
			throw new IllegalArgumentException("Given player sign object can not be null!");
		}
		this.gameLogic = gameLogic;
		this.playerSign = playerSign;
	}
	
	/**
	 * Abstract method for the make move logic in a tic tac toe game.
	 * 
	 * Here, the player picks a field which should be marked.
	 * After that the tic tac toe game this player is associated with
	 * checks whether there now is a draw or winner and decides what
	 * to do next.
	 * This method is implemented by subclasses of this parent class.
	 */
	public abstract void makeMove();
	
	/**
	 * Indicates whether a player is an AI player or not.
	 * 
	 * @return true when player is instance of AIPlayer, otherwise false
	 */
	public abstract boolean isAIPlayer();
	
	/**
	 * Getter for this players tic tac toe game.
	 * 
	 * Returns the tic tac toe game object that 
	 * this player is associated with.
	 * 
	 * @return this players tic tac toe game
	 */
	public /* @ pure @ */ GameLogic getGameLogic() {
		return gameLogic;
	}
	
	/**
	 * Getter for the player sign.
	 * 
	 * Returns the name of this players player sign
	 * enum value.
	 * 
	 * @return name of the player sign enum value of this player
	 */
	public /* @ pure @ */ String getName() {
		return playerSign.name();
	}

	/**
	 * Getter for the player sign.
	 * 
	 * Returns the player sign of this player object.
	 * Can not be null.
	 * 
	 * @return player sign object of this player
	 */
	public /* @ pure @ */ PlayerSign getPlayerSign() {
		return playerSign;
	}

}
