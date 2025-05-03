package project.gui;

import javax.print.attribute.standard.Sides;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import project.common.*;
import project.game.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;



import javafx.scene.Node;


public class MainWindow extends Application {
    private Game game;
    private  int tileSize = 50;
    private  int cols = 10;
    private  int rows = 10;
    private  int leftPanelWidth = 200;
    private GamePreviewWindowView gamePreview;
    private GameBoardView gameBoardView;
    private BorderPane mainLayout;
    private Pane intro;
    private Button undoButton;
    private Button redoButton;

    @Override
    public void start(Stage primaryStage) {
        game = Game.create(cols, rows);
        mainLayout = new BorderPane();
        VBox leftPanel = createLeftPanel();
        leftPanel.setId("leftPanel");
        mainLayout.setLeft(leftPanel);


        gameBoardView = new GameBoardView(game, undoButton, redoButton);
        intro = createIntro();
        mainLayout.setCenter(intro);


        // buffer na rám + title bar
        int extraWidth = 16;
        int extraHeight = 39;
        
        
        Scene scene = new Scene(mainLayout,
        tileSize * cols + leftPanelWidth + extraWidth,
        tileSize * rows + extraHeight);
        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/Icon.png")));
        primaryStage.setScene(scene);
        primaryStage.setTitle("Electrician");
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

        Text headline = new Text("MENU");
        headline.getStyleClass().add("headline");

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
            undoButton.setVisible(true);
            redoButton.setVisible(true);
            GameLoader loader = new GameLoader("data/currentLevel/levelData.txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.init();

            gameBoardView = new GameBoardView(game, undoButton, redoButton);
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

        this.undoButton = createButton("Undo", "control");
        undoButton.setOnAction(event -> gameBoardView.getCommandManager().undo());
        redoButton = createButton("Redo", "control");
        redoButton.setOnAction(event -> gameBoardView.getCommandManager().redo());

        undoButton.setVisible(false);
        redoButton.setVisible(false);
      

        leftPanel.getChildren().addAll(headline, levelButton, levelButton2, levelButton3, levelButton4,undoButton,redoButton, loadButton, previewButton);
        leftPanel.setMinWidth(leftPanelWidth);
        return leftPanel;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private Pane createIntro() {
        VBox mainContainer = new VBox(20); // Increased vertical spacing between sections
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40, 30, 40, 30));
        
        // Title and subtitle in a VBox with centered text
        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER);
        
        Text title = new Text("Welcome to Light Puzzle!");
        title.getStyleClass().add("title");
        
        Text subtitle = new Text("Rotate the tiles to light up all the bulbs.");
        subtitle.getStyleClass().add("subtitle");
        
        textContainer.getChildren().addAll(title, subtitle);
        
        // Create nodes with their components
        Side[] wireConnectors = {Side.NORTH, Side.SOUTH, Side.EAST};
        GameNode wire = new GameNode(1, 1, NodeType.WIRE, wireConnectors);
        
        Side[] bulbConnectors = {Side.NORTH};
        GameNode bulb = new GameNode(1, 1, NodeType.BULB, bulbConnectors);
        
        Side[] sourceConnectors = {Side.EAST};
        GameNode source = new GameNode(1, 1, NodeType.SOURCE, sourceConnectors);
        source.setPowered(true);
        
        // Create views for the nodes
        GameNodeView wireNode = new GameNodeView(wire);
        GameNodeView bulbNode = new GameNodeView(bulb);
        GameNodeView sourceNode = new GameNodeView(source);
        
        // Set fixed size for all nodes
        wireNode.setPrefSize(60, 60);
        bulbNode.setPrefSize(60, 60);
        sourceNode.setPrefSize(60, 60);

        wireNode.setOnMouseClicked(event -> {
            wire.turn();
        });

        bulbNode.setOnMouseClicked(event -> {
            bulb.turn();
        });
        
        sourceNode.setOnMouseClicked(event -> {
            source.turn();
        });
        // Create containers for each node with labels
        VBox wireContainer = createNodeWithLabel(wireNode, "Wire");
        VBox bulbContainer = createNodeWithLabel(bulbNode, "Bulb");
        VBox sourceContainer = createNodeWithLabel(sourceNode, "Source");
        
        // Create horizontal layout for all nodes
        HBox nodesContainer = new HBox(20); // 20 = spacing between nodes
        nodesContainer.setAlignment(Pos.CENTER);
        nodesContainer.getChildren().addAll(wireContainer, bulbContainer, sourceContainer);
        
        // Add everything to the main container
        mainContainer.getChildren().addAll(textContainer, nodesContainer);
        
        Pane pane = new Pane();
        pane.getChildren().add(mainContainer);
        
        // Center the main container in the pane
        mainContainer.layoutXProperty().bind(pane.widthProperty().subtract(mainContainer.widthProperty()).divide(2));
        
        return pane;
    }

// Helper method to create a node with its label
    private VBox createNodeWithLabel(Node nodeView, String labelText) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);
        
        Label label = new Label(labelText);
        label.getStyleClass().add("node-label");
        
        container.getChildren().addAll(nodeView, label);
        return container;
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

            gameBoardView = new GameBoardView(game, undoButton, redoButton);
            mainLayout.setCenter(gameBoardView);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> new RandomizeGame(game, loader));
            delay.play();
    }
}
