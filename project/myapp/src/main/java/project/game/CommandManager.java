package project.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;


public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }

    public void loadCommandsFromFile(String filename, Game game) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Stack<Command> reversedStack = new Stack<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);

                    // Príkaz, ktorý tento node otočí (alebo obnoví jeho stav)
                    TurnCommand cmd = new TurnCommand(game.getGame()[row][col]);

                    // Pridaj do redoStacku (alebo executeCommand pre undoStack)
                    reversedStack.push(cmd);
                }
            }
            while (!reversedStack.isEmpty()) {
                redoStack.push(reversedStack.pop());
            }
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}