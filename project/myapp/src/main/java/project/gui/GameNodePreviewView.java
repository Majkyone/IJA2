package project.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import project.common.*;


public class GameNodePreviewView extends Pane implements Observer{
    private GameNode node;
    private Rectangle rectangle;
    private Text nodeText;

    public GameNodePreviewView(GameNode node){
        this.node = node;

        if(node instanceof Observable observable){
            observable.addObserver(this);
        }
        this.getStylesheets().add(getClass().getResource("/preview.css").toExternalForm());
        this.getChildren().addAll(createNode());

    }

    public Pane createNode(){
        Pane pane = new Pane();
        
        // Vytvorenie obdĺžnika
        rectangle = new Rectangle();
        rectangle.getStyleClass().add("node-rectangle");
        rectangle.widthProperty().bind(this.widthProperty());
        rectangle.heightProperty().bind(this.heightProperty());
        
        // Vytvorenie textu, ktorý bude v strede
        if (this.node.getType() != NodeType.EMPTY) {
            nodeText = new Text(String.valueOf(node.getNumberOfTurns()));
        }else{
            nodeText = new Text("");
        }
        nodeText.getStyleClass().add("node-text");  // Ak máš CSS pre text
        nodeText.setStyle("-fx-font-size: 14; -fx-fill: black;");  // Nastavenie štýlu textu
        
        // Umiestnenie textu do stredu
        nodeText.xProperty().bind(pane.widthProperty().subtract(nodeText.getBoundsInLocal().getWidth()).divide(2));
        nodeText.yProperty().bind(pane.heightProperty().subtract(nodeText.getBoundsInLocal().getHeight()).divide(2));
        
        // Pridanie prvkov do panela
        pane.getChildren().addAll(rectangle, nodeText);
        return pane;
    }

    public void update(Observable observable) {
        if(node.getType() != NodeType.EMPTY){
            nodeText.setText(String.valueOf(node.getNumberOfTurns()));
        }
    }
}
