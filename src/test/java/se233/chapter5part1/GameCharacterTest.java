package se233.chapter5part1;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part1.model.GameCharacter;
import se233.chapter5part1.view.GameStage;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class GameCharacterTest {
    Field xVelocityField, yVelocityField, yAccelerationField,
            xCoordinateField, yCoordinateField, isFallingField, canJumpField, isJumpingField, isMoveRightField, isMoveLeftField,
            xCoordinateFieldOther, yCoordinateFieldOther;
    private GameCharacter gameCharacter;
    private GameCharacter otherCharacter;

    @BeforeAll
    public static void initJfxRuntime() {
        javafx.application.Platform.startup(() -> {});
    }
    @BeforeEach
    public void setUp() throws NoSuchFieldException {
        gameCharacter = new GameCharacter(0, 30, 30, "assets/Character1.png", 4, 3, 2, 111, 97, KeyCode.A, KeyCode.D, KeyCode.W);
        otherCharacter = new GameCharacter(1, GameStage.WIDTH-60, 30, "assets/Character2.png", 4, 4 ,1, 129,66, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP);
        xVelocityField = gameCharacter.getClass().getDeclaredField("xVelocity");
        yVelocityField = gameCharacter.getClass().getDeclaredField("yVelocity");
        yAccelerationField = gameCharacter.getClass().getDeclaredField("yAcceleration");
        xCoordinateField = gameCharacter.getClass().getDeclaredField("x");
        yCoordinateField = gameCharacter.getClass().getDeclaredField("y");
        xCoordinateFieldOther = otherCharacter.getClass().getDeclaredField("x");
        yCoordinateFieldOther = otherCharacter.getClass().getDeclaredField("y");
        isFallingField = gameCharacter.getClass().getDeclaredField("isFalling");
        canJumpField = gameCharacter.getClass().getDeclaredField("canJump");
        isJumpingField = gameCharacter.getClass().getDeclaredField("isJumping");
        isMoveLeftField = gameCharacter.getClass().getDeclaredField("isMoveLeft");
        isMoveRightField = gameCharacter.getClass().getDeclaredField("isMoveRight");

        xVelocityField.setAccessible(true);
        yVelocityField.setAccessible(true);
        yAccelerationField.setAccessible(true);
        xCoordinateField.setAccessible(true);
        yCoordinateField.setAccessible(true);
        isFallingField.setAccessible(true);
        canJumpField.setAccessible(true);
        isJumpingField.setAccessible(true);
        isMoveRightField.setAccessible(true);
        isMoveLeftField.setAccessible(true);

        xCoordinateFieldOther.setAccessible(true);
        yCoordinateFieldOther.setAccessible(true);
    }
    @Test
    public void respawn_givenNewGameCharacter_thenCoordinatesAre30_30() {
        gameCharacter.respawn();
        assertEquals(30, gameCharacter.getX(), "Initial x");
        assertEquals(30, gameCharacter.getY(), "Initial y");
    }
    @Test
    public void respawn_givenNewGameCharacter_thanScoreIs0() {
        gameCharacter.respawn();
        assertEquals(0, gameCharacter.getScore(), "Initial score");
    }
    @Test
    public void moveX_givenMoveRightOnce_thenXCoordinateIncreasedByXVelocity() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveRight();
        gameCharacter.moveX();
        assertEquals(30 + xVelocityField.getInt(gameCharacter), gameCharacter.getX(), "Move right x");
    }
    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYVelocityIncrease() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveY();
        int yVelocity1 = yVelocityField.getInt(gameCharacter);
        gameCharacter.moveY();
        int yVelocity2 = yVelocityField.getInt(gameCharacter);
        assertTrue(yVelocity2>yVelocity1, "Velocity is increasing");
    }
    @Test
    public void moveY_givenTwoConsecutiveCalls_thenYAccelerationUnchanged() throws IllegalAccessException {
        gameCharacter.respawn();
        gameCharacter.moveY();
        int yAcceleration1 = yAccelerationField.getInt(gameCharacter);
        gameCharacter.moveY();
        int yAcceleration2 = yAccelerationField.getInt(gameCharacter);
        assertTrue(yAcceleration1 == yAcceleration2, "Acceleration is not change");
    }

    @Test
    public void checkReachGameWall_givenCharacterAtLeftBoundary_thenXCoordinateIsZero() throws IllegalAccessException {
        gameCharacter.respawn();
        xCoordinateField.setInt(gameCharacter, -10);
        gameCharacter.checkReachGameWall();
        assertEquals(0, gameCharacter.getX(), "Character should be positioned at left boundary");
    }
    @Test
    public void checkReachGameWall_givenCharacterAtRightBoundary_thenXCoordinateIsGameWidth() throws IllegalAccessException {
        gameCharacter.respawn();
        xCoordinateField.setInt(gameCharacter, GameStage.WIDTH+20);
        gameCharacter.checkReachGameWall();
        assertEquals(GameStage.WIDTH, gameCharacter.getX(), "Character should be positioned at right boundary");
    }
    @Test
    public void jump_givenCharacterCanJump_thenJumpIsInitiated() throws IllegalAccessException {
        gameCharacter.respawn();
        canJumpField.setBoolean(gameCharacter, true);
        isFallingField.setBoolean(gameCharacter, false);
        isJumpingField.setBoolean(gameCharacter, false);
        gameCharacter.jump();
        assertFalse(canJumpField.getBoolean(gameCharacter), "Character should not be able to jump after jumping");
        assertTrue(isJumpingField.getBoolean(gameCharacter), "Character should be in jumping state after jumping");
        assertFalse(isFallingField.getBoolean(gameCharacter), "Character should not be falling when jumping");
    }
    @Test
    public void jump_givenCharacterCannotJump_thenJumpIsNotInitiated() throws IllegalAccessException {
        gameCharacter.respawn();
        canJumpField.setBoolean(gameCharacter, false);
        isFallingField.setBoolean(gameCharacter, true);
        isJumpingField.setBoolean(gameCharacter, false);
        gameCharacter.jump();
        assertFalse(canJumpField.getBoolean(gameCharacter), "Character should not be able to jump");
        assertFalse(isJumpingField.getBoolean(gameCharacter), "Character should not be in jumping state");
        assertTrue(isFallingField.getBoolean(gameCharacter), "Character should be falling");
    }
    @Test
    public void collided_givenHorizontalCollision_thenCharacterStops() throws IllegalAccessException {
        gameCharacter.respawn();
        otherCharacter.respawn();
        xCoordinateField.setInt(gameCharacter, 100);
        xCoordinateFieldOther.setInt(otherCharacter, 200);
        isMoveRightField.setBoolean(gameCharacter,true);

        boolean isCollided = gameCharacter.collided(otherCharacter);
        assertFalse(isCollided, "Horizontal Collision should return false");
        assertFalse(isMoveRightField.getBoolean(gameCharacter), "Character should stop moving right after collision");
        assertFalse(isMoveLeftField.getBoolean(gameCharacter), "Character should stop moving left after collision");
    }
    @Test
    public void collided_givenVerticalCollision_thenScoreIncreaseAndOtherCharacterCollapsed() throws IllegalAccessException {
        gameCharacter.respawn();
        otherCharacter.respawn();

        xCoordinateField.setInt(gameCharacter, 100);
        xCoordinateFieldOther.setInt(otherCharacter, 100);
        yCoordinateField.setInt(gameCharacter, 100);
        yCoordinateFieldOther.setInt(otherCharacter, 200);

        isFallingField.setBoolean(gameCharacter, true);

        int initialScore = gameCharacter.getScore();
        boolean isCollided = gameCharacter.collided(otherCharacter);

        assertTrue(isCollided, "Vertical Collision should return true");
        assertEquals(initialScore+1, gameCharacter.getScore(), "Score should be increased by 1 after vertical collision");
    }
}