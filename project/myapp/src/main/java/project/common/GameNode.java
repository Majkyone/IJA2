package project.common;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * The GameNode class represents a node in the game grid, which could be a part of a wire, a bulb, or a power source.
 * It keeps track of its position, the sides it connects to, and whether it is powered on or off.
 * This class extends AbstractObservableField, meaning it can notify observers about its state changes.
 */
public class GameNode extends AbstractObservableField {
    private Position position;  // The position of the GameNode in the grid (row, column)
    private Side[] side;  // The sides that this node connects to (north, south, east, west)
    private NodeType type;  // The type of node (e.g., BULB, WIRE, SOURCE)
    private boolean powered = false;  // Indicates if the node is powered
    private int numberOfTurns = 0;  // The number of turns this node has undergone

    /**
     * Constructor for creating a GameNode with a specific position, type, and sides.
     * @param row The row of the node.
     * @param col The column of the node.
     * @param type The type of the node (e.g., BULB, WIRE, SOURCE).
     * @param sides The sides this node connects to.
     */
    public GameNode(int row, int col, NodeType type, Side... sides) {
        this.side = new Side[sides.length];
        this.position = new Position(row, col);
        this.type = type;
        this.side = sides;
    }

    /**
     * Constructor for creating a GameNode with a specific position and type (no sides).
     * @param row The row of the node.
     * @param col The column of the node.
     * @param type The type of the node (e.g., BULB, WIRE, SOURCE).
     */
    public GameNode(int row, int col, NodeType type) {
        this.position = new Position(row, col);
        this.type = type;
        this.side = new Side[0];
    }

    /**
     * Returns the position of the GameNode.
     * @return The position of the GameNode.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Checks if the node has a connector to the east.
     * @return true if the node connects to the east, otherwise false.
     */
    @Override
    public boolean east() {
        return containsConnector(Side.EAST);
    }

    /**
     * Checks if the node has a connector to the west.
     * @return true if the node connects to the west, otherwise false.
     */
    @Override
    public boolean west() {
        return containsConnector(Side.WEST);
    }

    /**
     * Checks if the node has a connector to the north.
     * @return true if the node connects to the north, otherwise false.
     */
    @Override
    public boolean north() {
        return containsConnector(Side.NORTH);
    }

    /**
     * Checks if the node has a connector to the south.
     * @return true if the node connects to the south, otherwise false.
     */
    @Override
    public boolean south() {
        return containsConnector(Side.SOUTH);
    }

    /**
     * Checks if the node is a bulb.
     * @return true if the node is a bulb, otherwise false.
     */
    @Override
    public boolean isBulb() {
        return this.type == NodeType.BULB;
    }

    /**
     * Checks if the node is a wire.
     * @return true if the node is a wire, otherwise false.
     */
    @Override
    public boolean isLink() {
        return this.type == NodeType.WIRE;
    }

    /**
     * Checks if the node is a power source.
     * @return true if the node is a power source, otherwise false.
     */
    @Override
    public boolean isPower() {
        return this.type == NodeType.SOURCE;
    }

    /**
     * Checks if the node contains a specific connector (side).
     * @param s The side to check for a connector (e.g., NORTH, EAST, etc.).
     * @return true if the node contains the specified connector, otherwise false.
     */
    public boolean containsConnector(Side s) {
        if (this.side == null) {
            return false;
        }
        return Arrays.asList(this.side).contains(s);
    }

    /**
     * Returns the number of sides the node has.
     * @return The number of sides.
     */
    public int getNumberOfSides() {
        return Arrays.asList(this.side).size();
    }

    /**
     * Rotates the sides of the node 90 degrees clockwise.
     * This changes the direction of all the connectors and updates the number of turns.
     */
    public void turn() {
        int i = 0;
        for (Side side : this.side) {
            int changeDirection = side.ordinal();
            this.side[i] = Side.values()[(changeDirection + 1) % Side.values().length];
            i++;
        }
        numberOfTurns++;
        List<Side> order = Arrays.asList(Side.NORTH, Side.EAST, Side.SOUTH, Side.WEST);
        Arrays.sort(this.side, Comparator.comparingInt(order::indexOf));
        notifyObservers();
    }

