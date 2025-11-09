package se233.chapter5part1;

import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se233.chapter5part1.model.Keys;

import static org.junit.jupiter.api.Assertions.*;

public class KeysTest {
    private Keys keys;

    @BeforeEach
    public void setUp() {
        keys = new Keys();
    }

    @Test
    public void singleKeyPress_givenKeyPressed_thenKeyStateIsTrue() {
        keys.add(KeyCode.A);
        assertTrue(keys.isPressed(KeyCode.A), "Key A should be pressed after being added");

        keys.remove(KeyCode.A);
        assertFalse(keys.isPressed(KeyCode.A), "Key A should not be pressed after being removed");
    }
    @Test
    public void multipleKeyPress_givenMultipleKeysPressed_thenAllKeysStateAreCorrect() {
        keys.add(KeyCode.A);
        keys.add(KeyCode.D);
        keys.add(KeyCode.W);
        assertTrue(keys.isPressed(KeyCode.A), "Key A should be pressed after being added");
        assertTrue(keys.isPressed(KeyCode.D), "Key D should be pressed after being added");
        assertTrue(keys.isPressed(KeyCode.W), "Key W should be pressed after being added");
        assertFalse(keys.isPressed(KeyCode.SPACE), "Key Space should not be pressed");

        keys.remove(KeyCode.A);
        keys.remove(KeyCode.D);
        assertFalse(keys.isPressed(KeyCode.A), "Key A should not be pressed after being removed");
        assertFalse(keys.isPressed(KeyCode.D), "Key A should not be pressed after being removed");
        assertTrue(keys.isPressed(KeyCode.W), "Key W should still be pressed");
        keys.remove(KeyCode.W);
        assertFalse(keys.isPressed(KeyCode.W), "Key W should not be pressed after being removed");
    }
    @Test
    public void defaultKeyState_givenUnpressedKeys_thenKeysStateAreFalse() {
        assertFalse(keys.isPressed(KeyCode.A), "Key A should not be pressed by default");
        assertFalse(keys.isPressed(KeyCode.D), "Key D should not be pressed by default");
        assertFalse(keys.isPressed(KeyCode.SPACE), "Key Space should not be pressed by default");
    }
}
