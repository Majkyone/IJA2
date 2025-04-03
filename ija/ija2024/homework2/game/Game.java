package ija.ija2024.homework2.game;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.NodeType;
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
public class Game {
    private GameNode[][] grid;
    private int rows;
    private int cols;
    private boolean isSource = false;
    private Game(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        grid = new GameNode[rows+1][cols+1];

        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                grid[r][c] = new GameNode(r, c, NodeType.EMPTY);
            }
        }
    }

    private boolean chceskPosition(Position p){
        return this.rows >= p.getRow() && this.cols >=p.getCol() && p.getRow() >= 1 && p.getCol() >= 1;
    }

    public static Game create(int rows, int cols){
        if (rows < 1 || cols < 1) {
            throw new IllegalArgumentException();
        }else{
            return new Game(rows, cols);
        }
    }
    public int rows(){
        return this.rows;
    }
    public int cols(){
        return this.cols;
    }

    public GameNode createBulbNode(Position p, Side sides){
        if (chceskPosition(p)){
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.BULB, sides);
            return grid[p.getRow()][p.getCol()];

        }else{
            return null;
        }
    }

    public GameNode createPowerNode(Position p, Side... sides){
        if (chceskPosition(p) && !isSource && sides.length >= 1){
            isSource = true;
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.SOURCE, sides);
            return grid[p.getRow()][p.getCol()];

        }else{
            return null;
        }
    }

    public GameNode createLinkNode(Position p, Side... sides){
        if (chceskPosition(p) && sides.length >= 2){
            grid[p.getRow()][p.getCol()] = new GameNode(p.getRow(), p.getCol(), NodeType.WIRE, sides);
            return grid[p.getRow()][p.getCol()];

        }else{
            return null;
        }
    }
    public GameNode node(Position p){
        if (chceskPosition(p)) {
            return grid[p.getRow()][p.getCol()];
            
        }else{
            return null;
        }
    }
    public void init(){
        return;
    }
}
