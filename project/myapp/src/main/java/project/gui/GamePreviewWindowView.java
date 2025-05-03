package project.gui;


import javafx.scene.Scene;
import javafx.stage.Stage;
import project.game.*;

public class GamePreviewWindowView extends Stage{
    public GamePreviewWindowView(Game game) {
        // Nastavenie názvu okna
        this.setTitle("Game Preview");

        // Vytvorenie herného poľa pre náhľad
        GameBoardPreviewView previewBoard = new GameBoardPreviewView(game);

        // Vytvorenie scény len s herným poľom
        Scene previewScene = new Scene(previewBoard);

        // Nastavenie scény
        this.setScene(previewScene);

        // Zakázanie zmeny veľkosti
        this.setResizable(false);
    }
}
