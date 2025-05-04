package project.gui;

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


/**
 * The {@link MainWindow} class is the main window of the Electrician game application.
 * It extends {@link Application} and is responsible for initializing the user interface and handling user interactions.
 * <p>
 * This class sets up the game environment, including creating the game grid, loading game levels, and displaying
 * the current game state. It also handles UI components such as buttons for loading a game, starting new levels, 
 * and undoing or redoing actions.
 * </p>
 * <p>
 * The class uses a {@link BorderPane} layout to organize the game interface. The left panel contains a menu for
 * selecting levels, undo/redo actions, and previewing the game. The center is dynamically updated to display the
 * game board or an introduction screen.
 * </p>
 * <p>
 * The {@link MainWindow} class integrates with the game logic, provided by the {@link Game} class, to control the
 * flow of the game. It manages game progress, updates the view accordingly, and reacts to user actions such as 
 * clicking on game tiles or selecting different levels.
 * </p>
 * 
 * @see Game
 * @see GameBoardView
 * @see GamePreviewWindowView
 */
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

    /**
     * The entry point for the Electrician game application. Initializes the game and the primary stage.
     * Sets up the user interface, including the game board and menu, and handles stage setup.
     * 
     * @param primaryStage The primary stage for the game application.
     */
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

    /**
     * Creates the left panel containing the menu options such as level selection, undo, redo, and load game buttons.
     * The left panel is added to the main layout of the game window.
     * 
     * @return A {@link VBox} representing the left panel.
     */
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox();
        leftPanel.setSpacing(10);

        Text headline = new Text("MENU");
        headline.getStyleClass().add("headline");

        Button levelButton = createButton("Level 1", "levelButton");
        levelButton.setOnAction(event -> {
            undoButton.setVisible(false);
            redoButton.setVisible(false);
            LoadGame(1);
        });

        Button levelButton2 = createButton("Level 2", "levelButton");
        levelButton2.setOnAction(event -> {
            undoButton.setVisible(false);
            redoButton.setVisible(false);
            LoadGame(2);
        });

        Button levelButton3 = createButton("Level 3", "levelButton");
        levelButton3.setOnAction(event -> {
            undoButton.setVisible(false);
            redoButton.setVisible(false);
            LoadGame(3);
        });

        Button levelButton4 = createButton("Level 4", "levelButton");
        levelButton4.setOnAction(event -> {
            undoButton.setVisible(false);
            redoButton.setVisible(false);
            LoadGame(4);
        });

        Button loadButton = createButton("Load Last Game", "loadButton");
        loadButton.setOnAction(event -> {
            this.game.setWin(false);
            undoButton.setVisible(true);
            redoButton.setVisible(true);
            GameLoader loader = new GameLoader("data/currentLevel/levelData.txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            //lambda implmentation of onGameWin(boolean isWin)
            game.addGameWinListener(isWin -> {
                if (isWin) {
                    int currentLevel = 1; 
                    // after load next level ist first
                    Pane winPane = createWinMessage(currentLevel);
                    mainLayout.setCenter(winPane);
                }
            });
            game.init();

            gameBoardView = new GameBoardView(game, undoButton, redoButton);
            mainLayout.setCenter(gameBoardView);
            gameBoardView.getCommandManager().loadCommandsFromFile("data/currentLevel/steps.txt", game);
        });

        Button previewButton = createButton("Preview Game", "previewButton");
        previewButton.setOnAction(event -> {
            System.out.println("PREVIEW");
            if (gamePreview != null && gamePreview.isShowing()) {
                gamePreview.toFront(); 
                return; 
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

    /**
     * Creates an introductory screen that gives the player a brief description of the game and shows a preview
     * of the game elements (such as a wire, bulb, and source).
     * 
     * @return A {@link Pane} containing the introductory screen.
     */
    private Pane createIntro() {
        VBox mainContainer = new VBox(20); 
        mainContainer.setAlignment(Pos.CENTER);
        mainContainer.setPadding(new Insets(40, 30, 40, 30));
        
        VBox textContainer = new VBox(10);
        textContainer.setAlignment(Pos.CENTER);
        
        Text title = new Text("Welcome to Light Puzzle!");
        title.getStyleClass().add("title");
        
        Text subtitle = new Text("Rotate the tiles to light up all the bulbs.");
        subtitle.getStyleClass().add("subtitle");
        
        textContainer.getChildren().addAll(title, subtitle);
        
        Side[] wireConnectors = {Side.NORTH, Side.SOUTH, Side.EAST};
        GameNode wire = new GameNode(1, 1, NodeType.WIRE, wireConnectors);
        
        Side[] bulbConnectors = {Side.NORTH};
        GameNode bulb = new GameNode(1, 1, NodeType.BULB, bulbConnectors);
        
        Side[] sourceConnectors = {Side.EAST};
        GameNode source = new GameNode(1, 1, NodeType.SOURCE, sourceConnectors);
        source.setPowered(true);
        
        GameNodeView wireNode = new GameNodeView(wire);
        GameNodeView bulbNode = new GameNodeView(bulb);
        GameNodeView sourceNode = new GameNodeView(source);
        
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
        
        VBox wireContainer = createNodeWithLabel(wireNode, "Wire");
        VBox bulbContainer = createNodeWithLabel(bulbNode, "Bulb");
        VBox sourceContainer = createNodeWithLabel(sourceNode, "Source");
        
        
        HBox nodesContainer = new HBox(20); 
        nodesContainer.setAlignment(Pos.CENTER);
        nodesContainer.getChildren().addAll(wireContainer, bulbContainer, sourceContainer);
        
        
        mainContainer.getChildren().addAll(textContainer, nodesContainer);
        
        Pane pane = new Pane();
        pane.getChildren().add(mainContainer);
        
        
        mainContainer.layoutXProperty().bind(pane.widthProperty().subtract(mainContainer.widthProperty()).divide(2));
        
        return pane;
    }
    
    /**
     * Helper method to create a container for a game node view along with a label.
     * The label displays the name of the node (e.g., "Wire", "Bulb", etc.).
     * 
     * @param nodeView The node view to be displayed.
     * @param labelText The label text to describe the node.
     * @return A {@link VBox} containing the node view and its label.
     */
    private VBox createNodeWithLabel(Node nodeView, String labelText) {
        VBox container = new VBox(5);
        container.setAlignment(Pos.CENTER);
        
        Label label = new Label(labelText);
        label.getStyleClass().add("node-label");
        
        container.getChildren().addAll(nodeView, label);
        return container;
    }

    /**
     * Helper method to create a button with the specified label and style ID.
     * 
     * @param label The label of the button.
     * @param group The style ID for the button.
     * @return A {@link Button} with the specified label and style.
     */
    private Button createButton(String label, String group){
        Button button = new Button(label);
        button.setId(group);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setMinWidth(Region.USE_PREF_SIZE);
        button.setWrapText(true);
        return button;
    }


    /**
     * Launches the game with the specified level. Loads the level's data, initializes the game state, and displays 
     * the game board view. It also handles the undo/redo buttons' visibility and updates the view accordingly.
     * 
     * @param level The level to load.
     */
    private void LoadGame(int level){
        GameLoader loader = new GameLoader("data/levels/level" + level + ".txt");
            game = Game.create(loader.x, loader.y);
            loader.SetUpGame(game);
            game.setWin(false);
            //lambda implmentation of onGameWin(boolean isWin)
            game.addGameWinListener(isWin -> {
                if (isWin) {
                    Pane winPane = createWinMessage(level);
                    mainLayout.setCenter(winPane);
                }
            });
            game.init();

            gameBoardView = new GameBoardView(game, undoButton, redoButton);
            mainLayout.setCenter(gameBoardView);
            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> new RandomizeGame(game, loader));
            delay.play();
    }

    /**
     * Creates a message that is displayed when the player wins a level, showing the number of turns taken
     * and providing options to go to the next level or return to the main menu.
     * 
     * @param currentLevel The current level number to determine if the player can move to the next level.
     * @return A {@link Pane} containing the win message.
     */
    private Pane createWinMessage(int currentLevel) {
        VBox winPane = new VBox(20);
        winPane.setAlignment(Pos.CENTER);
        winPane.setPadding(new Insets(40));
        
        Text congrats = new Text("Congratulations!");
        congrats.getStyleClass().add("title");
        
        Text winText = new Text("You Won!");
        winText.getStyleClass().add("subtitle");

        Text nOOfTurns = new Text("Turns made: " + String.valueOf(gameBoardView.getTurns()));
        nOOfTurns.getStyleClass().add("subtitle");
        
        Button nextLevelButton = new Button("Next Level");
        nextLevelButton.setId("levelButton");
        nextLevelButton.setOnAction(e -> {
            if (currentLevel < 4) {
                LoadGame(currentLevel + 1);
            } else {
                Text message = new Text("You completed all levels!");
                winPane.getChildren().add(message);
                nextLevelButton.setDisable(true);
            }
        });
        
        Button menuButton = new Button("Back to Intro");
        menuButton.setId("levelButton");
        menuButton.setOnAction(e -> {
            mainLayout.setCenter(intro);
            undoButton.setVisible(false);
            redoButton.setVisible(false);
        });
        
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(nextLevelButton, menuButton);
        
        winPane.getChildren().addAll(congrats, winText, nOOfTurns, buttonBox);
        
        return winPane;
    }
}

