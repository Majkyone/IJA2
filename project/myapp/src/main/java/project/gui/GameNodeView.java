package project.gui;

import project.common.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class GameNodeView extends StackPane implements Observer {
    private final GameNode node;
    private Rectangle rectangle;
    private NodeShape shape;
    
    // CSS classes for power and no power
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    
    public GameNodeView(GameNode node) {
        this.node = node;
        this.shape = node.getShape();
        // add view as observer
        if(node instanceof Observable observable){
            observable.addObserver(this);
        }
        
        // adds css
        this.getStylesheets().add(getClass().getResource("/styles/gamenode.css").toExternalForm());
        
        // default class unpowered
        this.getStyleClass().add(UNPOWERED_CLASS);
        
        this.getChildren().addAll(createNode());
    }
    
    private StackPane createNode(){
        switch (node.getType()) {
            case EMPTY:
                return createEmptyNode();
            case WIRE:
                return createLinkNode();
            case SOURCE:
                //return createSourceNode();
            case BULB:
                // return createBulbNode();
            default:
                throw new IllegalStateException("Unexpected node value: " + node.getType());
        }
    }
    
    private StackPane createEmptyNode(){
        StackPane pane = new StackPane();
        rectangle = new Rectangle(50, 50);
        rectangle.setStroke(Color.BLACK);
        
        // class for rectangle
        rectangle.getStyleClass().add("node-rectangle");
        
        pane.getChildren().add(rectangle);
        return pane;
    }
    
    private void addWireToPane(StackPane pane, Line wire) {
        wire.setStrokeWidth(3);
        
        // class for wire
        wire.getStyleClass().add("wire-line");
        
        pane.getChildren().add(wire);
    }
    
    private StackPane createLinkNode(){
        StackPane pane = new StackPane();
        
        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setStroke(Color.BLACK);
        rectangle.getStyleClass().add("node-rectangle");
        pane.getChildren().add(rectangle);
        
        switch (shape) {
            case X:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Horizontálny vodič
                addWireToPane(pane, new Line(25, 0, 25, 50)); // Vertikálny vodič
                break;
            case NE:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Diagonálny NE
                break;
            case WE:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Horizontálny vodič
                break;
            case I:
                addWireToPane(pane, new Line(25, 0, 25, 50)); // Vertikálny vodič
                break;
            case NW:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Diagonálny NW
                break;
            case SE:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Diagonálny SE
                break;
            case SW:
                addWireToPane(pane, new Line(0, 25, 50, 25)); // Diagonálny SW
                break;
            default:
                break;
        }
        
        return pane;
    }
    
    public void update(Observable observable) {
        if(node.getType() != NodeType.EMPTY){
            if (observable instanceof GameNode gameNode) {
                // change color of wire 
                if (gameNode.isPowered()) {
                    this.getStyleClass().remove(UNPOWERED_CLASS);
                    this.getStyleClass().add(POWERED_CLASS);
                } else {
                    this.getStyleClass().remove(POWERED_CLASS);
                    this.getStyleClass().add(UNPOWERED_CLASS);
                }
                
                // rotate 90 deg.
                NodeShape newShape = gameNode.getShape();
                if (this.shape != newShape) {
                    int turns = node.getNumberOfturns() % 4;
                    this.setRotate(turns * 90);
                }
                this.shape = newShape;
            }
        }
    }
}