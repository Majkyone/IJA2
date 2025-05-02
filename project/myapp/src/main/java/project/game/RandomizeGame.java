package project.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import project.common.GameNode;
import project.common.NodeType;
import project.common.Position;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomizeGame {
    private final Game game;
    private final GameLoader loader;
    private final Random random = new Random();

    public RandomizeGame(Game game, GameLoader loader) {
        this.game = game;
        this.loader = loader;

        // Spusti fázu 1
        playInitialRandomization(() -> {
            // Keď skončí fáza 1, spusti fázu 2
            playUntilNoBulbsOn();
            saveData();
        });
    }

    private void playInitialRandomization(Runnable onFinished) {
        List<Runnable> actions = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Position pos = loader.filledPositions.get(random.nextInt(loader.filledPositions.size()));
            int turns = random.nextInt(4);
            for (int j = 0; j < turns; j++) {
                actions.add(() -> game.node(pos).turn());
            }
        }

        Timeline timeline = new Timeline();
        int delay = 200;
        for (int i = 0; i < actions.size(); i++) {
            Runnable action = actions.get(i);
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * delay), e -> action.run()));
        }

        timeline.setOnFinished(e -> onFinished.run());
        timeline.play();
    }

    private void playUntilNoBulbsOn() {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
            if (!game.someBulbsAreOn()) {
                timeline.stop();
                return;
            }

            Position pos = loader.filledPositions.get(random.nextInt(loader.filledPositions.size()));
            int turns = random.nextInt(4);
            for (int i = 0; i < turns; i++) {
                game.node(pos).turn();
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void saveData() {
        try {
            File file = new File("data/currentLevel/levelData.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Zápis do súboru
            writer.write(game.rows() + " " + game.cols() + "\n");
            for (int r = 1; r <= game.rows(); r++) {
                for (int c = 1; c <= game.cols(); c++) {
                    GameNode[][] node = game.getGame();
                    if (node[r][c].getType() != NodeType.EMPTY) {
                        writer.write(node[r][c].toString() + "\n");
                    }
                }
            }
            // Ukončenie zápisu
            writer.close();
            removeUnnecessaryData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void removeUnnecessaryData(){
        try {
            FileWriter writer = new FileWriter("data/currentLevel/steps.txt");
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
