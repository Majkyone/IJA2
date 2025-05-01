package project.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.game.Game;
import javafx.scene.layout.Region;


public class MainWindow extends Application {
    private Game game;
    private  int tileSize = 50;
    private  int cols = 10;
    private  int rows = 10;
    private  int leftPanelWidth = 150;

    @Override
    public void start(Stage primaryStage) {
        game = Game.create(cols, rows);
        game.init();

        BorderPane mainLayout = new BorderPane();
        VBox leftPanel = createLeftPanel();
        leftPanel.setId("leftPanel");
        mainLayout.setLeft(leftPanel);

        GameBoardView gameBoardView = new GameBoardView(game);
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
            // logika pre level
        });

        Button resetButton = new Button("Reset Game");
        resetButton.setMaxWidth(Double.MAX_VALUE);
        resetButton.setMinWidth(Region.USE_PREF_SIZE);
        resetButton.setWrapText(true);
        resetButton.setId("resetButton");
        resetButton.setOnAction(event -> {
            game.init();
        });

        leftPanel.getChildren().addAll(levelButton, resetButton);
        leftPanel.setMinWidth(leftPanelWidth);
        return leftPanel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
