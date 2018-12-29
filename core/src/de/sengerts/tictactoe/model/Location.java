package de.sengerts.tictactoe.model;

/**
 * Immutable class representing a location in the game territory.
 * 
 * @author Tobias Senger
 */
public final class Location {

	/**
	 * Instance variable that stores the row number of this location
	 */
	private final int row;
	
	/**
	 * Instance variable that stores the column number of this location
	 */
	private final int column;

	/**
	 * Another constructor for class Location.
	 * 
	 * Creates a new object of type Location with given integer
	 * values for the row and column which can both not be negative.
	 * The top left of the territory represents the location at
	 * row 0 and column 0. The territory is increasing to the right
	 * and to the bottom.
	 *  
	 * @param row the row of the location
	 * @param column the column of the location
	 * @throws IllegalArgumentException if given row or column is negative.
	 */
	public Location(final int row, final int column) {
		super();
		if (row < 0 || column < 0) {
			throw new IllegalArgumentException("Row and column can not be negative!");
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * Getter for the row.
	 * 
	 * Returns the row integer value of this location object.
	 * Can not be negative.
	 * 
	 * @return row of this location object
	 */
	public /* @ pure @ */ int getRow() {
		return row;
	}

	/**
	 * Getter for the column.
	 * 
	 * Returns the column integer value of this location object.
	 * Can not be negative.
	 * 
	 * @return column of this location object
	 */
	public /* @ pure @ */ int getColumn() {
		return column;
	}

	/*
     * @see java.lang.Object#equals(java.lang.Object)
     */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Location)) {
			return false;
		}
		final Location objectLocation = (Location) object;
		return row == objectLocation.getRow() && column == objectLocation.getColumn();
	}
	
	/*
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		return "Location(row=" + row + ", column=" + column + ")";
	}

}
