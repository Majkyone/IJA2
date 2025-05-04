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

/**
 * The {@link GameNodeView} class represents the visual representation of a {@link GameNode} in the game.
 * <p>
 * This class is responsible for displaying the graphical representation of different types of game nodes
 * on the game board. It uses various shapes like rectangles, circles, and lines to represent nodes such as
 * wires, bulbs, sources, and empty nodes. The view dynamically updates based on the node's state, 
 * including whether it is powered or unpowered. It also handles the rotation of nodes and adds CSS styling.
 * This class implements the {@link Observer} interface to respond to changes in the {@link GameNode}.
 */
public class GameNodeView extends Pane implements Observer {
    private final GameNode node;
    private Rectangle rectangle;
    
    // CSS classes for power and no power
    private static final String POWERED_CLASS = "powered-node";
    private static final String UNPOWERED_CLASS = "unpowered-node";
    private final double STROKE_WIDTH = 5.0;
    
    /**
     * Constructs a {@link GameNodeView} for the given {@link GameNode}.
     * 
     * @param node The {@link GameNode} to be visualized.
     */
    public GameNodeView(GameNode node) {
        this.node = node;
        
        if (node instanceof Observable observable) {
            observable.addObserver(this);
        }
        
        this.getStylesheets().add(getClass().getResource("/gamenode.css").toExternalForm());
        
        this.getStyleClass().add("game-node");
        
        updatePowerStyles();

        this.getChildren().addAll(createNode());
    }
    
    /**
     * Updates the node's style classes based on its current power state.
     * If the node is powered, it applies the 'powered-node' class,
     * otherwise it applies the 'unpowered-node' class.
     */
    private void updatePowerStyles() {
        
        this.getStyleClass().remove(POWERED_CLASS);
        this.getStyleClass().remove(UNPOWERED_CLASS);
        
        if (node.getType() != NodeType.EMPTY) {
            if (node.isPowered()) {
                this.getStyleClass().add(POWERED_CLASS);
            } else {
                this.getStyleClass().add(UNPOWERED_CLASS);
            }
        }
    }
    
