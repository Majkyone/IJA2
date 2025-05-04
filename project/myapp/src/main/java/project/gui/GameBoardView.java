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


/**
 * The {@link GameBoardView} class represents the graphical view of the game board.
 * <p>
 * It extends {@link GridPane} and displays the game grid, with interactive {@link GameNodeView}
 * components for each node in the game. This class allows users to interact with the nodes on 
 * the game board, such as rotating nodes, and tracks user actions such as turns and steps.
 */
public class GameBoardView extends GridPane {
    private final Game game;
    private final int tileSize = 50;
    private final CommandManager commandManager = new CommandManager();
    private final Button undoButton;
    private final Button redoButton;
    private int noOfTurns = 0;

    /**
     * Creates a new {@link GameBoardView} for the given game with undo and redo buttons.
     * 
     * @param game The {@link Game} whose board will be displayed.
     * @param undoButton The {@link Button} for undoing a previous action.
     * @param redoButton The {@link Button} for redoing a previously undone action.
     */
    public GameBoardView(Game game, Button undoButton, Button redoButton) {
        this.game = game;
        this.undoButton = undoButton;
        this.redoButton = redoButton;
        this.setMinSize(tileSize * game.cols(), tileSize * game.rows());
        this.setMaxSize(tileSize * game.cols(), tileSize * game.rows());
        this.setPrefSize(tileSize * game.cols(), tileSize * game.rows());
        drawGameBoard();
    }

    /**
     * Draws the game board by setting up the grid layout and adding interactive nodes.
     * It initializes the grid by adjusting the column and row constraints based on 
     * the number of rows and columns in the game. Each {@link GameNode} is represented 
     * by a {@link GameNodeView} added to the grid, and actions are set for interactive 
     * nodes that are not of type {@link NodeType#EMPTY}.
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
                        this.noOfTurns++;
                    });
                }
                this.add(nodeView, c - 1, r - 1);
            }
        }
    }
    
    /**
     * Gets the {@link CommandManager} for managing commands.
     * 
     * @return The {@link CommandManager} instance used for handling commands.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

     /**
     * Returns the number of turns taken so far.
     * 
     * @return The number of turns taken by the player.
     */
    public int getTurns(){
        return this.noOfTurns;
    }

    /**
     * Saves the current step to a file for future reference.
     * 
     * @param node The {@link GameNode} whose state is being saved.
     */
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
