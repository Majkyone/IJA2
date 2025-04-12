package ija.ija2024.homework2.common;

public enum NodeType {
    EMPTY, WIRE, SOURCE, BULB;
    public String swichSideToString() {
        return switch (this) {
            case EMPTY -> "E";
            case WIRE -> "L";
            case SOURCE  -> "P";
            case BULB  -> "B";
        };
    }
}
