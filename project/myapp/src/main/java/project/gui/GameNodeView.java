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
        this.getStylesheets().add(getClass().getResource("/gamenode.css").toExternalForm());
        
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
    
    private void addWireToPane(StackPane pane, Line wire) {
        wire.setStrokeWidth(3);
        
        // class for wire
        wire.getStyleClass().add("wire-line");
        
        pane.getChildren().add(wire);
    }
    
    private StackPane createLinkNode(){
        StackPane pane = new StackPane();
        pane.setId("node");
        Rectangle rectangle = new Rectangle();
        // Don't set fill and stroke directly - let CSS handle it
        // rectangle.setStroke(Color.BLACK);
        // rectangle.setFill(Color.LIGHTGRAY);
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        pane.getChildren().add(rectangle);
        
        switch (shape) {
            case X:
                Line horizontalWire = new Line();
                horizontalWire.startXProperty().set(0);
                horizontalWire.startYProperty().bind(this.heightProperty().divide(2));
                horizontalWire.endXProperty().bind(this.widthProperty());
                horizontalWire.endYProperty().bind(this.heightProperty().divide(2));
                addWireToPane(pane, horizontalWire);
                
                Line verticalWire = new Line();
                verticalWire.startXProperty().bind(this.widthProperty().divide(2));
                verticalWire.startYProperty().set(0);
                verticalWire.endXProperty().bind(this.widthProperty().divide(2));
                verticalWire.endYProperty().bind(this.heightProperty());
                addWireToPane(pane, verticalWire);
                break;
            case NE:
                Line vertical = new Line();
                vertical.startXProperty().bind(pane.widthProperty().multiply(0.5));
                vertical.startYProperty().set(0);
                vertical.endXProperty().bind(pane.widthProperty().multiply(0.5));
                vertical.endYProperty().bind(pane.heightProperty().multiply(0.5));
                addWireToPane(pane, vertical);
                
                Line horizontal = new Line();
                horizontal.startXProperty().bind(pane.widthProperty().multiply(0.5));
                horizontal.startYProperty().bind(pane.heightProperty().multiply(0.5));
                horizontal.endXProperty().bind(pane.widthProperty());
                horizontal.endYProperty().bind(pane.heightProperty().multiply(0.5));
                addWireToPane(pane, horizontal); 
                break;
            case WE:
                Line wire = new Line(); 
                wire.startXProperty().set(0);
                wire.startYProperty().bind(this.heightProperty().divide(2));
                wire.endXProperty().bind(this.widthProperty());
                wire.endYProperty().bind(this.heightProperty().divide(2));
                addWireToPane(pane, wire); // Horizontálny vodič
                break;
            case I:
                Line wireI = new Line();
                wireI.startXProperty().bind(this.widthProperty().divide(2));
                wireI.startYProperty().set(0);
                wireI.endXProperty().bind(this.widthProperty().divide(2));
                wireI.endYProperty().bind(this.heightProperty());
                addWireToPane(pane, wireI);
                break;
            case NW:
                // Vertikálna časť (ide zo stredu hore)
                Line verticalWireNW = new Line();
                verticalWireNW.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                verticalWireNW.startYProperty().bind(this.heightProperty().multiply(1));   // Začiatok dole
                verticalWireNW.endXProperty().bind(this.widthProperty().multiply(0.5));     // Koniec vertikálne v strede
                verticalWireNW.endYProperty().bind(this.heightProperty().multiply(0));     // Koniec hore

                // Horizontálna časť (ide doľava)
                Line horizontalWireNW = new Line();
                horizontalWireNW.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                horizontalWireNW.startYProperty().bind(this.heightProperty().multiply(0)); // Stred výšky
                horizontalWireNW.endXProperty().bind(this.widthProperty().multiply(0));    // Koniec na ľavom okraji
                horizontalWireNW.endYProperty().bind(this.heightProperty().multiply(0));  // Stred výšky

                // Pridanie do panela
                addWireToPane(pane, verticalWireNW);
                addWireToPane(pane, horizontalWireNW);
                break;
            case SE:
                // Vertikálna časť (ide zo stredu dole)
                Line verticalWireSE = new Line();
                verticalWireSE.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                verticalWireSE.startYProperty().bind(this.heightProperty().multiply(0));   // Začiatok hore
                verticalWireSE.endXProperty().bind(this.widthProperty().multiply(0.5));     // Koniec vertikálne v strede
                verticalWireSE.endYProperty().bind(this.heightProperty().multiply(0.5));   // Stred výšky

                // Horizontálna časť (ide doprava)
                Line horizontalWireSE = new Line();
                horizontalWireSE.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                horizontalWireSE.startYProperty().bind(this.heightProperty().multiply(0.5)); // Stred výšky
                horizontalWireSE.endXProperty().bind(this.widthProperty().multiply(1));    // Koniec na pravom okraji
                horizontalWireSE.endYProperty().bind(this.heightProperty().multiply(0.5));  // Stred výšky

                // Pridanie do panela
                addWireToPane(pane, verticalWireSE);
                addWireToPane(pane, horizontalWireSE);
                break;
            case SW:
                // Vertikálna časť (ide zo stredu dole)
                Line verticalWireSW = new Line();
                verticalWireSW.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                verticalWireSW.startYProperty().bind(this.heightProperty().multiply(0));   // Začiatok hore
                verticalWireSW.endXProperty().bind(this.widthProperty().multiply(0.5));     // Koniec vertikálne v strede
                verticalWireSW.endYProperty().bind(this.heightProperty().multiply(0.5));   // Stred výšky

                // Horizontálna časť (ide doľava)
                Line horizontalWireSW = new Line();
                horizontalWireSW.startXProperty().bind(this.widthProperty().multiply(0.5)); // Stred šírky
                horizontalWireSW.startYProperty().bind(this.heightProperty().multiply(0.5)); // Stred výšky
                horizontalWireSW.endXProperty().bind(this.widthProperty().multiply(0));    // Koniec na ľavom okraji
                horizontalWireSW.endYProperty().bind(this.heightProperty().multiply(0.5));  // Stred výšky

                // Pridanie do panela
                addWireToPane(pane, verticalWireSW);
                addWireToPane(pane, horizontalWireSW);
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