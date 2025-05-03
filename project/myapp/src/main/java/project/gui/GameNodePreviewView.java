package project.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import project.common.*;

public class GameNodePreviewView extends Pane implements Observer {
    private GameNode node;
    private Rectangle rectangle;
    private Text nodeText;
    private int turnsToBeGood;
    
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    
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
     * Update the node's style based on its current state
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
    
    @Override
    public void update(Observable observable) {
        if (observable instanceof GameNode gameNode) {
            // Update the text regardless of node type (as long as it's not empty)
            if (gameNode.getType() != NodeType.EMPTY && gameNode.getSides().length < 4) {
                turnsToBeGood = gameNode.getNumberOfTurns();
                nodeText.setText(String.valueOf(turnsToBeGood));
                
                // Debug output
                System.out.println("Node updated: " + gameNode.getType() + ", turns left: " + turnsToBeGood);
                
                // Update styles based on current state - removed the sides.length check
                updateNodeStyle();
            }
        }
    }
}