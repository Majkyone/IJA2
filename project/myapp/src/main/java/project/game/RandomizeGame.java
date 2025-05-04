package project.game;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import project.common.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@link RandomizeGame} class handles the randomization of game elements 
 * in the game, including the initial randomization phase and the phase where 
 * the game continues until no bulbs are turned on.
 * <p>
 * This class coordinates the game setup and randomization using a series of
 * timelines, actions, and subsequent game state checks.
 */
public class RandomizeGame {
    private final Game game;
    private final GameLoader loader;
    private final Random random = new Random();

    /**
     * Initializes the game randomization process.
     * <p>
     * This method triggers the following sequence:
     * 1. {@link #playInitialRandomization(Runnable)} starts the initial randomization.
     * 2. Once the first phase finishes, {@link #playUntilNoBulbsOn(Runnable)} is triggered.
     * 3. When all bulbs are off, the game data is saved via {@link #saveData()}.
     * 
     * @param game The game instance to randomize.
     * @param loader The GameLoader instance used to load the game's initial state.
     */
    public RandomizeGame(Game game, GameLoader loader) {
        this.game = game;
        this.loader = loader;

        // Start phase 1
        playInitialRandomization(() -> {
            // Once phase 1 finishes, start phase 2
            playUntilNoBulbsOn(() -> {
                // After phase 2 ends, save game data
                saveData();
            });
        });
    }

    /**
     * Executes the initial randomization phase where random positions are selected
     * and nodes are rotated a random number of times.
     * 
     * @param onFinished Runnable callback to execute after the initial randomization phase.
     */
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

    /**
     * Executes the second phase where random positions are rotated until no bulbs are on.
     * 
     * @param onFinished Runnable callback to execute after all bulbs are off.
     */
    private void playUntilNoBulbsOn(Runnable onFinished) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
    
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
            if (!game.someBulbsAreOn()) {
                timeline.stop();
                // Invoke onFinished after stopping the animation
                onFinished.run();
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

    /**
     * Saves the current game state to a file for future use.
     * 
     * @throws IOException If an I/O error occurs during file writing.
     */
    private void saveData() {
        try {
            File file = new File("data/currentLevel/levelData.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // Write to file
            writer.write(game.rows() + " " + game.cols() + "\n");
            for (int r = 1; r <= game.rows(); r++) {
                for (int c = 1; c <= game.cols(); c++) {
                    GameNode[][] node = game.getGame();
                    if (node[r][c].getType() != NodeType.EMPTY) {
                        writer.write(node[r][c].toString() + "\n");
                    }
                }
            }
             // Close file writer
            writer.close();
            removeUnnecessaryData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


     /**
     * Removes unnecessary data by clearing the steps file after saving the game state.
     * 
     * @throws IOException If an I/O error occurs while clearing the file.
     */
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