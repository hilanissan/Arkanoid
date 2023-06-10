package animations;
import java.awt.Color;

import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import bodyGame.Counter;

/**
 * End screen Animation - at the end of the game, shows the score.
 * implement Animation
 */
public class EndScreen implements Animation {
    private boolean win;
    private boolean stop;
    private Counter score;
    private KeyboardSensor keyboard;

    /**
     * Constructor.
     * @param win - boolean if the user win or loose
     * @param ks - keyboard to check if sensor is press to quit from the game
     * @param score - the score the user score
     */
    public EndScreen(boolean win, KeyboardSensor ks, Counter score) {
        this.win = win;
        this.keyboard = ks;
        this.score = score;
    }

    /**
     * Draw the frame and the score of the user (add crown if the user win).
     * @param d - the surface we want to draw on
     */
    public void doOneFrame(DrawSurface d) {
        //Background and frame
        d.setColor(new Color(105, 105, 105));
        d.fillRectangle(0, 0, d.getWidth(), d.getHeight());
        for (int i = 12; i > 0; i--) {
            d.setColor(new Color(255 - i * 5, 255 - i * 5, 255 - i * 5));
            d.drawRectangle(0, 0, d.getWidth() - i, d.getHeight() - i);
        }
        if (this.win) {
            d.drawText(d.getWidth() / 4, d.getHeight() / 2, "You win! Your score is "
                                                + this.score.getValue(), 32);
        } else {
            d.setColor(new Color(178, 34, 34));
            d.drawText(d.getWidth() / 4, d.getHeight() / 2, "Game Over.", 60);
            d.drawText(d.getWidth() / 4 + 50, d.getHeight() / 2 + 100, "Your score is " + this.score.getValue(), 32);
        }
        //check if save bar is pressed to quit the game
        if (this.keyboard.isPressed(KeyboardSensor.SPACE_KEY)) {
            this.stop = true;
        }

    }


    /**
     * Stop the countdown.
     * @return - true - stop the animation. false - continue the animation
     */
    public boolean shouldStop() {
        return this.stop;
    }

}
