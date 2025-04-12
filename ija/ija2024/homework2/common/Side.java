package ija.ija2024.homework2.common;

public enum Side {
    NORTH, EAST, SOUTH, WEST;
    public Side opposite() {
        return switch (this) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST  -> WEST;
            case WEST  -> EAST;
        };
    }
}
