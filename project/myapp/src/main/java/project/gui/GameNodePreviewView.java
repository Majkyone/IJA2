package project.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import project.common.*;

/**
 * The {@link GameNodePreviewView} class represents a visual preview of a {@link GameNode}.
 * <p>
 * This class is responsible for displaying a preview of a game node in the game board. It
 * uses a rectangle to represent the node and text to display the number of turns remaining
 * before the node becomes "good". The view updates its style dynamically based on the node's state.
 * It also implements the {@link Observer} interface to respond to changes in the {@link GameNode}.
 */
public class GameNodePreviewView extends Pane implements Observer {
    private GameNode node;
    private Rectangle rectangle;
    private Text nodeText;
    private int turnsToBeGood;
    
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    
    /**
     * Creates a new preview view for the given {@link GameNode}.
     * 
     * @param node The {@link GameNode} to be displayed.
     */
    public GameNodePreviewView(GameNode node) {
        this.node = node;
        turnsToBeGood = this.node.getNumberOfTurns();
        
        if (node instanceof Observable observable) {
            observable.addObserver(this);
        }
        
        // Set initial style based on node state
        updateNodeStyle();
        
        this.getStylesheets().add(getClass().getResource("/preview.css").toExternalForm());
        this.getChildren().addAll(createNode());
    }
    
    /**
     * Updates the node's style based on its current state.
     * <p>
     * This method applies the appropriate CSS class to the node depending on whether the node is powered
     * or unpowered, and based on the number of turns remaining.
     */
    private void updateNodeStyle() {
        // First clear any existing style classes
        this.getStyleClass().remove(POWERED_CLASS);
        this.getStyleClass().remove(UNPOWERED_CLASS);
        
        // Only apply styles to non-empty nodes
        if (node.getType() != NodeType.EMPTY) {
            if (node.getNumberOfTurns() == 0) {
                this.getStyleClass().add(POWERED_CLASS);
            } else {
                this.getStyleClass().add(UNPOWERED_CLASS);
            }
        }
    }
    
    /**
     * Creates the visual representation of the node.
     * <p>
     * This method creates a {@link Pane} containing a rectangle that represents the node and
     * text displaying the number of turns remaining before the node becomes "good".
     * 
     * @return The {@link Pane} representing the game node.
     */
    public Pane createNode() {
        Pane pane = new Pane();
        pane.getStyleClass().add(this.node.getType().swichSideToString());
        
        // Create rectangle
        rectangle = new Rectangle();
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        
        // Create text that will be in the center
        if (this.node.getType() != NodeType.EMPTY) {
            nodeText = new Text(String.valueOf(turnsToBeGood));
        } else {
            nodeText = new Text("");
        }
        nodeText.getStyleClass().add("node-text");
        nodeText.setStyle("-fx-font-size: 14; -fx-fill: black;");
        
        // Center the text
        nodeText.setBoundsType(TextBoundsType.VISUAL);
        nodeText.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
            nodeText.setX((pane.getWidth() - newVal.getWidth()) / 2);
            nodeText.setY((pane.getHeight() + newVal.getHeight()) / 2);
        });
        
        pane.widthProperty().addListener((obs, oldVal, newVal) -> {
            nodeText.setX((newVal.doubleValue() - nodeText.getLayoutBounds().getWidth()) / 2);
        });
        
        pane.heightProperty().addListener((obs, oldVal, newVal) -> {
            nodeText.setY((newVal.doubleValue() + nodeText.getLayoutBounds().getHeight()) / 2);
        });

        if (this.node.getSides().length == 4) {
            nodeText.setText("");
            pane.getStyleClass().add("universal");
        }
        
        // Add elements to the panel
        pane.getChildren().addAll(rectangle, nodeText);
        return pane;
    }
    
     /**
     * Updates the view when the observed {@link GameNode} changes.
     * <p>
     * This method is called whenever the observed node is updated. It refreshes the text and 
     * style of the node preview based on the node's new state.
     * 
     * @param observable The {@link Observable} object that was updated (in this case, the {@link GameNode}).
     */
    @Override
    public void update(Observable observable) {
        if (observable instanceof GameNode gameNode) {
            // Update the text regardless of node type (as long as it's not empty)
            if (gameNode.getType() != NodeType.EMPTY && gameNode.getSides().length < 4) {
                turnsToBeGood = gameNode.getNumberOfTurns();
                nodeText.setText(String.valueOf(turnsToBeGood));
                updateNodeStyle();
            }
        }
    }
}