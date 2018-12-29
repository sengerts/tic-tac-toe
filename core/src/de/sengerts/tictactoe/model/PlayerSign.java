package de.sengerts.tictactoe.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

/**
 * Enumeration of the signs players in the game can have.
 * Its values are X and O.
 * 
 * @author Tobias Senger
 */
public enum PlayerSign {
	
	/**
	 * Player X / blue sign
	 */
	X("TTT_BLUE"),
	
	/**
	 * Player O / red sign
	 */
	O("TTT_RED");
	
	/**
	 * Instance variable representing the color
	 * name of this player sign.
	 */
	private String colorName;
	
	/**
	 * Constructor for enum PlayerSign.
	 * 
	 * @param color the color of the player sign
	 */
	private PlayerSign(final String colorName) {
		this.colorName = colorName;
	}
	
	/**
	 * Getter for the color name.
	 * 
	 * Returns the colors internal name.
	 * 
	 * @return color name for this player sign value
	 */
	public /* @ pure @ */ String getColorName() {
		return colorName;
	}
	
	/**
	 * Getter for the color.
	 * 
	 * Returns a color object of this player sign enum value.
	 * 
	 * @return color of this player sign value
	 */
	public /* @ pure @ */ Color getColor() {
		return Colors.get(colorName);
	}

}
