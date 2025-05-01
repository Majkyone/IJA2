package project.gui;

import javafx.scene.layout.GridPane;
import project.common.GameNode;
import project.common.NodeType;
import project.game.*;

/*
 * Single game node view
 */
public class GameBoardView extends GridPane{
    private final Game game;
    public GameBoardView(Game game) {
        this.game = game;
        drawGameBoard();
    }
    private void drawGameBoard() {
        GameNode[][] grid = game.getGame();

        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                GameNode node = grid[r][c];
                GameNodeView nodeView = new GameNodeView(node);
                // click event for each node
                if (node.getType() != NodeType.EMPTY) {
                    nodeView.setOnMouseClicked(event -> {
                        // rotate node
                        node.turn();
                    });
                }
                this.add(nodeView, c - 1, r - 1); 
            }
        }
    }
}
