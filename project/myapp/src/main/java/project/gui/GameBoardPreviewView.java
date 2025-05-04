package project.gui;

import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import project.common.GameNode;

import project.game.Game;

/**
 * The {@link GameBoardPreviewView} class represents a graphical preview of the game board.
 * <p>
 * It extends {@link GridPane} and provides a visual representation of the game grid,
 * displaying each {@link GameNode} on the board in a grid layout. This class is used 
 * to generate a preview of the game board's current state, allowing users to view 
 * the layout of the game at any given point.
 */
public class GameBoardPreviewView extends GridPane{
    private final Game game;
    private final int tileSize = 50;

    /**
     * Creates a new {@link GameBoardPreviewView} for the given game.
     * 
     * @param game The {@link Game} whose board will be displayed.
     */
    public GameBoardPreviewView(Game game) {
        this.game = game;
        this.setMinSize(tileSize * game.cols(), tileSize * game.rows());
        this.setMaxSize(tileSize * game.cols(), tileSize * game.rows());
        this.setPrefSize(tileSize * game.cols(), tileSize * game.rows());
        drawGameBoard();
    }

    /**
     * Draws the game board by setting up the grid layout and adding the node previews.
     * It initializes the grid by adjusting the column and row constraints based on 
     * the number of rows and columns in the game. Each {@link GameNode} is represented 
     * by a {@link GameNodePreviewView} added to the grid.
     */
    private void drawGameBoard() {
        GameNode[][] grid = game.getGame();

        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        this.getChildren().clear();

        for (int c = 0; c < game.cols(); c++) {
            ColumnConstraints cc = new ColumnConstraints(tileSize);
            this.getColumnConstraints().add(cc);
        }

        for (int r = 0; r < game.rows(); r++) {
            RowConstraints rc = new RowConstraints(tileSize);
            this.getRowConstraints().add(rc);
        }

        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                GameNode node = grid[r][c];
                GameNodePreviewView nodeView = new GameNodePreviewView(node);
                nodeView.setId("node");
                nodeView.setMinSize(tileSize, tileSize);
                nodeView.setMaxSize(tileSize, tileSize);
                this.add(nodeView, c - 1, r - 1);
            }
        }
    }
}
