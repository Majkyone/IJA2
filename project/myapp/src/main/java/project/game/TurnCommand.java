package project.game;
import project.common.*;
public class TurnCommand implements Command {
    private GameNode node;

    public TurnCommand(GameNode node) {
        this.node = node;
    }

    @Override
    public void execute() {
        node.turn(); // otočenie v smere hodinových ručičiek
    }

    @Override
    public void undo() {
        node.turnReverse(); // otočenie naspäť (v protismere)
    }
}