    /**
     * Returns whether the node is powered or not.
     * @return true if the node is powered, otherwise false.
     */
    public boolean light() {
        return this.powered;
    }

    /**
     * Returns the number of turns, considering the type of sides the node has.
     * @return The number of turns (either 0 or 1 based on the sides).
     */
    public int getNumberOfturns() {
        if (getNumberOfSides() == 2 && side[0].opposite() == side[1]) {
            return this.numberOfTurns % 2;
        } else {
            return (this.numberOfTurns + 2) % 4;
        }
    }

    /**
     * Returns the type of the node (e.g., BULB, WIRE, SOURCE).
     * @return The type of the node.
     */
    public NodeType getType() {
        return this.type;
    }

    /**
     * Sets the power state of the node.
     * @param powered true to power on the node, false to power it off.
     */
    public void setPowered(boolean powered) {
        this.powered = powered;
        // notifyObservers();  // Uncomment if observers need to be notified when the power state changes
    }

    /**
     * Returns the sides that the node connects to.
     * @return The sides (e.g., north, south, east, west).
     */
    public Side[] getSides() {
        return this.side;
    }

    /**
     * Returns whether the node is powered.
     * @return true if the node is powered, otherwise false.
     */
    public boolean isPowered() {
        return this.powered;
    }

    /**
     * Determines the shape of the node based on the connected sides.
     * The method checks which sides (NORTH, EAST, SOUTH, WEST) are connected and returns 
     * the corresponding shape from the NodeShape enumeration.
     * 
     * The shapes are defined as follows:
     * <ul>
     *     <li>X: All sides connected (NORTH, EAST, SOUTH, WEST)</li>
     *     <li>NE: North and East connected</li>
     *     <li>WE: West and East connected</li>
     *     <li>I: North and South connected</li>
     *     <li>NW: North and West connected</li>
     *     <li>SE: South and East connected</li>
     *     <li>SW: South and West connected</li>
     * </ul>
     * 
     * @return the shape of the node based on the connected sides.
     * @throws IllegalStateException if no matching shape is found (unexpected value).
     */
    public NodeShape getShape(){
        Side sides [];
        sides = getSides();
        int connectedSides = 0;
        for (Side side : sides) {
            switch (side) {
                case NORTH:
                    connectedSides |= 1;  // Bit 0 represents NORTH
                    break;
                case EAST:
                    connectedSides |= 2;  // Bit 1 represents EAST
                    break;
                case SOUTH:
                    connectedSides |= 4;  // Bit 2 represents SOUTH
                    break;
                case WEST:
                    connectedSides |= 8;  // Bit 3 represents WEST
                    break;
                default:
                    break;
            }
        }
        // Return the appropriate NodeShape based on the connected sides
        switch (connectedSides) {
            case 15:  // All sides connected (NORTH, EAST, SOUTH, WEST)
                return NodeShape.X;
            case 3:   // North and East connected
                return NodeShape.NE;
            case 9:   // West and East connected
                return NodeShape.WE;
            case 6:   // North and South connected
                return NodeShape.I;
            case 12:  // North and West connected
                return NodeShape.NW;
            case 10:  // North and East connected
                return NodeShape.NE;
            case 5:   // South and East connected
                return NodeShape.SE;
            case 7:   // South and West connected
                return NodeShape.SW;
            default:
                // If no matching shape found, you can return a default or throw an exception
                return NodeShape.UNKNOWN;
        }
    }
    

    /**
     * Returns a string representation of the GameNode, including its type, position, and connected sides.
     * @return A string representation of the GameNode.
     */
    public String toString() {
        return String.format("{%s[%d@%d]%s}", type.swichSideToString(), position.getRow(), position.getCol(), Arrays.toString(side).replace(" ", ""));
    }
}
