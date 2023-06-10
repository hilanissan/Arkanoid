package listeners;
import bodyGame.Ball;
import bodyGame.Block;
import bodyGame.Counter;

/**
 * ScoreTrackingListener . update the counter of the score
 * implements HitListener
 */
public class ScoreTrackingListener implements HitListener {

    private Counter currentScore;

    /**
     * Constructor.
     * @param scoreCounter - the counter that save the score of the user
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.currentScore = scoreCounter;
    }


    /**
     * Update the score when a block being hit.
     * @param beingHit - the block that being hit
     * @param hitter - the ball that hit the block
     */
    public void hitEvent(Block beingHit, Ball hitter) {
            this.currentScore.increase(5);
        }

    }


