package project.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

/**
 * Manages the execution, undo, and redo of commands.
 *
 * This class maintains two stacks: one for undo operations and another for redo operations.
 * It allows commands to be executed, undone, and redone in a controlled manner.
 */
public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command and adds it to the undo stack.
     * Clears the redo stack after executing a new command.
     *
     * @param cmd the command to be executed.
     */
    public void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear();
    }

     /**
     * Undoes the last executed command, if any.
     * The undone command is pushed onto the redo stack.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command cmd = undoStack.pop();
            cmd.undo();
            redoStack.push(cmd);
        }
    }
    /**
     * Redoes the last undone command, if any.
     * The redone command is pushed onto the undo stack.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command cmd = redoStack.pop();
            cmd.execute();
            undoStack.push(cmd);
        }
    }


    /**
     * Loads commands from a file and adds them to the redo stack.
     * The file format should be such that each line represents a command to be executed.
     *
     * @param filename the name of the file to load commands from.
     * @param game the game instance where the commands will be applied.
     */
    public void loadCommandsFromFile(String filename, Game game) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            Stack<Command> reversedStack = new Stack<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4) {
                    int row = Integer.parseInt(parts[1]);
                    int col = Integer.parseInt(parts[2]);

                     // Command to turn (or revert the state of) the node
                    TurnCommand cmd = new TurnCommand(game.getGame()[row][col]);

                    // Add to redoStack (or executeCommand for undoStack)
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