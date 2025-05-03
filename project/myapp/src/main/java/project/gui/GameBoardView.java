package project.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.layout.*;
import project.common.GameNode;
import project.common.NodeType;
import project.game.*;

public class GameBoardView extends GridPane {
    private final Game game;
    private final int tileSize = 50;
    private final CommandManager commandManager = new CommandManager();
    private final Button undoButton;
    private final Button redoButton;
    public GameBoardView(Game game, Button undoButton, Button redoButton) {
        this.game = game;
        this.undoButton = undoButton;
        this.redoButton = redoButton;
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
                nodeView.setId("node");
                nodeView.setMinSize(tileSize, tileSize);
                nodeView.setMaxSize(tileSize, tileSize);
                if (node.getType() != NodeType.EMPTY && node.getNumberOfSides() != 4) {
                    nodeView.setOnMouseClicked(event -> {
                        undoButton.setVisible(false);
                        redoButton.setVisible(false);
                        TurnCommand cmd = new TurnCommand(node);
                        commandManager.executeCommand(cmd);
                        saveSteps(node);
                    });
                }
                this.add(nodeView, c - 1, r - 1);
            }
        }
    }
    public CommandManager getCommandManager() {
        return commandManager;
    }
    public void saveSteps(GameNode node) {
        try {
            File file = new File("data/currentLevel/steps.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            // Zápis do súboru
            writer.write(node.toString() + "\n");
            // Ukončenie zápisu
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
