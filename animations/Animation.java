package animations;
import biuoop.DrawSurface;
/**
 *  Animation Interface.
 * animations : countdown, endscreen, pausescreen
 */
public interface Animation {
    /**
     * Draw frame of the animation.
     * @param d - the surface to drow on it
     */
    void doOneFrame(DrawSurface d);

    /**
     * When to end the animation.
     * @return - true to stop animation, false to continue.
     */
    boolean shouldStop();
}
