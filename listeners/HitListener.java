package listeners;
import bodyGame.Ball;
import bodyGame.Block;

/**
 * Hit Listener interface.
 */
public interface HitListener {
    /**
     * This method is called whenever the beingHit object is hit.
     * @param beingHit - the block that being hit.
     * @param hitter - the Ball that's doing the hitting.
     */
       void hitEvent(Block beingHit, Ball hitter);
}
