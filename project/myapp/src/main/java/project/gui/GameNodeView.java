package project.gui;

import project.common.*;
import javafx.scene.paint.Color;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javafx.geometry.Pos;

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
        if (node instanceof Observable observable) {
            observable.addObserver(this);
        }

        // adds css
        this.getStylesheets().add(getClass().getResource("/gamenode.css").toExternalForm());

        // default class unpowered
        this.getStyleClass().add(POWERED_CLASS);

        this.getChildren().addAll(createNode());

    }

    private StackPane createNode() {
        switch (node.getType()) {
            case EMPTY:
                return createEmptyNode();
            case WIRE:
                return createLinkNode();
            case SOURCE:
                return createLinkNode();
            case BULB:
                return createLinkNode();
            default:
                throw new IllegalStateException("Unexpected node value: " + node.getType());
        }
    }

    private StackPane createEmptyNode() {
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

    private StackPane createLinkNode() {
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

        StackPane northPane = new StackPane();
        StackPane southPane = new StackPane();
        StackPane eastPane = new StackPane();
        StackPane westPane = new StackPane();

        northPane.setAlignment(Pos.TOP_CENTER);
        southPane.setAlignment(Pos.BOTTOM_CENTER);
        eastPane.setAlignment(Pos.CENTER_RIGHT);
        westPane.setAlignment(Pos.CENTER_LEFT);
        pane.getChildren().addAll(northPane, southPane, eastPane, westPane);
        for (Side side : node.getSides()) {
            switch (side) {
                case Side.NORTH:

                    Line northLine = new Line();
                    northLine.startXProperty().bind(this.widthProperty().multiply(0.5));
                    // Stredový bod Y (50%)
                    northLine.startYProperty().bind(this.heightProperty().multiply(0.5));
                    // Stredový bod X (50%)
                    northLine.endXProperty().bind(this.widthProperty().multiply(0.5));
                    // Horný okraj (0%)
                    northLine.endYProperty().bind(this.heightProperty());
                    addWireToPane(northPane, northLine);
                    break;
                case Side.WEST:
                    Line westLine = new Line();
                    westLine.startXProperty().bind(this.widthProperty().multiply(0.5));
                    // Stredový bod Y (50%)
                    westLine.startYProperty().bind(this.heightProperty().multiply(0.5));
                    // Stredový bod X (50%)
                    westLine.endXProperty().bind(this.widthProperty());
                    // Horný okraj (0%)
                    westLine.endYProperty().bind(this.heightProperty().multiply(0.5));
                    addWireToPane(westPane, westLine);
                    break;
                case Side.EAST:
                    Line eastLine = new Line();
                    eastLine.startXProperty().bind(this.widthProperty().multiply(0.5));
                    // Stredový bod Y (50%)
                    eastLine.startYProperty().bind(this.heightProperty().multiply(0.5));
                    // Stredový bod X (50%)
                    eastLine.endXProperty().bind(this.widthProperty());
                    // Horný okraj (0%)
                    eastLine.endYProperty().bind(this.heightProperty().multiply(0.5));
                    addWireToPane(eastPane, eastLine);
                    break;
                case Side.SOUTH:
                    Line southLine = new Line();
                    southLine.startXProperty().bind(this.widthProperty().multiply(0.5));
                    // Stredový bod Y (50%)
                    southLine.startYProperty().bind(this.heightProperty().multiply(0.5));
                    // Stredový bod X (50%)
                    southLine.endXProperty().bind(this.widthProperty().multiply(0.5));
                    // Horný okraj (0%)
                    southLine.endYProperty().bind(this.heightProperty());
                    addWireToPane(southPane, southLine);
                    break;
                default:
                    break;
            }
        }

        return pane;
    }

    public void update(Observable observable) {
        if (node.getType() != NodeType.EMPTY) {

            if (observable instanceof GameNode gameNode) {
                // change color of wire

                if (gameNode.isPowered()) {
                    while (this.getStyleClass().contains(UNPOWERED_CLASS)) {
                        this.getStyleClass().remove(UNPOWERED_CLASS);
                    }
                    this.getStyleClass().add(POWERED_CLASS);
                } else {
                    while (this.getStyleClass().contains(POWERED_CLASS)) {
                        this.getStyleClass().remove(POWERED_CLASS);
                    }
                    this.getStyleClass().add(UNPOWERED_CLASS);
                }

                // rotate 90 deg.
                int turns = node.getTotalNumberOfTurns() % 4;
                this.setRotate(turns * 90);
                // NodeShape newShape = gameNode.getShape();
                // if (this.shape != newShape) {

                // }
                // this.shape = newShape;
            }
        }
    }
}