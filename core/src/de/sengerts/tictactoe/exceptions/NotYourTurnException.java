package de.sengerts.tictactoe.exceptions;

public class NotYourTurnException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5743721926863432298L;

	public NotYourTurnException() {
		super("Sorry, but it's not your turn!");
	}

}
