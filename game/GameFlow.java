package game;
import java.util.List;

import biuoop.KeyboardSensor;
import animations.AnimationRunner;
import animations.EndScreen;
import bodyGame.Counter;
import levels.LevelInformation;
/**
 * GameFlow - control the game (score).
 * run the levels and close when the game end
 */
public class GameFlow {
    private AnimationRunner ar;
    private KeyboardSensor ks;
    private Counter score;

    /**
     * Constructor.
     * @param ar - animation runner for the game
     * @param ks - keyboard for the game
     */
    public GameFlow(AnimationRunner ar, KeyboardSensor ks) {
        this.ar = ar;
        this.ks = ks;
        this.score = new Counter();
    }

    /**
     * Run the levels one after one (by the list of levels)
     * rematch the level if all the balls drop.
     * stop the game if the user win (complete all the levels) or lose (all
     * the balls are gone)
     * @param levels - list of the levels to run in the game
     */
    public void runLevels(List<LevelInformation> levels) {
        boolean win  = true;
        //Run the levels one after one
        for (LevelInformation levelInfo : levels) {
            //Create a game level and initialize it
            GameLevel level = new GameLevel(levelInfo, this.ar, this.ks, this.score);
            level.initialize();

            //while the user don't lose and there is still a block on the screen play a turn
            while (level.getRemainingBlocks() > 0) {
                level.playOneTurn();

                if (level.getRemainingBalls() == 0
                        && level.getRemainingBlocks() > 0) {
                    win = false;
                    break;
                }
            }
        }
        //Start the end screen and wait to spacer being pressed - close the gui
        this.ar.run(new EndScreen(win, this.ks, this.score));
    }
}
