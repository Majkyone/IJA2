package project.gui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.game.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class MainWindow extends Application {
    private Game game;
    private  int tileSize = 50;
    private  int cols = 10;
    private  int rows = 10;
    private  int leftPanelWidth = 150;
    private GamePreviewWindowView gamePreview;
    private GameBoardView gameBoardView;
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) {
        game = Game.create(cols, rows);
        mainLayout = new BorderPane();
        VBox leftPanel = createLeftPanel();
        leftPanel.setId("leftPanel");
        mainLayout.setLeft(leftPanel);

        gameBoardView = new GameBoardView(game);
        mainLayout.setCenter(gameBoardView);

        // buffer na rám + title bar
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
        primaryStage.setOnCloseRequest(event -> {
            if (gamePreview != null && gamePreview.isShowing()) {
                gamePreview.close();
            }
        });
    }

    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);

        Button levelButton = createButton("Level 1", "levelButton");
        levelButton.setOnAction(event -> {
            LoadGame(1);
        });

        Button levelButton2 = createButton("Level 2", "levelButton");
        levelButton2.setOnAction(event -> {
            LoadGame(2);
        });

        Button levelButton3 = createButton("Level 3", "levelButton");
        levelButton3.setOnAction(event -> {
            LoadGame(3);
        });

        Button levelButton4 = createButton("Level 4", "levelButton");
        levelButton4.setOnAction(event -> {
            LoadGame(4);
        });

        Button loadButton = createButton("Load Last Game", "loadButton");
        loadButton.setOnAction(event -> {
            GameLoader loader = new GameLoader("data/currentLevel/levelData.txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game);
            mainLayout.setCenter(gameBoardView);
            gameBoardView.getCommandManager().loadCommandsFromFile("data/currentLevel/steps.txt", game);
        });

        Button previewButton = createButton("Preview Game", "previewButton");
        previewButton.setOnAction(event -> {
            System.out.println("PREVIEW");
            if (gamePreview != null && gamePreview.isShowing()) {
                gamePreview.toFront(); // Zobrazí už otvorené okno na popredí
                return; // Ukončíme metódu, aby sa nezobrazovalo nové okno
            }
            gamePreview = new GamePreviewWindowView(game);           
            gamePreview.show();
        });

        Button undoButton = createButton("Undo", "undoButton");
        undoButton.setOnAction(event -> gameBoardView.getCommandManager().undo());
        Button redoButton = createButton("Redo", "redoButton");
        redoButton.setOnAction(event -> gameBoardView.getCommandManager().redo());

      

        leftPanel.getChildren().addAll(levelButton, levelButton2, levelButton3, levelButton4,undoButton,redoButton, loadButton, previewButton);
        leftPanel.setMinWidth(leftPanelWidth);
        return leftPanel;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Button createButton(String label, String group){
        Button button = new Button(label);
        button.setId(group);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinWidth(Region.USE_PREF_SIZE);
        button.setWrapText(true);
        return button;
    }

    private void LoadGame(int level){
        GameLoader loader = new GameLoader("data/levels/level" + level + ".txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game);
            mainLayout.setCenter(gameBoardView);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> new RandomizeGame(game, loader));
            delay.play();
    }
}
