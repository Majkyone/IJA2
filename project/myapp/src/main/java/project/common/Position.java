package project.common;

/**
 * Represents a position in a 2D grid using row and column coordinates.
 */
public class Position {
    private final int row;
    private final int col;

    /**
     * Constructs a new Position with the specified row and column.
     *
     * @param row the row index of the position.
     * @param col the column index of the position.
     */
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }
    /**
     * Returns the row index of this position.
     *
     * @return the row index.
     */
    public int getRow(){
        return this.row;
    }

    /**
     * Returns the column index of this position.
     *
     * @return the column index.
     */
    public int getCol(){
        return this.col;
    }

    /**
     * Checks whether this position is equal to another object.
     * Two positions are equal if they have the same row and column values.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the positions are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }
}
