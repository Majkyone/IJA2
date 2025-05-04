package project.common;

/**
 * Represents a cardinal direction used for orientation in the game grid.
 *
 * The available directions are:
 * - {@code NORTH}
 * - {@code EAST}
 * - {@code SOUTH}
 * - {@code WEST}
 */
public enum Side {
    NORTH, EAST, SOUTH, WEST;
     /**
     * Returns the opposite direction of the current side.
     *
     * @return the side opposite to this one:
     *         NORTH ↔ SOUTH, EAST ↔ WEST.
     */
    public Side opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST  -> WEST;
            case SOUTH -> NORTH;
            case WEST  -> EAST;
        };
    }
}