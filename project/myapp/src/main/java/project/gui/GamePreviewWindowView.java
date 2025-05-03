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

public class GamePreviewWindowView extends Stage {
    
    public GamePreviewWindowView(Game game) {
        // Set window title
        this.setTitle("Game Preview");
        
        // Create game board preview
        GameBoardPreviewView previewBoard = new GameBoardPreviewView(game);
        
        // Create main layout container
        BorderPane mainLayout = new BorderPane();
        mainLayout.setCenter(previewBoard);
        mainLayout.setPadding(new Insets(10));
        
        // Add legend at the bottom
        VBox legend = createLegend();
        mainLayout.setBottom(legend);
        BorderPane.setMargin(legend, new Insets(10, 0, 0, 0));
        
        // Create scene with the layout
        Scene previewScene = new Scene(mainLayout);
        
        // Optional: Add CSS for consistent styling
        previewScene.getStylesheets().add(getClass().getResource("/preview.css").toExternalForm());
        
        // Set scene and disable resizing
        this.setScene(previewScene);
        this.setResizable(false);
    }
    
    /**
     * Creates a legend panel explaining the color coding of nodes
     */
    private VBox createLegend() {
        VBox legendContainer = new VBox(10);
        legendContainer.setAlignment(Pos.CENTER);
        
        Label legendTitle = new Label("Legend");
        legendTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14;");

        Label legendSubTitle = new Label("Each number represents number of turns left to achive good conncetion");
        legendSubTitle.setStyle("-fx-font-size: 12; -fx-text-fill:rgb(217, 62, 62);");

        
        // Create a container for legend items
        HBox legendItems = new HBox(20);
        legendItems.setAlignment(Pos.CENTER);
        
        // Add powered node legend item
        HBox poweredNode = createLegendItem("Powered Node", "powered-node");
        
        // Add unpowered node legend item
        HBox unpoweredNode = createLegendItem("Unpowered Node", "unpowered-node");

        // Add unpowered node legend item
        HBox universalNode = createLegendItem("Universal node", "universal");
        
        // Add empty node legend item
        HBox emptyNode = createLegendItem("Empty Node", null);
        Rectangle emptyRect = (Rectangle) emptyNode.getChildren().get(0);
        emptyRect.setFill(Color.LIGHTGRAY);
        emptyRect.setStroke(Color.GRAY);
        
        // Add items to the legend container
        legendItems.getChildren().addAll(poweredNode, unpoweredNode, universalNode, emptyNode);
        legendContainer.getChildren().addAll(legendTitle, legendSubTitle, legendItems);
        
        return legendContainer;
    }
    
    /**
     * Creates a legend item with a colored square and label
     */
    private HBox createLegendItem(String text, String styleClass) {
        HBox item = new HBox(5);
        item.setAlignment(Pos.CENTER_LEFT);
        
        // Create colored square
        Rectangle colorSquare = new Rectangle(20, 20);
        if (styleClass != null) {
            colorSquare.getStyleClass().add(styleClass);
            // Fallback colors in case CSS doesn't load
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
        
        // Create label
        Label label = new Label(text);
        
        // Add to item container
        item.getChildren().addAll(colorSquare, label);
        
        return item;
    }
}