package project.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import project.game.*;

/**
 * The {@link GamePreviewWindowView} class represents a window that displays a preview of the game board.
 * <p>
 * This window provides an overview of the game state, showing the layout of the game board, as well as a 
 * legend that explains the meaning of different node types (e.g., powered, unpowered, universal, empty).
 * It is designed to give players a clear understanding of the game mechanics and help them navigate the board.
 */
public class GamePreviewWindowView extends Stage {
    
    /**
     * Constructs the {@link GamePreviewWindowView} for the given {@link Game}.
     * <p>
     * This constructor sets the window title, creates the game board preview, and adds a legend at the bottom.
     * The scene is configured with a {@link BorderPane} layout, where the preview board is placed at the center 
     * and the legend is placed at the bottom. The window is non-resizable.
     *
     * @param game The game whose board will be previewed.
     */
    public GamePreviewWindowView(Game game) {
        this.setTitle("Game Preview");
        
        GameBoardPreviewView previewBoard = new GameBoardPreviewView(game);
        
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(previewBoard);
        mainLayout.setPadding(new Insets(10));
        
        VBox legend = createLegend();
        mainLayout.setBottom(legend);
        BorderPane.setMargin(legend, new Insets(10, 0, 0, 0));
        
        Scene previewScene = new Scene(mainLayout);
        
        previewScene.getStylesheets().add(getClass().getResource("/preview.css").toExternalForm());
        
        this.setScene(previewScene);
        this.setResizable(false);
    }
    
    /**
     * Creates a legend panel that explains the color coding of nodes.
     * <p>
     * The legend provides a clear visual guide to help users understand the meaning of different node types.
     * It includes descriptions of powered, unpowered, universal, and empty nodes, along with their corresponding colors.
     *
     * @return A {@link VBox} containing the legend for the game preview.
     */
    private VBox createLegend() {
        VBox legendContainer = new VBox(10);
        legendContainer.setAlignment(Pos.CENTER);
        
        Label legendTitle = new Label("Legend");
        legendTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label legendSubTitle = new Label("Each number represents number of turns left to achive good conncetion");
        legendSubTitle.setStyle("-fx-font-size: 12; -fx-text-fill:rgb(217, 62, 62);");

        HBox legendItems = new HBox(20);
        legendItems.setAlignment(Pos.CENTER);
        
        HBox poweredNode = createLegendItem("Powered Node", "powered-node");
        
        HBox unpoweredNode = createLegendItem("Unpowered Node", "unpowered-node");

        HBox universalNode = createLegendItem("Universal node", "universal");
        
        HBox emptyNode = createLegendItem("Empty Node", null);
        Rectangle emptyRect = (Rectangle) emptyNode.getChildren().get(0);
        emptyRect.setFill(Color.LIGHTGRAY);
        emptyRect.setStroke(Color.GRAY);
        
        legendItems.getChildren().addAll(poweredNode, unpoweredNode, universalNode, emptyNode);
        legendContainer.getChildren().addAll(legendTitle, legendSubTitle, legendItems);
        
        return legendContainer;
    }
    
    /**
     * Creates a legend item consisting of a colored square and a label.
     * <p>
     * The colored square represents the node type (e.g., powered, unpowered, universal, empty), 
     * and the label provides a description of the node type.
     *
     * @param text The text to display next to the color square.
     * @param styleClass The CSS class to apply to the color square (e.g., "powered-node", "unpowered-node").
     * @return An {@link HBox} containing the color square and label.
     */
    private HBox createLegendItem(String text, String styleClass) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        
        Rectangle colorSquare = new Rectangle(20, 20);
        if (styleClass != null) {
            colorSquare.getStyleClass().add(styleClass);
            if (styleClass.equals("powered-node")) {
                colorSquare.setFill(Color.GREEN);
            } else if (styleClass.equals("unpowered-node")) {
                colorSquare.setFill(Color.ORANGE);
            }
            else if (styleClass.equals("universal")) {
                colorSquare.setFill(Color.rgb(37, 234, 224, 1.0));
            }
        }
        colorSquare.setStroke(Color.BLACK);
        colorSquare.setStrokeWidth(1);
        
        Label label = new Label(text);
        
        item.getChildren().addAll(colorSquare, label);
        
        return item;
    }
}