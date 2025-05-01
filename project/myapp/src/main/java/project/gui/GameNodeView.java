package project.gui;

import project.common.*;

import javafx.scene.paint.Color;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class GameNodeView extends StackPane implements Observer{
    private final GameNode node;
    
    public GameNodeView(GameNode node){
        this.node = node;

        //added view of node as node observer
        if(node instanceof Observable observable){
            observable.addObserver(this);
        }
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
        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setFill(Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
        pane.getChildren().add(rectangle);
        return pane;
    }

    
    private StackPane createLinkNode(){
        StackPane pane = new StackPane();
    
        Rectangle rectangle = new Rectangle(50, 50);
        rectangle.setFill(Color.LIGHTGREY);
        rectangle.setStroke(Color.BLACK);
    
        // creates lines to represend wire
        switch (node.getShape()) {
            case X:
                Line wire1 = new Line(0, 25, 50, 25); 
                Line wire2 = new Line(25, 0, 25, 50); 
                wire1.setStroke(Color.BLACK);
                wire2.setStroke(Color.BLACK);
                wire1.setStrokeWidth(3);
                wire2.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wire1, wire2); 
                break;
            
            case NE:
                Line wireNE = new Line(0, 25, 50, 25); 
                wireNE.setStroke(Color.BLACK);
                wireNE.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireNE); 
                break;
    
            case WE:
                Line wireWE = new Line(0, 25, 50, 25);
                wireWE.setStroke(Color.BLACK);
                wireWE.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireWE);
                break;
    
            case I:
                Line wireI = new Line(25, 0, 25, 50);
                wireI.setStroke(Color.BLACK);
                wireI.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireI);
                break;
    
            case NW:
                Line wireNW = new Line(0, 25, 50, 25);
                wireNW.setStroke(Color.BLACK);
                wireNW.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireNW);
                break;
    
            case SE:
                Line wireSE = new Line(0, 25, 50, 25);
                wireSE.setStroke(Color.BLACK);
                wireSE.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireSE);
                break;
    
            case SW:
                Line wireSW = new Line(0, 25, 50, 25);
                wireSW.setStroke(Color.BLACK);
                wireSW.setStrokeWidth(3);
                pane.getChildren().addAll(rectangle, wireSW);
                break;
            
            default:
                break;
        }
    
        return pane;
    }
    public void update(Observable observable) {
        // Implement the update logic here when the observable notifies this observer
    }
    
}
