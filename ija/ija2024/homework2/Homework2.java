package ija.ija2024.homework2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Node;

import ija.ija2024.homework2.common.GameNode;
import ija.ija2024.homework2.common.NodeType;
//--- Importy z implementovaneho reseni ukolu
import ija.ija2024.homework2.common.Position;
import ija.ija2024.homework2.common.Side;
import static ija.ija2024.homework2.common.Side.*;
import ija.ija2024.homework2.game.Game;
import ija.ija2024.homework2.game.GameLoader;
import ija.ija2024.homework2.game.RandomizeGame;
//--- Importy z baliku dodaneho nastroje
import ija.ija2024.tool.EnvPresenter;

public class Homework2 {

    public static void main(String... args) {
        int level = 1;
        GameLoader loader = new GameLoader("ija/ija2024/homework2/levels/level"+ level +".txt");
        Game game = Game.create(loader.x, loader.y);
        loader.SetUpGame(game);
        game.init();

        EnvPresenter presenter = new EnvPresenter(game);
        presenter.open();
        RandomizeGame randomGame = new RandomizeGame(game, loader);
        for (int r = 1; r <= game.rows(); r++) {
            for (int c = 1; c <= game.cols(); c++) {
                if (game.node(new Position(r, c)).getType() == NodeType.EMPTY || game.node(new Position(r, c)).getNumberOfSides() == 4) {
                    System.out.print("0 "); 
                }else{
                    System.out.print(game.node(new Position(r, c)).getNumberOfturns()+ " "); 

                }
            }
            System.out.println();
        }
    }

}
