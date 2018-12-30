package de.sengerts.tictactoe.logic;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.sengerts.tictactoe.model.GameState;
import de.sengerts.tictactoe.model.board.Location;
import de.sengerts.tictactoe.model.board.Territory;
import de.sengerts.tictactoe.model.players.AIPlayer;
import de.sengerts.tictactoe.model.players.HumanPlayer;
import de.sengerts.tictactoe.model.players.Player;
import de.sengerts.tictactoe.model.players.PlayerSign;
import de.sengerts.tictactoe.model.players.ai.EasyAIPlayer;
import de.sengerts.tictactoe.model.players.ai.HardAIPlayer;
import de.sengerts.tictactoe.model.players.ai.MediumAIPlayer;
import de.sengerts.tictactoe.model.settings.AIDifficulty;
import de.sengerts.tictactoe.model.settings.Dimension;

/**
 * Class representing a tic tac toe game.
 * 
 * @author Tobias Senger
 */
public class GameLogic {

	public static final Random RANDOM = new Random();
	public static final Timer TIMER = new Timer();

	/**
	 * Instance variable that stores the state of this tic tac toe game.
	 */
	private GameState gameState;

	/**
	 * Instance variable that stores the size of this tic tac toe game.
	 */
	private final Dimension size;

	/**
	 * Instance variable that stores whether the opponent of this tic tac toe game
	 * is an AI player.
	 */
	private final boolean aiOpponent;

	/**
	 * Instance variable that stores the AI difficulty of this tic tac toe game.
	 */
	private final AIDifficulty aiDifficulty;

	/**
	 * Instance variable that stores the territory of this tic tac toe game.
	 */
	private final Territory territory;

	/**
	 * Instance variable that stores both players of this tic tac toe game.
	 */
	private final List<Player> players;

	/**
	 * Instance variable that stores the list index of the player whose turn it is
	 */
	private int currentPlayerIndex;

	/**
	 * Instance variable that stores the player object of the winner of this tic tac
	 * toe game.
	 */
	private Player winner;

	/**
	 * Another constructor for class TicTacToeGame.
	 * 
	 * Creates a new object of type TicTacToeGame with the given size containing
	 * rows and column counts for the games territory.
	 * 
	 * @param size the size of the tic tac toe game territory
	 */
	public GameLogic(final Dimension size, final boolean aiOpponent, final AIDifficulty aiDifficulty) {
		if (size == null) {
			throw new IllegalArgumentException("Given size can not be null!");
		}
		if (size.getRowsCount() <= 0 || size.getColumnsCount() <= 0) {
			throw new IllegalArgumentException("Given sizes column count and row count must be positive!");
		}
		if (size.getRowsCount() != size.getColumnsCount()) {
			throw new IllegalArgumentException("Given size must be a square (equal rows and columns counts)!");
		}

		// TODO Verify that row/ column count is odd
		this.gameState = GameState.INGAME;
		this.size = size;
		this.aiOpponent = aiOpponent;
		this.aiDifficulty = aiDifficulty;
		this.players = new LinkedList<Player>();
		this.territory = new Territory(this);

		initPlayers();
	}

	/**
	 * Initializes both players of this tic tac toe game.
	 * 
	 * Initializes both players of this tic tac toe game by creating both a human
	 * player and an ai player and adding them to the set in the instance variable
	 * players.
	 */
	private void initPlayers() {
		HumanPlayer humanPlayer = new HumanPlayer(this, PlayerSign.X);
		Player opponentPlayer = aiOpponent ? getNewAIPlayer() : new HumanPlayer(this, PlayerSign.O);

		players.add(humanPlayer);
		players.add(opponentPlayer);

		this.currentPlayerIndex = RANDOM.nextInt(players.size());
		checkAIPlayersTurn();
	}

