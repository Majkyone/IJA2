package ija.ija2024.homework2.game;

import java.util.Arrays;
import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.NodeType;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import ija.ija2024.tool.common.*;

public class Game implements ToolEnvironment, Observable.Observer{
    private GameNode[][] grid;
    private int rows;
    private int cols;
    private boolean isSource = false;

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

    private boolean chceckPosition(Position p) {
        return this.rows >= p.getRow() && this.cols >= p.getCol() && p.getRow() >= 1 && p.getCol() >= 1;
    }

    public static Game create(int rows, int cols) {
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException();
        } else {
            return new Game(rows, cols);
        }
    }

    @Override
    public int rows(){
        return this.rows;
    }

    @Override
    public int cols(){
        return this.cols;
    }
    @Override
    public ToolField fieldAt(int row, int col) {
        if (row < 1 || col < 1 || row > rows || col > cols) {
            throw new IllegalArgumentException();
        }
        return grid[row][col];
    }

    public GameNode createBulbNode(Position p, Side sides) {
        if (chceckPosition(p)) {
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.BULB, sides);
            grid[p.getRow()][p.getCol()].addObserver(this);
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
       
    }

    public GameNode createPowerNode(Position p, Side... sides) {
        if (chceckPosition(p) && !isSource && sides.length >= 1) {
            isSource = true;
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.SOURCE, sides);
            
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

    public GameNode createLinkNode(Position p, Side... sides) {
        if (chceckPosition(p) && sides.length >= 2) {
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.WIRE, sides);
            grid[p.getRow()][p.getCol()].addObserver(this);
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

    public GameNode node(Position p) {
        if (chceckPosition(p)) {
            return grid[p.getRow()][p.getCol()];

        } else {
            return null;
        }
    }

    public void init() {
        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                GameNode node = grid[r][c];
                if (node.getType() == NodeType.SOURCE) {
                    node.setPowered(true);
                    poweredNodes(node);
                    return;
                }
            }
        }
    }

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

    // private void addObserver(){
    //     for (int r = 1; r <= rows; r++) {
    //         for (int c = 1; c <= cols; c++) {
    //             grid[r][c].addObserver(this);
    //         }
    //     }
    // }

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

    public GameNode[][] getGame(){
        return this.grid;
    }
    @Override
    public void update(Observable o) {
        init();
    }

}
