package com.qthstudios.game.gosky.model;

import android.util.Log;

import com.qthstudios.game.gosky.config.Assets;
import com.qthstudios.game.gosky.framework.math.OverlapTester;
import com.qthstudios.game.gosky.framework.math.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {


    public interface WorldListener {
        public void jump();

        public void highJump();

        public void hit();

        public void coin();
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 199 * 15;
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -12);

    public final Bob bob;
    public final List<Platform> platforms;
    public final List<Float> platformsPositions;
    public final List<Spring> springs;
    public final List<Star> stars;
    public Castle castle;
    public final WorldListener listener;
    public final Random rand;

    public float heightSoFar;
    public int score;
    public int maxScore;
    public int state;

    public float stateTime;

    public World(WorldListener listener) {
        this.bob = new Bob(5, 1);
        this.platforms = new ArrayList<Platform>();
        this.platformsPositions = new ArrayList<Float>();
        this.springs = new ArrayList<Spring>();
        stars = new ArrayList<Star>();
        this.listener = listener;
        rand = new Random();
        generateLevel();
        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
    }

    private void generateLevel() {
        float y = Platform.PLATFORM_HEIGHT / 2;
        float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY
                / (2 * -gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            int type;
            float platformWidth;
            float x;
            // First platform always is a static & place in center & no spring
            if (platforms.size() == 0) {
                type = Platform.PLATFORM_TYPE_STATIC;
                platformWidth = Platform.PLATFORM_WIDTH_MAX;
                x = WORLD_WIDTH / 2;
            } else {
                // From the platform number 2, all will be random
                type = rand.nextFloat() > Platform.PLATFORM_TYPE_MOVING_PERCENT ? Platform.PLATFORM_TYPE_MOVING
                        : Platform.PLATFORM_TYPE_STATIC;
                platformWidth = rand.nextFloat()  * (Platform.PLATFORM_WIDTH_MAX -
                        Platform.PLATFORM_WIDTH_MIN) + Platform.PLATFORM_WIDTH_MIN;
                x = rand.nextFloat()
                        * (WORLD_WIDTH - platformWidth)
                        + platformWidth / 2;
            }

            Platform platform = new Platform(type, x, y, platformWidth);
            platformsPositions.add(y);
            platforms.add(platform);

            // Generate springs
            if (rand.nextFloat() > Platform.PLATFORM_TYPE_SPRING_PERCENT
                    && type != Platform.PLATFORM_TYPE_MOVING && platforms.size() > 1) {
                Spring spring = new Spring(platform.position.x,
                        platform.position.y + Platform.PLATFORM_HEIGHT * 0.75f
                                + Spring.SPRING_HEIGHT / 2);
                springs.add(spring);
            }

//            if (y > WORLD_HEIGHT / 3 && rand.nextFloat() > 0.8f) {
//                Squirrel squirrel = new Squirrel(platform.position.x
//                        + rand.nextFloat(), platform.position.y
//                        + Squirrel.SQUIRREL_HEIGHT + rand.nextFloat() * 2);
//                squirrels.add(squirrel);
//            }

//            if (rand.nextFloat() > 0.6f) {
//                Coin coin = new Coin(platform.position.x + rand.nextFloat(),
//                        platform.position.y + Coin.COIN_HEIGHT
//                                + rand.nextFloat() * 3);
//                coins.add(coin);
//            }

            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight / 3);
        }

        castle = new Castle(WORLD_WIDTH / 2, y);
    }

    public void update(float deltaTime, float accelX) {
        updateBob(deltaTime, accelX);
        updatePlatforms(deltaTime);
//        updateSquirrels(deltaTime);
//        updateCoins(deltaTime);
        updateScore();
        if (bob.state != Bob.BOB_STATE_HIT && bob.velocity.y > -25) {
            checkCollisions();
        }
        if (bob.velocity.y < -25 && !bob.isDead) {
            bob.isDead = true;
            Assets.nyan1.play();
        }
        updateStars(deltaTime);
        checkGameOver();
    }

    private void updateStars(float deltaTime) {
        if (bob.isDead) {
            // Update common state time (use for all stars)
            stateTime += deltaTime;
//            Log.e("TRUNGDQ", "stateTime: " + stateTime + " first con: " + ((int) stateTime % Star.STAR_SPEED));
            // Add new stars
//            if ((int) (stateTime * 100) % Star.STAR_SPEED == 0 && !starAddedFlag) {
//                starAddedFlag = true;
                stars.add(new Star(
                        (float) (bob.position.x + (Math.random() * 3) - 1.5),
                        (float) (bob.position.y + (Math.random() * 3) - 1.5),
                        Star.STAR_WIDTH, Star.STAR_HEIGHT));
                if (stars.size() > Star.STAR_MAX_COUNT) {
                    stars.remove(0);
                }
//            } else {
//                if ((int) (stateTime * 100) % Star.STAR_SPEED != 0) {
//                starAddedFlag = false;
//                }
//            }

            // Update stars state time;
            for (Star star : stars) {
                star.stateTime += deltaTime;
            }
        }
    }

    private void updateScore() {
        for (int i = 0; i < platformsPositions.size(); ++i) {
            if (platformsPositions.get(i) > bob.position.y) {
                score = i - 1;
                break;
            }
        }

        if (maxScore < score) maxScore = score;

    }

    private void updateBob(float deltaTime, float accelX) {
        if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f)
            bob.hitPlatform();
        if (bob.state != Bob.BOB_STATE_HIT)
            bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
        bob.update(deltaTime);
        heightSoFar = Math.max(bob.position.y, heightSoFar);
    }

    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            platform.update(deltaTime);
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING
                    && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }

