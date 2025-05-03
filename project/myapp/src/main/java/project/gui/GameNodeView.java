package project.gui;

import project.common.*;
import javafx.scene.paint.Color;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;

import javafx.scene.shape.Path;

import javafx.scene.Group;


public class GameNodeView extends Pane implements Observer {
    private final GameNode node;
    private Rectangle rectangle;
    
    // CSS classes for power and no power
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    private final double STROKE_WIDTH = 5.0;
    
    public GameNodeView(GameNode node) {
        this.node = node;
        // add view as observer
        if(node instanceof Observable observable){
            observable.addObserver(this);
        }
        
        // adds css
        this.getStylesheets().add(getClass().getResource("/gamenode.css").toExternalForm());
        
        // Apply the style class to the entire pane (not just the rectangle)
        if (node.isPowered()) {
            this.getStyleClass().add(POWERED_CLASS);
        }else{
            this.getStyleClass().add(UNPOWERED_CLASS);

        }
        this.getStyleClass().add("game-node"); // Add a class to the entire pane for hover effects
        
        this.getChildren().addAll(createNode());
    }
    
    private Pane createNode(){
        switch (node.getType()) {
            case EMPTY:
                return createEmptyNode();
            case WIRE:
                return createLinkNode();
            case SOURCE:
                return createSourceNode();
            case BULB:
                return createBulbNode();
            default:
                throw new IllegalStateException("Unexpected node value: " + node.getType());
        }
    }
    
    private Pane createEmptyNode(){
        Pane pane = new Pane();
        // Apply the style class to the pane itself for hover effects
        pane.getStyleClass().add("node-pane");
        
        // Add specific class for empty nodes
        pane.getStyleClass().add("empty-node"); // <-- Pridaj túto triedu
        
        rectangle = new Rectangle();
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        
        pane.getChildren().add(rectangle);
        return pane;
    }
    private void addWireToPane(Pane pane, Line wire) {
        // Increase stroke width for better visibility
        wire.setStrokeWidth(STROKE_WIDTH);
        
        // Apply wire style class
        wire.getStyleClass().add("wire-line");
        
        // Ensure lines extend to edges by adjusting start/end caps
        wire.setStrokeLineCap(StrokeLineCap.BUTT);

        wire.toFront();
        
        pane.getChildren().add(wire);
    }
    
    private Pane createLinkNode(){
        Pane pane = new Pane();
        pane.setId("node");
        // Apply the style class to the pane itself for hover effects
        pane.getStyleClass().add("node-pane");
        
        
        Rectangle rectangle = new Rectangle();
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

    private Pane createBulbNode(){
        Pane pane = new Pane();
        pane.setId("node");
        // Apply the style class to the pane itself for hover effects
        pane.getStyleClass().add("node-pane");
        pane.getStyleClass().add("bulb-node");
        
        Rectangle rectangle = new Rectangle();
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

         // Pridanie kruhu do stredu ako žiarovky
         Circle bulb = new Circle();
         bulb.setRadius(10); // veľkosť žiarovky
         bulb.getStyleClass().add("bulb");
         // bulb.setFill(Color.YELLOW);
         // bulb.setStroke(Color.BLACK);
 
         // Umiestniť žiarovku do stredu podľa veľkosti parenta
         bulb.centerXProperty().bind(pane.widthProperty().divide(2));
         bulb.centerYProperty().bind(pane.heightProperty().divide(2));
         pane.getChildren().add(bulb);
        
        return pane;
    }

    private Pane createSourceNode() {
        Pane pane = new Pane();
        pane.setId("node");
        pane.getStyleClass().add("node-pane");
        pane.getStyleClass().add("source-node"); // CSS trieda pre odlíšenie
    
        Rectangle rectangle = new Rectangle();
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        pane.getChildren().add(rectangle);
    
        Side[] sides = node.getSides();
        for (Side side : sides) {
            Line wire = createWireForSide(pane, side);
            if (wire != null) {
                addWireToPane(pane, wire);
            }
        }
    
        // Vytvorenie symbolu zdroja - menší kruh s vlnkou
        Group sourceSymbol = new Group();
    
        Circle circle = new Circle(10); // Zmenšený kruh
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(2);
        circle.getStyleClass().add("source");
    
        Path wave = new Path();
        wave.setStroke(Color.BLACK);
        wave.setStrokeWidth(2);
        wave.setFill(null);
    
        // Menšia vlnka cez stred kruhu
        MoveTo moveTo = new MoveTo(-5, 0);
        QuadCurveTo curve1 = new QuadCurveTo(-2, -5, 0, 0);
        QuadCurveTo curve2 = new QuadCurveTo(2, 5, 5, 0);
        wave.getElements().addAll(moveTo, curve1, curve2);
    
        sourceSymbol.getChildren().addAll(circle, wave);
    
        // Zarovnanie do stredu
        sourceSymbol.layoutXProperty().bind(pane.widthProperty().divide(2));
        sourceSymbol.layoutYProperty().bind(pane.heightProperty().divide(2));
    
        pane.getChildren().add(sourceSymbol);
    
        return pane;
    }
    

    private Line createWireForSide(Pane pane, Side side) {
        Line wire = new Line();
        double offset = STROKE_WIDTH / 2;
        switch (side) {
            case NORTH:
                System.out.println("creating north link");
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2).add(offset));
                wire.endXProperty().bind(this.widthProperty().divide(2));
                wire.endYProperty().bind(Bindings.createDoubleBinding(() -> 0.0, this.heightProperty()));
                return wire;
                    
            case EAST:
                // Wire from center to right
                System.out.println("creating east link");
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2));
                wire.endXProperty().bind(this.widthProperty());
                wire.endYProperty().bind(this.heightProperty().divide(2));
                return wire;
                    
            case SOUTH:
                // Wire from center to bottom
                System.out.println("creating south link");
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2).subtract(offset));
                wire.endXProperty().bind(this.widthProperty().divide(2));
                wire.endYProperty().bind(this.heightProperty()); // To the bottom
                return wire;
                    
            case WEST:
                // Wire from center to left
                System.out.println("creating west link");
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2));
                wire.endXProperty().set(0);  // To the left side
                wire.endYProperty().bind(this.heightProperty().divide(2));
                return wire;
                    
            default:
                return null;
        }
    }
    
    
    public void update(Observable observable) {
        if(node.getType() != NodeType.EMPTY){
            System.out.println("update na view");
            if (observable instanceof GameNode gameNode) {
                // change color of wire 
                if (gameNode.isPowered()) {
                    this.getStyleClass().remove(UNPOWERED_CLASS);
                    this.getStyleClass().add(POWERED_CLASS);
                } else {
                    this.getStyleClass().remove(POWERED_CLASS);
                    this.getStyleClass().add(UNPOWERED_CLASS);
                }
                
                System.out.println(node.getTurns() % 4);
                int turns = node.getTurns() % 4; // Zabezpečí, že sa točí v rozsahu 0-3 (360°)
                this.setRotate(turns * 90);
            }
        }
    }
}