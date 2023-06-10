package listeners;
import game.GameLevel;
import bodyGame.Ball;
import bodyGame.Block;
import bodyGame.Counter;

/**
 * Ball remover. remove Ball when hit the "death" blocks
 * implements HitListener
 */
public class BallRemover implements HitListener {

    private GameLevel game;
    private Counter remainingBalls;

    /**
     * Constructor.
     * @param game - the game we want to remove the ball from
     * @param remainingBalls - counter of the balls
     */
    public BallRemover(GameLevel game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    /**
     * When the block hit by the ball, remove the ball from the game
     * and update the counter.
     * @param beingHit - the block that being hit
     * @param hitter - the ball that hit the block - we need to remove from game
     */
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.game);
        remainingBalls.decrease(1);
    }

}
