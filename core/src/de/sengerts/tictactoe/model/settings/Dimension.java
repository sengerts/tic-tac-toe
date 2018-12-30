package de.sengerts.tictactoe.model.settings;

/**
 * Immutable class representing a dimension for the game territory.
 * 
 * @author Tobias Senger
 */
public final class Dimension {

	/**
	 * Instance variable that stores the amount of rows
	 */
	private final int rowsCount;
	
	/**
	 * Instance variable that stores the amount of columns
	 */
	private final int columnsCount;

	/**
	 * Another constructor for class Dimension.
	 * 
	 * Creates a new object of type Dimension with given integer
	 * values for the row count and column count which should both
	 * be greater than zero.
	 *  
	 * @param rowCount the amount of rows for this dimension
	 * @param columnCount the amount of columns for this dimension
	 * @throws IllegalArgumentException if given row or column is not greater than zero.
	 */
	public Dimension(final int rowsCount, final int columnsCount) {
		super();
		if (rowsCount <= 0 || columnsCount <= 0) {
			throw new IllegalArgumentException("Row and column count must be greater than zero!");
		}
		this.rowsCount = rowsCount;
		this.columnsCount = columnsCount;
	}

	/**
	 * Getter for the rows count.
	 * 
	 * Returns the rows count integer value of this dimension object.
	 * Can not be zero or less.
	 * 
	 * @return rows count of this dimension object
	 */
	public /* @ pure @ */ int getRowsCount() {
		return rowsCount;
	}

	/**
	 * Getter for the columns count.
	 * 
	 * Returns the columns count integer value of this dimension object.
	 * Can not be zero or less.
	 * 
	 * @return rows count of this dimension object
	 */
	public /* @ pure @ */ int getColumnsCount() {
		return columnsCount;
	}

	/*
     * @see java.lang.Object#equals(java.lang.Object)
     */
	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!(object instanceof Dimension)) {
			return false;
		}
		final Dimension objectDimension = (Dimension) object;
		return rowsCount == objectDimension.getRowsCount()
				&& columnsCount == objectDimension.getColumnsCount();
	}
	
	/*
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		return "Dimensin(rowsCount=" + rowsCount + ", columnsCount=" + columnsCount + ")";
	}

}
