package project.common;

public enum Side {
    NORTH, EAST, SOUTH, WEST;
    public Side opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case EAST  -> WEST;
            case SOUTH -> NORTH;
            case WEST  -> EAST;
        };
    }
}