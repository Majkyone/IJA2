package ija.ija2024.homework2.game;

import java.util.Random;
import ija.ija2024.homework2.common.Position;

public class RandomizeGame {
    public RandomizeGame(Game game, GameLoader loader){
        Random random = new Random();
        int numberOfTurns = 0;
        while (numberOfTurns < 10) {
            Position pos = loader.filledPositions.get(random.nextInt(loader.filledPositions.size()));
            int turns = random.nextInt(4);
            for (int i = 0; i < turns; i++) {
                turn(500, game, pos);
            }
            numberOfTurns++;
        }
        while (game.someBulbsAreOn()) {
            Position pos = loader.filledPositions.get(random.nextInt(loader.filledPositions.size()));
            int turns = random.nextInt(4);
            for (int i = 0; i < turns; i++) {
                turn(500, game, pos);
            }
        }
    }

    private static void turn(int ms, Game game, Position pos) {
        sleep(ms);
        game.node(pos).turn();
    }

    private static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            System.out.println("Sleep was interrupted.");
        }
    }
}
