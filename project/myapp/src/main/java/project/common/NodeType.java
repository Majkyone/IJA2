package project.common;

/**
 * Represents the type of a node in the game grid.
 *
 * The node can be one of the following types:
 * - {@code EMPTY}: An empty tile with no function.
 * - {@code WIRE}: A conductor tile used to connect electricity.
 * - {@code SOURCE}: A power source tile that provides energy.
 * - {@code BULB}: A light bulb tile that lights up when powered.
 */
public enum NodeType {
    EMPTY, WIRE, SOURCE, BULB;
    /**
     * Converts the node type to its corresponding single-letter string representation.
     *
     * @return a single-letter string representing the node type:
     *         'E' for EMPTY, 'L' for WIRE, 'P' for SOURCE, and 'B' for BULB.
     */
    public String swichSideToString() {
        return switch (this) {
            case EMPTY -> "E";
            case WIRE -> "L";
            case SOURCE  -> "P";
            case BULB  -> "B";
        };
    }
}