	/**
	 * Creates a new AI player.
	 * 
	 * Creates and returns a new AI Player with given set AI difficulty.
	 * 
	 * @return new AI Player with given difficulty
	 */
	private AIPlayer getNewAIPlayer() {
		if (aiDifficulty == AIDifficulty.EASY) {
			return new EasyAIPlayer(this, PlayerSign.O);
		} else if (aiDifficulty == AIDifficulty.MEDIUM) {
			return new MediumAIPlayer(this, PlayerSign.O);
		} else {
			return new HardAIPlayer(this, PlayerSign.O);
		}
	}

	/**
	 * Lets a human player make a move.
	 * 
	 * Lets a human player make a move by first verifying that it's the human
	 * players turn and then letting the current human player whose turn it is make
	 * a move.
	 */
	public void makeHumanPlayerMove() {
		if (!isHumanPlayersTurn()) {
			// TODO Alert player
			return;
		}
		getCurrentPlayer().makeMove();
	}

	/**
	 * Ends a players move.
	 * 
	 * Ends a players move by checking if the game should now end
	 * ({@see checkEnd()}) and if not, setting the next player whose turn it is
	 * ({@see setNextPlayer()}) and then checking if its currently the AI player's
	 * turn and if yes, letting it make its move ({@see checkAIPlayersTurn}).
	 */
	public void endMove() {
		checkEnd();
		if (getGameState() != GameState.INGAME) {
			return;
		}
		setNextPlayer();
		checkAIPlayersTurn();
	}

	/**
	 * Checks if its the AI players turn and if yes lets it make a move.
	 * 
	 * Checks if its the AI player's turn and if yes lets it make a move by first
	 * verifying that its the AI player's turn and then, after waiting 400
	 * milliseconds, letting the AI player make its next move.
	 */
	private void checkAIPlayersTurn() {
		Player currentPlayer = getCurrentPlayer();
		if (!currentPlayer.isAIPlayer()) {
			return;
		}
		TIMER.schedule(new TimerTask() {
			@Override
			public void run() {
				currentPlayer.makeMove();
			}
		}, 400);
	}

	/**
	 * Checks if this game should end and ends it if it should.
	 * 
	 * Checks if the game should end by first checking if there is a winner
	 * horizontally (in row), vertically (in column) or diagonally (in left or right
	 * diag) and if so, let this player win the game ({@see winGame(Player
	 * winner)}). If that is not the case, it also checks whether all fields are
	 * marked, resulting in the game being ended with a draw
	 * ({@see endGameWithDraw}).
	 */
	private void checkEnd() {
		Territory territory = getTerritory();

		// Check if there is a winner horizontally
		for (int row = 0; row < size.getRowsCount(); row++) {
			if (!territory.isRowCompletelyMarkedByOnePlayer(row))
				continue;

			Player winner = territory.getTile(new Location(row, 0)).getMarkedPlayer();
			winGame(winner);
			return;
		}

		// Check if there is a winner vertically
		for (int column = 0; column < size.getColumnsCount(); column++) {
			if (!territory.isColumnCompletelyMarkedByOnePlayer(column))
				continue;

			Player winner = territory.getTile(new Location(0, column)).getMarkedPlayer();
			winGame(winner);
			return;
		}

		// Check if there is a winner diagonally
		if (territory.isLeftDiagCompletelyMarkedByOnePlayer() || territory.isRightDiagCompletelyMarkedByOnePlayer()) {
			int midTileRow = territory.getSize().getRowsCount() / 2;
			int midTileColumn = territory.getSize().getColumnsCount() / 2;

			Player winner = territory.getTile(new Location(midTileRow, midTileColumn)).getMarkedPlayer();
			winGame(winner);
			return;
		}

		// Check if it is a draw (all tiles marked).
		if (territory.isEveryTileMarked()) {
			endGameWithDraw();
		}
	}

	/**
	 * End this game with a draw.
	 * 
	 * End this game with a draw by setting the value games state to DRAW.
	 */
	private void endGameWithDraw() {
		this.gameState = GameState.DRAW;
	}