//    private void updateSquirrels(float deltaTime) {
//        int len = squirrels.size();
//        for (int i = 0; i < len; i++) {
//            Squirrel squirrel = squirrels.get(i);
//            squirrel.update(deltaTime);
//        }
//    }

//    private void updateCoins(float deltaTime) {
//        int len = coins.size();
//        for (int i = 0; i < len; i++) {
//            Coin coin = coins.get(i);
//            coin.update(deltaTime);
//        }
//    }

    private void checkCollisions() {
        checkPlatformCollisions(); //
//        checkSquirrelCollisions();
        checkItemCollisions(); //
        checkCastleCollisions(); //
    }

    private void checkPlatformCollisions() {
        if (bob.velocity.y > 0)
            return;

        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if (bob.position.y > platform.position.y) {
                if (OverlapTester
                        .overlapRectangles(bob.bounds, platform.bounds)) {
                    bob.hitPlatform();
                    listener.jump();
                    // Destroy after touch
                    platform.pulverize();
//                    if (rand.nextFloat() > 0.5f) {
//                        platform.pulverize();
//                    }
                    break;
                }
            }
        }
    }

//    private void checkSquirrelCollisions() {
//        int len = squirrels.size();
//        for (int i = 0; i < len; i++) {
//            Squirrel squirrel = squirrels.get(i);
//            if (OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
//                bob.hitSquirrel();
//                listener.hit();
//            }
//        }
//    }

    private void checkItemCollisions() {
//        int len = coins.size();
//        for (int i = 0; i < len; i++) {
//            Coin coin = coins.get(i);
//            if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
//                coins.remove(coin);
//                len = coins.size();
//                listener.coin();
//                score += Coin.COIN_SCORE;
//            }
//        }

        if (bob.velocity.y > 0)
            return;

        int len = springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = springs.get(i);
            if (bob.position.y > spring.position.y - spring.bounds.height / 2) {
                if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
                    bob.hitSpring();
                    listener.highJump();
                }
            }
        }
    }

    private void checkCastleCollisions() {
        if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds)) {
            state = WORLD_STATE_NEXT_LEVEL;
        }
    }

    private void checkGameOver() {
        if (bob.position.y < 0.7f) {
            state = WORLD_STATE_GAME_OVER;
            bob.state = Bob.BOB_STATE_HIT;
            bob.isDead = false;
            stars.clear();
            Assets.playSound(Assets.hitSound);
            if (Assets.nyan1.isPlaying()) {
                Assets.nyan1.stop();
            }
            if (Assets.nyan2.isPlaying()) {
                Assets.nyan2.stop();
            }
        }
    }
}
