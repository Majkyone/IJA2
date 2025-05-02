package project.game;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import project.common.*;
public class GameLoader {
    public int x;
    public int y;
    public Object[][] def;
    public List<Position> filledPositions = new ArrayList<>();
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