	/**
	 * Lets {@bind winner} win this game.
	 * 
	 * Lets {@bind winner} win this game by setting the value of the instance
	 * variable winner to the given {@bind winner} and the games state to WON.
	 * 
	 * @param winner the player who won this tic tac toe game
	 */
	private void winGame(Player winner) {
		this.gameState = GameState.WON;
		this.winner = winner;
	}

	/**
	 * Sets the next player whose turn it is.
	 * 
	 * Sets the next player whose turn it is by incrementing the current player
	 * index value and resetting it to zero when it has reached the length of the
	 * players list.
	 */
	private void setNextPlayer() {
		currentPlayerIndex++;
		if (currentPlayerIndex == players.size()) {
			currentPlayerIndex = 0;
		}
	}

	/**
	 * Gets whether its currently the human player's turn.
	 * 
	 * Gets whether its the human player's turn by checking if the current player to
	 * mark a tile is not an AI player.
	 * 
	 * @return true if its the human players turn, otherwise false
	 */
	public boolean isHumanPlayersTurn() {
		return !players.get(currentPlayerIndex).isAIPlayer();
	}

	/**
	 * Gets the current player whose turn it is.
	 * 
	 * Gets the current player whose turn it is by getting the player object from
	 * the list of players by given index of the current player.
	 * 
	 * @return the player whose turn it is
	 */
	private Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}

	/**
	 * Gets if the game is ingame.
	 * 
	 * Gets if the game is ingame by checking if the games state
	 * {@see getGameState()} is GameState.INGAME.
	 * 
	 * @return true if this tic tac toe game is still ingame, otherwise returns
	 *         false
	 */
	public boolean isInGame() {
		return getGameState() == GameState.INGAME;
	}

	/**
	 * Gets if the game ended.
	 * 
	 * Gets if the game ended by checking if the games state {@see getGameState()}
	 * is not GameState.INGAME.
	 * 
	 * @return true if this tic tac toe game ended already, otherwise returns false
	 */
	public boolean isEnded() {
		return !isInGame();
	}

	/**
	 * Gets if the game ended with a draw.
	 * 
	 * Gets if the game ended with a draw by checking if the games state
	 * {@see getGameState()} is GameState.DRAW.
	 * 
	 * @return true if this tic tac toe game ended with a draw, in all other cases
	 *         returns false
	 */
	public boolean isDraw() {
		return getGameState() == GameState.DRAW;
	}

	/**
	 * Gets if the player is playing against AI.
	 * 
	 * Gets if the human player is playing against an AI opponent by checking if the
	 * count of AI players in the list in the instance variable players is greater
	 * than zero.
	 * 
	 * @return true if opponent is AI, otherwise false
	 */
	public boolean isPlayingAgainstAI() {
		return players.stream().filter(player -> player.isAIPlayer()).count() > 0;
	}

	/**
	 * Getter for the size.
	 * 
	 * Returns the size of this tic tac toe game representing the rows and columns
	 * counts of its square territory.
	 * 
	 * @return the size of this territory
	 */
	public /* @ pure @ */ Dimension getSize() {
		return size;
	}

	/**
	 * Getter for the games territory.
	 * 
	 * Returns the territory object for this tic tac toe game.
	 * 
	 * @return the territory of this tic tac toe game
	 */
	public /* @ pure @ */ Territory getTerritory() {
		return territory;
	}

	/**
	 * Getter for the games players set.
	 * 
	 * Returns a set of player objects for this tic tac toe game.
	 * 
	 * @return the players of this tic tac toe game
	 */
	public /* @ pure @ */ List<Player> getPlayers() {
		return players;
	}

	/**
	 * Getter for the games state.
	 * 
	 * Returns the value of the instance variable gameState. Possible values for the
	 * game state are INGAME, DRAW and WON.
	 * 
	 * @return the state of this tic tac toe game.
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * Getter for the games winner.
	 * 
	 * Returns the value of the instance variable winner containing null or a
	 * reference to a player who won this tic tac toe game.
	 * 
	 * @return the winner of this tic tac toe game (null if not won yet).
	 */
	public Player getWinner() {
		return winner;
	}
}
