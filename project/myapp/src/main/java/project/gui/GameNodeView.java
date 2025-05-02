package project.gui;

import project.common.*;
import javafx.scene.paint.Color;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

public class GameNodeView extends Pane implements Observer {
    private final GameNode node;
    private Rectangle rectangle;
    
    // CSS classes for power and no power
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    
    public GameNodeView(GameNode node) {
        this.node = node;
        // add view as observer
        if(node instanceof Observable observable){
            observable.addObserver(this);
        }
        
        // adds css
        this.getStylesheets().add(getClass().getResource("/gamenode.css").toExternalForm());
        
        // default class unpowered
        this.getStyleClass().add(UNPOWERED_CLASS);
        
        this.getChildren().addAll(createNode());
    }
    
    private Pane createNode(){
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
    
    private Pane createEmptyNode(){
        Pane pane = new Pane();
        pane.setId("node");
        rectangle = new Rectangle();
        // Don't set fill and stroke directly - let CSS handle it
        // rectangle.setFill(Color.LIGHTGRAY);
        // rectangle.setStroke(Color.BLACK);
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        
        // Add the style class - THIS WAS COMMENTED OUT BEFORE
        rectangle.getStyleClass().add("node-rectangle");
        
        pane.getChildren().add(rectangle);
        return pane;
    }
    
    private void addWireToPane(Pane pane, Line wire) {
        // Increase stroke width for better visibility
        wire.setStrokeWidth(5);
        
        // Apply wire style class
        wire.getStyleClass().add("wire-line");
        
        // Ensure lines extend to edges by adjusting start/end caps
        wire.setStrokeLineCap(StrokeLineCap.BUTT);

        wire.toFront();
        
        pane.getChildren().add(wire);
    }
    
    private Pane createLinkNode(){
        Pane pane = new Pane();  // Používame Pane namiesto StackPane
        pane.setId("node");
        Rectangle rectangle = new Rectangle();
        // Don't set fill and stroke directly - let CSS handle it
        // rectangle.setStroke(Color.BLACK);
        // rectangle.setFill(Color.LIGHTGRAY);
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        pane.getChildren().add(rectangle);

        Side [] sides = node.getSides();    

        for (Side side : sides) {
            Line wire = createWireForSide(pane, side);
            if (wire != null) {
                addWireToPane(pane, wire);
            }
        }
        
        return pane;
    }

    private Line createWireForSide(Pane pane, Side side) {
        Line wire = new Line();
        
        switch (side) {
            case NORTH:
                System.out.println("creating north link");
                wire.startXProperty().bind(pane.widthProperty().divide(2));
                wire.startYProperty().bind(pane.heightProperty().divide(2));
                wire.endXProperty().bind(pane.widthProperty().divide(2));
                wire.endYProperty().bind(Bindings.createDoubleBinding(() -> 0.0, pane.heightProperty()));
                return wire;
                    
            case EAST:
                // Wire from center to right
                System.out.println("creating east link");
                wire.startXProperty().bind(pane.widthProperty().divide(2));
                wire.startYProperty().bind(pane.heightProperty().divide(2));
                wire.endXProperty().bind(pane.widthProperty());
                wire.endYProperty().bind(pane.heightProperty().divide(2));
                return wire;
                    
            case SOUTH:
                // Wire from center to bottom
                System.out.println("creating south link");
                wire.startXProperty().bind(pane.widthProperty().divide(2));
                wire.startYProperty().bind(pane.heightProperty().divide(2));
                wire.endXProperty().bind(pane.widthProperty().divide(2));
                wire.endYProperty().bind(pane.heightProperty()); // To the bottom
                return wire;
                    
            case WEST:
                // Wire from center to left
                System.out.println("creating west link");
                wire.startXProperty().bind(pane.widthProperty().divide(2));
                wire.startYProperty().bind(pane.heightProperty().divide(2));
                wire.endXProperty().set(0);  // To the left side
                wire.endYProperty().bind(pane.heightProperty().divide(2));
                return wire;
                    
            default:
                return null;
        }
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
                // NodeShape newShape = gameNode.getShape();
                // if (this.shape != newShape) {
                //     int turns = node.getNumberOfturns() % 4;
                //     this.setRotate(turns * 90);
                // }
                // this.shape = newShape;
            }
        }
    }
}
