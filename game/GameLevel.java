package game;
import java.awt.Color;

import animations.Animation;
import animations.AnimationRunner;
import animations.CountdownAnimation;
import animations.PauseScreen;
import biuoop.DrawSurface;
import biuoop.KeyboardSensor;
import bodyGame.Ball;
import bodyGame.Block;
import bodyGame.Collidable;
import bodyGame.Counter;
import bodyGame.GameEnvironment;
import bodyGame.Paddle;
import bodyGame.Sprite;
import bodyGame.SpriteCollection;
import geometry.Point;
import geometry.Rectangle;
import indicators.LevelIndicator;
import indicators.ScoreIndicator;
import levels.LevelInformation;
import listeners.BallRemover;
import listeners.BlockRemover;
import listeners.ScoreTrackingListener;

/**
 * Initialize and run the level.
 * implement Animation
 */
public class GameLevel implements Animation {
    private SpriteCollection sprites;
    private GameEnvironment environment;
    private KeyboardSensor keyboard;
    private Paddle mainPaddle;
    private int xFrameSize = 800;
    private int yFrameSize = 600;
    private Counter remainingBlocks;
    private Counter score;
    private Counter remainingBalls;
    private AnimationRunner runner;
    private boolean running;
    private LevelInformation levelInformation;

    /**
     * Constructor.
     * @param levelInformation - the information of the level - how to build the level
     * @param ar - the animation runner that run the animations
     * @param ks - the keyboard to input the user controls
     * @param score - counter of the score
     */
    public GameLevel(LevelInformation levelInformation, AnimationRunner ar,
                     KeyboardSensor ks, Counter score) {
        this.levelInformation = levelInformation;
        this.runner = ar;
        this.keyboard = ks;
        this.score = score;
        this.remainingBlocks = new Counter();
        this.remainingBalls = new Counter();
        this.sprites  = new SpriteCollection();
        this.environment = new GameEnvironment();
    }
    /**
     * Add a Collidable  to the Collidable list.
     * @param c - the Collidable we want to add
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Add a Sprite to the Sprite list.
     * @param s - the Sprite we want to add
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }


    /**
     * Initialize the game - Create the Blocks,Ball,Paddle and add them to the lists.
     */
    public void initialize() {
        //Add Listeners
        //PrintingHitListener printer = new PrintingHitListener();
        BlockRemover blockRemover = new BlockRemover(this, this.remainingBlocks);
        ScoreTrackingListener scoreTrack = new ScoreTrackingListener(this.score);
        BallRemover deathRegion = new BallRemover(this, this.remainingBalls);

        //Add the background to the sprites
        this.addSprite(this.levelInformation.getBackground());

        //Add indicators
        ScoreIndicator scoreInd = new ScoreIndicator(new Rectangle(new Point(0, 0), xFrameSize + 1, 20), this.score);
        scoreInd.addToGame(this);
        LevelIndicator levelInd = new LevelIndicator(new Rectangle(new Point(0, 0), xFrameSize + 1, 20),
                                                    this.levelInformation.levelName());
        levelInd.addToGame(this);

        //Create the paddle
        Rectangle paddleRect = new Rectangle(new Point(400 - this.levelInformation.paddleWidth(), 560),
                                            this.levelInformation.paddleWidth(), 20);
        this.mainPaddle = new Paddle(paddleRect, this.keyboard, xFrameSize - 20, this.levelInformation.paddleSpeed());
        mainPaddle.addToGame(this);

        //Create Borders
        Block top = new Block(new Rectangle(new Point(0, 20), xFrameSize + 1, 20), Color.GRAY, 0);
        Block bottom = new Block(new Rectangle(new Point(0, yFrameSize - 1), xFrameSize + 1, 1), Color.GRAY, 0);
        Block left = new Block(new Rectangle(new Point(0, 0), 24, yFrameSize + 1), Color.GRAY, 0);
        Block right = new Block(new Rectangle(new Point(xFrameSize - 24, 0), 24, yFrameSize + 1), Color.GRAY, 0);
        top.addToGame(this);
        bottom.addToGame(this);
        left.addToGame(this);
        right.addToGame(this);

        //Remove bottom from sprite collection and add the bottom to the "death" region
        this.sprites.getSprites().remove(bottom);
        bottom.addHitListener(deathRegion);

        //Set the block counter
        this.remainingBlocks.increase(this.levelInformation.numberOfBlocksToRemove());

        //Add the block to the lists(collidable, sprites)
        for (Block currBlock: this.levelInformation.blocks()) {
            currBlock.addToGame(this);
            //currBlock.addHitListener(printer);
            currBlock.addHitListener(blockRemover);
            currBlock.addHitListener(scoreTrack);
        }
    }

    /**
     * Run the game - start the infinite animation loop.
     * the loop:
     * Draw the background
     * draw all the Sprites in Sprite list
     * shot the gui
     * check collision by calling notifyAll
     */

    /**
     * PlayOneTurn - initiliaze the turn, run the countdown animation.
     * and coninute untill the turn's end (no more balls/blocks)
     */
    public void playOneTurn() {
        //Middle the paddle, create the balls and update the balls counter
        initializeTurn();
        this.runner.run(new CountdownAnimation(2, 3, this.sprites));
        this.running = true;
        this.runner.run(this);
    }

    /**
     * initiliazeTurn - called each turn.
     * Middle the paddle, create the balls and update the balls counter
     */
    private void initializeTurn() {
        this.mainPaddle.middlePaddle();

        Ball ball;
        for (int i = 0; this.levelInformation.numberOfBalls() > i; i++) {
            ball = new Ball(new Point(400, 500), 7, java.awt.Color.WHITE,
                    environment);
            ball.setVelocity(this.levelInformation.initialBallVelocities().get(i));
            ball.addToGame(this);
            this.remainingBalls.increase(1);
        }
    }

    /**
     * Remove Collidable from the Collidables list.
     * @param c - the Collidable we want to remove.
     */
    public void removeCollidable(Collidable c) {
        this.environment.getCollidables().remove(c);
    }

    /**
     * Remove Sprite from the Sprites list.
     * @param s - the Sprite we want to remove.
     */
    public void removeSprite(Sprite s) {
        this.sprites.getSprites().remove(s);
    }

    /**
     * Draw one frame, draw and notify.
     * @param d - the surface we want to draw on
     */
    public void doOneFrame(DrawSurface d) {
        //Draw all the Sprites
        this.sprites.drawAllOn(d);
        //notify all sprite TimePassed
        this.sprites.notifyAllTimePassed();

        //Check for pause game
        if (this.keyboard.isPressed("p")) {
            this.runner.run(new PauseScreen(this.keyboard));
        }
    }

    /**
     * When the animation should stop.
     * the remaining blocks is 0 / the remaining balls is 0
     * @return true - stop the animation. false - continue the animation
     */
    public boolean shouldStop() {
        //There is no more blocks - the user win the level
        if (this.remainingBlocks.getValue() == 0) {
            //Give the user points
            this.score.increase(100);
            //Stop the animation
            this.running = true;
        } else if (this.remainingBalls.getValue() == 0) {
            //There is no more balls - the user lose the level
            this.running = true;
        } else {
            //The turn shouldn't stop
            this.running = false;
        }
        return this.running;
    }

    /**
     * @return - the number of the remaining blocks
     */
    public int getRemainingBlocks() {
        return this.remainingBlocks.getValue();
    }
    public int getRemainingBalls(){
        return this.remainingBalls.getValue();
    }
}
