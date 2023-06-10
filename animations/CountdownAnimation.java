package animations;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Random;

import biuoop.DrawSurface;
import bodyGame.SpriteCollection;

/**
 * Countdown Animation - at the beginning of each turn.
 * implement Animation
 */
public class CountdownAnimation implements Animation {
    private int countFrom;
    private double showTime;
    private SpriteCollection gameScreen;
    private boolean firstRun;
    private long pervMilesSeconds;

    /**
     * Constructor.
     * @param numOfSeconds - the duration of the whole animation
     * @param countFrom - the number to count from
     * @param gameScreen - the sprites (background) we want to draw under the countdown
     */
    public CountdownAnimation(double numOfSeconds, int countFrom, SpriteCollection gameScreen) {
        this.countFrom = countFrom;
        //showtime of each number in milliseconds
        this.showTime = (numOfSeconds / countFrom) * 1000;
        this.gameScreen = gameScreen;
        this.firstRun = true;
    }

    /**
     * Draw the frame (the background and the countdown).
     * @param d - the surface we want to draw on
     */
    public void doOneFrame(DrawSurface d) {

        //Draw the background
        this.gameScreen.drawAllOn(d);

        //Draw the circles
        Random rand = new Random();
        d.setColor(new Color(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)));
        for (int i = 5; i > 0; i--) {
            d.drawCircle(d.getWidth() / 2, d.getHeight() / 2, 103 - i);
            d.drawCircle(d.getWidth() / 2, d.getHeight() / 2, 53 - i);
        }

        //Draw the number of the countdown
        d.setColor(java.awt.Color.YELLOW);
         d.drawText(d.getWidth() / 2 - 20, d.getHeight() / 2 + 30, Integer.toString(this.countFrom), 100);

        //First time only measure the time
        if (this.firstRun) {
            this.firstRun = false;
            this.pervMilesSeconds = System.currentTimeMillis();
        }

        //Not first time - check if showtime passed from the previous print
        if (!this.firstRun) {
            if ((System.currentTimeMillis() - this.pervMilesSeconds) >= this.showTime) {
                this.countFrom--;
                this.pervMilesSeconds = System.currentTimeMillis();
            }
        }
    }

    /**
     * Stop the countdown.
     * @return - true - stop the animation. false - continue the animation
     */
    public boolean shouldStop() {
        if (this.countFrom == 0) {
            return true;
        }
        return false;
    }

}
