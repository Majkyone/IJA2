package project.game;
import project.common.*;

/**
 * The {@link TurnCommand} class represents a command to rotate a {@link GameNode}.
 * <p>
 * This class encapsulates the action of rotating a game node either clockwise (execute) 
 * or counterclockwise (undo) as part of a command pattern. It is used to allow the 
 * rotation of nodes to be executed and undone, enabling undo functionality in the game.
 */
public class TurnCommand implements Command {
    private GameNode node;

    /**
     * Creates a new {@link TurnCommand} for a given game node.
     * 
     * @param node The {@link GameNode} that will be rotated by this command.
     */
    public TurnCommand(GameNode node) {
        this.node = node;
    }

    /**
     * Executes the command to rotate the game node clockwise (in the direction of the clock).
     * This method will be invoked to perform the rotation action.
     */
    @Override
    public void execute() {
        node.turn(); // otočenie v smere hodinových ručičiek
    }

     /**
     * Undoes the command by rotating the game node counterclockwise (opposite direction of the clock).
     * This method will be invoked to reverse the rotation action.
     */
    @Override
    public void undo() {
        node.turnReverse(); // otočenie naspäť (v protismere)
    }
}