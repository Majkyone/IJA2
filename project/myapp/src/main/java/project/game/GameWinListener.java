package project.game;

/**
 * The {@link GameWinListener} interface defines a listener for game win events.
 * <p>
 * Implementations of this interface can be used to listen for changes in the game's win state.
 */
public interface GameWinListener {
    /**
     * This method is called when the game reaches a win state.
     * 
     * @param isWin A boolean indicating whether the game has been won (true) or not (false).
     */
    void onGameWin(boolean isWin);
}
