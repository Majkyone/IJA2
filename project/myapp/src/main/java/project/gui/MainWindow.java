package project.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.common.NodeType;
import project.common.Position;
import project.game.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainWindow extends Application {
    private Game game;
    private int tileSize = 50;
    private int cols = 10;
    private int rows = 10;
    private int leftPanelWidth = 150;
    private BorderPane mainLayout;
    private GameBoardView gameBoardView;
    private Stage previewStage = null;

    @Override
    public void start(Stage primaryStage) {
        mainLayout = new BorderPane();
        VBox leftPanel = createLeftPanel(); // teraz môže pristupovať k hlavným atribútom
        leftPanel.setId("leftPanel");
        mainLayout.setLeft(leftPanel);

        // Inicializuj prázdnu hru 10x10
        game = Game.create(cols, rows);
        game.init();
        gameBoardView = new GameBoardView(game);
        mainLayout.setCenter(gameBoardView);

        int extraWidth = 16;
        int extraHeight = 39;
        Scene scene = new Scene(mainLayout,
                tileSize * cols + leftPanelWidth + extraWidth,
                tileSize * rows + extraHeight);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);

        Button levelButton = new Button("Level 1");
        levelButton.setId("levelButton");
        levelButton.setMaxWidth(Double.MAX_VALUE);
        levelButton.setMinWidth(Region.USE_PREF_SIZE);
        levelButton.setWrapText(true);
        levelButton.setOnAction(event -> {
            int level = 1;
            GameLoader loader = new GameLoader("data/levels/level" + level + ".txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game);
            mainLayout.setCenter(gameBoardView);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> new RandomizeGame(game, loader));
            delay.play();
        });

        Button resetButton = new Button("Reset Game");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setMinWidth(Region.USE_PREF_SIZE);
        resetButton.setWrapText(true);
        resetButton.setId("resetButton");
        resetButton.setOnAction(event -> {
            GameLoader loader = new GameLoader("data/levels/default.txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game);
            mainLayout.setCenter(gameBoardView);
        });

        Button viewButton = new Button("View");
        viewButton.setMaxWidth(Double.MAX_VALUE);
        viewButton.setMinWidth(Region.USE_PREF_SIZE);
        viewButton.setWrapText(true);
        viewButton.setId("viewButton");
        viewButton.setOnAction(event -> {
            if (previewStage != null && previewStage.isShowing()) {
                previewStage.toFront(); // Zobrazí už otvorené okno na popredí
                return; // Ukončíme metódu, aby sa nezobrazovalo nové okno
            }
            previewStage = new Stage();
            previewStage.setTitle("Náhled na hru");

            // Vytvor komponenty pre nové okno (napr. nový GameBoardView)
            // prípadne skopíruj stav z `game` do `previewGame` ak chceš aktuálny stav

            GameBoardView previewBoard = new GameBoardView(game);
            StackPane root = new StackPane();
            root.getChildren().add(previewBoard);
            for (int r = 1; r <= game.rows(); r++) {
                for (int c = 1; c <= game.cols(); c++) {
                    Label numberLabel;
                    if (game.node(new Position(r, c)).getType() == NodeType.EMPTY
                            || game.node(new Position(r, c)).getNumberOfSides() == 4) {
                        numberLabel = new Label(String.valueOf(0));
                    } else {
                        numberLabel = new Label(String.valueOf(game.node(new Position(r, c)).getNumberOfTurns()));

                    }
                    numberLabel.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

                    // Umiestnite číslo do správnej pozície podľa row a col
                    StackPane.setAlignment(numberLabel, Pos.TOP_LEFT);
                    numberLabel.setTranslateX((c - 1) * 50);
                    numberLabel.setTranslateY((r - 1) * 50);

                    // Pridajte číslo do root StackPane
                    root.getChildren().add(numberLabel);
                }
            }

            Scene previewScene = new Scene(root);
            previewScene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());

            previewStage.setScene(previewScene);
            previewStage.show();
        });
        Button undoButton = new Button("UNDO");
        undoButton.setId("undoButton");
        undoButton.setMaxWidth(Double.MAX_VALUE);
        undoButton.setMinWidth(Region.USE_PREF_SIZE);
        undoButton.setWrapText(true);
        undoButton.setOnAction(event -> gameBoardView.getCommandManager().undo());
        Button redoButton = new Button("REDO");
        redoButton.setId("redoButton");
        redoButton.setMaxWidth(Double.MAX_VALUE);
        redoButton.setMinWidth(Region.USE_PREF_SIZE);
        redoButton.setWrapText(true);
        redoButton.setOnAction(event -> gameBoardView.getCommandManager().redo());
        Button loadButton = new Button("Load");
        loadButton.setId("undoButton");
        loadButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setMinWidth(Region.USE_PREF_SIZE);
        loadButton.setWrapText(true);
        loadButton.setOnAction(event -> {
            GameLoader loader = new GameLoader("data/currentLevel/levelData.txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game);
            mainLayout.setCenter(gameBoardView);
            gameBoardView.getCommandManager().loadCommandsFromFile("data/currentLevel/steps.txt", game);
        });
        leftPanel.getChildren().addAll(levelButton, resetButton, viewButton, undoButton, redoButton, loadButton);
        leftPanel.setMinWidth(leftPanelWidth);
        return leftPanel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