    /**
     * Creates the appropriate visual representation for the {@link GameNode} based on its type.
     * 
     * @return A {@link Pane} containing the graphical elements representing the node.
     */
    private Pane createNode() {
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
    
    /**
     * Creates the graphical representation for an empty node.
     * 
     * @return A {@link Pane} containing a rectangle representing the empty node.
     */
    private Pane createEmptyNode() {
        Pane pane = new Pane();
        // Apply style classes
        pane.getStyleClass().add("node-pane");
        pane.getStyleClass().add("empty-node");
        
        rectangle = new Rectangle();
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        
        pane.getChildren().add(rectangle);
        return pane;
    }
    
    /**
     * Adds a wire to the node's pane.
     * 
     * @param pane The pane to which the wire will be added.
     * @param wire The wire to be added.
     */
    private void addWireToPane(Pane pane, Line wire) {
        wire.setStrokeWidth(STROKE_WIDTH);
        
        wire.getStyleClass().add("wire-line");
        
        wire.setStrokeLineCap(StrokeLineCap.BUTT);

        wire.toFront();
        
        pane.getChildren().add(wire);
    }
    
    /**
     * Creates the graphical representation for a wire node.
     * 
     * @return A {@link Pane} containing the graphical elements representing the wire node.
     */
    private Pane createLinkNode() {
        Pane pane = new Pane();
        pane.setId("node");
        pane.getStyleClass().add("node-pane");
        
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
        
        return pane;
    }

    /**
     * Creates the graphical representation for a bulb node.
     * 
     * @return A {@link Pane} containing the graphical elements representing the bulb node.
     */
    private Pane createBulbNode() {
        Pane pane = new Pane();
        pane.setId("node");
        pane.getStyleClass().add("node-pane");
        pane.getStyleClass().add("bulb-node");
        
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
    
        // Create electrical symbol for light bulb (circle with X inside)
        Group bulbSymbol = new Group();
        
        // Create the circle component
        Circle bulbCircle = new Circle();
        bulbCircle.setRadius(12);
        bulbCircle.setStroke(Color.BLACK);
        bulbCircle.setFill(Color.TRANSPARENT);
        bulbCircle.setStrokeWidth(2);
        bulbCircle.getStyleClass().add("bulb-circle");
        
        // Create the X inside the circle using two lines
        Line diagonal1 = new Line(-8, -8, 8, 8);
        diagonal1.setStroke(Color.BLACK);
        diagonal1.setStrokeWidth(2);
        diagonal1.getStyleClass().add("bulb-line");
        
        Line diagonal2 = new Line(8, -8, -8, 8);
        diagonal2.setStroke(Color.BLACK);
        diagonal2.setStrokeWidth(2);
        diagonal2.getStyleClass().add("bulb-line");
        
        // Add all components to the group
        bulbSymbol.getChildren().addAll(bulbCircle, diagonal1, diagonal2);
        
        // Center the bulb symbol
        bulbSymbol.layoutXProperty().bind(pane.widthProperty().divide(2));
        bulbSymbol.layoutYProperty().bind(pane.heightProperty().divide(2));
        
        pane.getChildren().add(bulbSymbol);
        
        return pane;
    }

     /**
     * Creates the graphical representation for a source node.
     * 
     * @return A {@link Pane} containing the graphical elements representing the source node.
     */
    private Pane createSourceNode() {
        Pane pane = new Pane();
        pane.setId("node");
        pane.getStyleClass().add("node-pane");
        pane.getStyleClass().add("source-node");
    
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
    
        // Create source symbol - smaller circle with wave
        Group sourceSymbol = new Group();
    
        Circle circle = new Circle(12);
        circle.setStroke(Color.BLACK);
        circle.setFill(Color.TRANSPARENT);
        circle.setStrokeWidth(2);
        circle.getStyleClass().add("source");
    
        Path wave = new Path();
        wave.setStroke(Color.BLACK);
        wave.setStrokeWidth(2);
        wave.setFill(null);
    
        // Small wave through center of circle
        MoveTo moveTo = new MoveTo(-5, 0);
        QuadCurveTo curve1 = new QuadCurveTo(-2, -5, 0, 0);
        QuadCurveTo curve2 = new QuadCurveTo(2, 5, 5, 0);
        wave.getElements().addAll(moveTo, curve1, curve2);
    
        sourceSymbol.getChildren().addAll(circle, wave);
    
        // Center alignment
        sourceSymbol.layoutXProperty().bind(pane.widthProperty().divide(2));
        sourceSymbol.layoutYProperty().bind(pane.heightProperty().divide(2));
    
        pane.getChildren().add(sourceSymbol);
    
        return pane;
    }
    
    /**
     * Creates a wire for a specific side of the node.
     * 
     * @param pane The pane that will contain the wire.
     * @param side The side for which to create the wire.
     * @return A {@link Line} representing the wire, or null if no wire should be created.
     */
    private Line createWireForSide(Pane pane, Side side) {
        Line wire = new Line();
        double offset = STROKE_WIDTH / 2;
        
        switch (side) {
            case NORTH:
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2).add(offset));
                wire.endXProperty().bind(this.widthProperty().divide(2));
                wire.endYProperty().bind(Bindings.createDoubleBinding(() -> 0.0, this.heightProperty()));
                return wire;
                    
            case EAST:
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2));
                wire.endXProperty().bind(this.widthProperty());
                wire.endYProperty().bind(this.heightProperty().divide(2));
                return wire;
                    
            case SOUTH:
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2).subtract(offset));
                wire.endXProperty().bind(this.widthProperty().divide(2));
                wire.endYProperty().bind(this.heightProperty());
                return wire;
                    
            case WEST:
                wire.startXProperty().bind(this.widthProperty().divide(2));
                wire.startYProperty().bind(this.heightProperty().divide(2));
                wire.endXProperty().set(0);
                wire.endYProperty().bind(this.heightProperty().divide(2));
                return wire;
                    
            default:
                return null;
        }
    }
    
    /**
     * The {@link update} method is called when the {@link GameNode} is updated. 
     * It is responsible for reacting to changes in the {@link GameNode} and updating 
     * the view accordingly. This includes updating the node's power state styling 
     * and handling any rotation changes. The rotation is handled by determining the 
     * number of turns (modulo 4) and applying the corresponding rotation in degrees 
     * (90°, 180°, 270°, 360°).
     * 
     * @param observable The observable object that triggered the update, in this case, a {@link GameNode}.
     */
    @Override
    public void update(Observable observable) {
        if (observable instanceof GameNode gameNode) {
            if (gameNode.getType() != NodeType.EMPTY) {
                updatePowerStyles();
                
                // Handle rotation
                int turns = node.getTurns() % 4; // Ensures rotation in range 0-3 (360°)
                this.setRotate(turns * 90);
            }
        }
    }
}