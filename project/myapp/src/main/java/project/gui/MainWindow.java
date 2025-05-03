package project.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import project.common.Position;
import project.common.Side;
import project.game.Game;
import javafx.scene.layout.Region;


public class MainWindow extends Application {
    private Game game;
    private  int tileSize = 50;
    private  int cols = 10;
    private  int rows = 10;
    private  int leftPanelWidth = 150;
    private GamePreviewWindowView gamePreview;


    @Override
    public void start(Stage primaryStage) {
        game = Game.create(cols, rows);
        Position position = new Position(2, 2);
        Side [] sides = {Side.SOUTH, Side.EAST};
        game.createLinkNode(position, sides);

        Position position7 = new Position(2, 3);
        Side [] sides7 = {Side.SOUTH, Side.WEST};
        game.createLinkNode(position7, sides7);

        Position position2 = new Position(3, 2);
        Side [] sides2 = {Side.NORTH, Side.SOUTH};
        game.createLinkNode(position2, sides2);
        game.init();

        Position position4 = new Position(3, 3);
        Side [] sides4 = {Side.NORTH, Side.WEST};
        game.createLinkNode(position4, sides4);
        game.init();

        Position position5 = new Position(3, 4);
        Side [] sides5 = {Side.WEST, Side.EAST};
        game.createLinkNode(position5, sides5);
        game.init();

        Position position6 = new Position(3, 5);
        Side [] sides6 = {Side.NORTH, Side.EAST};
        game.createLinkNode(position6, sides6);
        game.init();

        Position position3 = new Position(4, 2);
        Side [] sides3 = {Side.NORTH, Side.SOUTH, Side.EAST, Side.WEST};
        game.createLinkNode(position3, sides3);
        game.init();

        Position position10 = new Position(3, 6);
        Side [] sides10 = {Side.SOUTH, Side.EAST, Side.WEST};
        game.createLinkNode(position10, sides10);

        Position position8 = new Position(4, 3);
        Side  sides8 = Side.NORTH;
        game.createBulbNode(position8, sides8);
        game.init();

        Position position9 = new Position(4, 4);
        Side [] sides9 = {Side.EAST, Side.NORTH};
        game.createPowerNode(position9, sides9);
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
            // logika pre level
        });

        Button levelButton2 = createButton("Level 2", "levelButton");
        levelButton2.setOnAction(event -> {
            // logika pre level
        });

        Button levelButton3 = createButton("Level 3", "levelButton");
        levelButton3.setOnAction(event -> {
            // logika pre level
        });

        Button levelButton4 = createButton("Level 4", "levelButton");
        levelButton4.setOnAction(event -> {
            // logika pre level
        });

        Button importButton = createButton("Import Game", "importButton");
        importButton.setOnAction(event -> {
            // logika pre level
        });

        Button exportButton = createButton("Export Game", "exportButton");
        exportButton.setOnAction(event -> {
            // logika pre level
        });

        Button previewButton = createButton("Preview Game", "previewButton");
        previewButton.setOnAction(event -> {
            System.out.println("PREVIEW");
            gamePreview = new GamePreviewWindowView(game);           
            gamePreview.show();
        });

      

        leftPanel.getChildren().addAll(levelButton, levelButton2, levelButton3, levelButton4, importButton, exportButton, previewButton);
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
}
