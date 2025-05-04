package project.game;

/**
 * Interface representing a command that can be executed and undone.
 *
 * This interface is typically used in the Command design pattern,
 * where concrete implementations define specific actions that can be executed
 * and reversed.
 */
public interface Command {
     /**
     * Executes the command.
     * This method performs the action defined by the command.
     */
    void execute();
     /**
     * Undoes the command.
     * This method reverts the action performed by the execute method.
     */
    void undo();
}