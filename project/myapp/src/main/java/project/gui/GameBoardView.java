package project.gui;

import javafx.scene.layout.*;
import project.common.GameNode;
import project.common.NodeType;
import project.game.Game;

public class GameBoardView extends GridPane {
    private final Game game;
    private final int tileSize = 50;

    public GameBoardView(Game game) {
        this.game = game;
        this.setMinSize(tileSize * game.cols(), tileSize * game.rows());
        this.setMaxSize(tileSize * game.cols(), tileSize * game.rows());
        this.setPrefSize(tileSize * game.cols(), tileSize * game.rows());
        drawGameBoard();
    }

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
                GameNodeView nodeView = new GameNodeView(node);
                nodeView.setMinSize(tileSize, tileSize);
                nodeView.setMaxSize(tileSize, tileSize);
                if (node.getType() != NodeType.EMPTY) {
                    nodeView.setOnMouseClicked(event -> node.turn());
                }
                this.add(nodeView, c - 1, r - 1);
            }
        }
    }
}
