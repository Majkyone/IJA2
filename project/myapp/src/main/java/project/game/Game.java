package project.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.common.*;


/**
 * Represents the game grid and manages game logic, including node creation,
 * initialization, and win condition checking.
 */
public class Game implements ToolEnvironment, Observer{
    private GameNode[][] grid;
    private int rows;
    private int cols;
    private boolean isSource = false;
    private boolean win = false;
    private List<GameWinListener> winListeners = new ArrayList<>();

    /**
     * Private constructor to initialize a new game with the given number of rows and columns.
     *
     * @param rows the number of rows in the game grid.
     * @param cols the number of columns in the game grid.
     */
    private Game(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new GameNode[rows + 1][cols + 1];

        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                grid[r][c] = new GameNode(r, c, NodeType.EMPTY);
            }
        }
    }

    /**
     * Checks if the position is valid within the game grid.
     *
     * @param p the position to check.
     * @return true if the position is within the grid bounds, false otherwise.
     */
    private boolean chceckPosition(Position p) {
        return this.rows >= p.getRow() && this.cols >= p.getCol() && p.getRow() >= 1 && p.getCol() >= 1;
    }

    /**
     * Creates and returns a new game instance with the specified rows and columns.
     *
     * @param rows the number of rows in the grid.
     * @param cols the number of columns in the grid.
     * @return a new game instance.
     * @throws IllegalArgumentException if rows or columns are less than 1.
     */
    public static Game create(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException();
        } else {
            return new Game(rows, cols);
        }
    }

    /**
     * Returns the number of rows in the game grid.
     *
     * @return the number of rows.
     */
    @Override
    public int rows() {
        return this.rows;
    }

    /**
     * Returns the number of columns in the game grid.
     *
     * @return the number of columns.
     */
    @Override
    public int cols() {
        return this.cols;
    }

    /**
     * Retrieves the field at the specified row and column.
     *
     * @param row the row index.
     * @param col the column index.
     * @return the game field at the specified position.
     * @throws IllegalArgumentException if the row or column is out of bounds.
     */
    @Override
    public ToolField fieldAt(int row, int col) {
        if (row < 1 || col < 1 || row > rows || col > cols) {
            throw new IllegalArgumentException();
        }
        return grid[row][col];
    }

     /**
     * Creates a bulb node at the specified position with the given sides.
     *
     * @param p the position of the node.
     * @param sides the sides of the node.
     * @return the created game node, or null if the position is invalid.
     */
    public GameNode createBulbNode(Position p, Side sides) {
        if (chceckPosition(p)) {
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.BULB, sides);
            grid[p.getRow()][p.getCol()].addObserver(this);
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }

    }

    /**
     * Creates a power source node at the specified position with the given sides.
     *
     * @param p the position of the node.
     * @param sides the sides of the node.
     * @return the created game node, or null if the position is invalid or the source already exists.
     */
    public GameNode createPowerNode(Position p, Side... sides) {
        if (chceckPosition(p) && !isSource && sides.length >= 1) {
            isSource = true;
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.SOURCE, sides);
            grid[p.getRow()][p.getCol()].addObserver(this);
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

     /**
     * Creates a wire node at the specified position with the given sides.
     *
     * @param p the position of the node.
     * @param sides the sides of the node.
     * @return the created game node, or null if the position is invalid.
     */
    public GameNode createLinkNode(Position p, Side... sides) {
        if (chceckPosition(p) && sides.length >= 2) {
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.WIRE, sides);
            grid[p.getRow()][p.getCol()].addObserver(this);
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

    /**
     * Retrieves the game node at the specified position.
     *
     * @param p the position of the node.
     * @return the game node at the specified position, or null if the position is invalid.
     */
    public GameNode node(Position p) {
        if (chceckPosition(p)) {
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

    /**
     * Initializes the game grid by resetting the power of all nodes and powering the source node.
     */
    public void init() {
       // System.out.println("init game robkam");
        boolean [] [] originalPower = new boolean[rows + 1][cols + 1];
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                originalPower[r][c] = node.isPowered();
            }
        }
        resetAllNodesPower();
        
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                if (node.getType() == NodeType.SOURCE) {
                    node.setPowered(true);
                    
                    poweredNodes(node);
                    break;
                }
            }
        }

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                if (originalPower[r][c] != node.isPowered()) {
                    grid[r][c].notifyObservers();
                } 
            }
        }
    }

    /**
     * Recursively powers nodes connected to the given node.
     *
     * @param node the node to start powering from.
     */
    private void poweredNodes(GameNode node) {
        for (Side side : node.getSides()) {
            Position neighborPos = getNeighborPosition(node.getPosition(), side);

            if (!chceckPosition(neighborPos))
                continue;

            GameNode neighbor = node(neighborPos);
            if (neighbor == null || neighbor.getType() == NodeType.EMPTY || neighbor.isPowered())
                continue;

            if (Arrays.asList(neighbor.getSides()).contains(side.opposite())) {
                neighbor.setPowered(true);
                poweredNodes(neighbor);
            }
        }
    }

    /**
     * Resets the power state of all nodes on the grid to false.
     */
    private void resetAllNodesPower() {
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];

                node.setPowered(false);
            }
        }
    }

    /**
     * Returns the position of a neighboring node based on the given direction.
     *
     * @param pos the position of the current node.
     * @param side the direction (side) of the neighboring node.
     * @return the position of the neighboring node.
     */
    private Position getNeighborPosition(Position pos, Side side) {
        int r = pos.getRow();
        int c = pos.getCol();
        return switch (side) {
            case NORTH -> new Position(r - 1, c);
            case SOUTH -> new Position(r + 1, c);
            case EAST -> new Position(r, c + 1);
            case WEST -> new Position(r, c - 1);
        };
    }

    /**
     * Returns the current game grid.
     * The grid is represented as a 2D array of {@link GameNode} objects, where each node
     * contains information about its type (e.g., bulb, wire, source) and its powered state.
     *
     * @return The 2D array representing the game grid.
     */
    public GameNode[][] getGame() {
        return this.grid;
    }
    
    /**
     * Checks if any bulb in the game is powered on.
     * This method iterates through the game grid and checks if any {@link GameNode}
     * of type {@link NodeType#BULB} is powered.
     *
     * @return {@code true} if at least one bulb is powered on, {@code false} otherwise.
     */
    public boolean someBulbsAreOn() {

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                if (node.getType() == NodeType.BULB && node.isPowered()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This method is called when an {@link Observable} object (in this case, a {@link GameNode}) notifies its observers.
     * It checks if the updated node is of type {@link NodeType#WIRE}, {@link NodeType#BULB}, or {@link NodeType#SOURCE}.
     * If the node's sides are not fully connected (i.e., not equal to 4 sides), the game state is reinitialized.
     * It also checks if the win state has changed and notifies the win listeners if necessary.
     *
     * @param o The observable object that triggered the update. This will typically be a {@link GameNode}.
     */
    @Override
    public void update(Observable o) {
        GameNode node = (GameNode) o;
        if(node.getType() == NodeType.WIRE || node.getType() == NodeType.BULB || node.getType() == NodeType.SOURCE){
            if (node.getSides().length != 4) {
                
                init();
                boolean oldWin = this.win;
                this.win = isWin();
                
                if (oldWin != this.win) {
                    notifyWinListeners();
                }
            }
        }
    }

    /**
     * Returns the current win state of the game.
     * 
     * @return {@code true} if the game is won, {@code false} otherwise.
     */
    public boolean getWin(){
        return this.win;
    }

    /**
     * Sets the win state of the game.
     * 
     * @param win {@code true} if the game is won, {@code false} otherwise.
     */
    public void setWin(Boolean win){
        this.win  = win;
    }

    /**
     * Checks if the game is won by verifying if all non-empty nodes are powered.
     * 
     * @return {@code true} if all non-empty nodes are powered, indicating the game is won;
     *         {@code false} if at least one non-empty node is not powered.
     */
    private boolean isWin() {
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                if (node.getType() != NodeType.EMPTY && !node.isPowered()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Adds a listener to be notified when the game win state changes.
     * 
     * @param listener The {@link GameWinListener} to be added to the listener list.
     */
    public void addGameWinListener(GameWinListener listener) {
        winListeners.add(listener);
    }
    
    /**
     * Removes a listener that was previously added to be notified when the game win state changes.
     * 
     * @param listener The {@link GameWinListener} to be removed from the listener list.
     */
    public void removeGameWinListener(GameWinListener listener) {
        winListeners.remove(listener);
    }
    
    /**
     * Notifies all registered {@link GameWinListener} instances about the current game win state.
     * 
     * This method is called to inform all listeners about a change in the win state of the game.
     * Each listener's {@link GameWinListener#onGameWin(boolean)} method is invoked with the current win state.
     */
    private void notifyWinListeners() {
        for (GameWinListener listener : winListeners) {
            listener.onGameWin(this.win);
        }
    }

}
