package project.game;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import project.common.*;
/**
 * The {@link GameLoader} class is responsible for loading game configuration from a file
 * and setting up a game board accordingly.
 * <p>
 * It reads the game grid dimensions and node definitions from a specified file,
 * then processes each node type (Link, Bulb, Power) and its respective position and sides.
 */
public class GameLoader {
    public int x;
    public int y;
    public Object[][] def;
    public List<Position> filledPositions = new ArrayList<>();
    /**
     * Constructs a {@link GameLoader} to load the game configuration from the specified file.
     * 
     * @param fileString The path to the file containing the game configuration.
     * @throws IOException If there is an error reading the file.
     */
    public GameLoader(String fileString){
        File file = new File(fileString);
        List<Object[]> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    x = Integer.parseInt(parts[0]);
                    y = Integer.parseInt(parts[1]);
                }
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                List<Object> entry = new ArrayList<>();
                entry.add(parts[0]); // typ ("L", "B", "P")

                for (int i = 1; i < parts.length; i++) {
                    try {
                        entry.add(Integer.parseInt(parts[i]));
                    } catch (NumberFormatException e) {
                        entry.add(parts[i].toUpperCase()); // smer ako String
                    }
                }

                dataList.add(entry.toArray());
            }

            def = new Object[dataList.size()][];
            def = dataList.toArray(def);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      /**
     * Sets up the game by creating nodes on the game grid based on the loaded configuration.
     * <p>
     * This method iterates through the node definitions and creates the appropriate nodes
     * (link, bulb, or power) at their specified positions, attaching the corresponding sides.
     * 
     * @param game The {@link Game} instance where the nodes will be created.
     */
    public void SetUpGame(Game game){
        for (Object[] n : def) {
            String type = (String) n[0];
            int row = (Integer) n[1];
            int col = (Integer) n[2];
            Position p = new Position(row, col);
            filledPositions.add(p);
            Side sides[] = new Side[n.length - 3];
            for (int i = 3; i < n.length; i++) {
                sides[i - 3] = Side.valueOf((String) n[i]);
            }
            switch (type) {
                case "L" -> game.createLinkNode(p, sides);
                case "B" -> game.createBulbNode(p, sides[0]);
                case "P" -> game.createPowerNode(p, sides);
            }
        }
    }
}